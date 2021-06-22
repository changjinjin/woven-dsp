package com.info.baymax.common.http;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.core.env.Environment;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * create by pengchuan.chen on 2020/6/17
 */
@Slf4j
public class KerberosAuthenticator {

  protected static final String DEFAULT_HADOOP_USER_NAME = "merce";

  protected static final String SECURITY_KRB5_CONF = "woven.kerberos.%1$s.krb5";

  protected static final String SECURITY_KEYTABLE = "woven.kerberos.%1$s.keytable";

  protected static final String SECURITY_PRINCIPAL = "woven.kerberos.%1$s.principal";

  @Getter
  protected String hadoop_user_name;

  protected HttpClient httpClient;

  private LoginContext loginContext;

  private KerberosParameter kerberosParameter;

  public KerberosAuthenticator() {
    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(5000)
            .setConnectionRequestTimeout(1000)
            .build();
    httpClient = HttpClientBuilder.create()
            .setDefaultRequestConfig(requestConfig)
            .build();
  }

  public KerberosAuthenticator(KerberosParameter kerberosParameter) {
    this.kerberosParameter = kerberosParameter;
    System.setProperty("java.security.krb5.conf", kerberosParameter.getKrb5Conf());
    if (log.isDebugEnabled()) {
      System.setProperty("sun.security.spnego.debug", "true");
      System.setProperty("sun.security.krb5.debug", "true");
    }
    httpClient = buildSpengoHttpClient();
    loginContext = login();
  }

  private HttpClient buildSpengoHttpClient() {
    HttpClientBuilder builder = HttpClientBuilder.create();
    Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
            .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true))
            .build();
    builder.setDefaultAuthSchemeRegistry(authSchemeRegistry);

    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(new AuthScope(null, -1, null), new Credentials() {
      @Override
      public Principal getUserPrincipal() {
        return null;
      }

      @Override
      public String getPassword() {
        return null;
      }
    });
    builder.setDefaultCredentialsProvider(credentialsProvider);

    RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setSocketTimeout(5000)
            .setConnectionRequestTimeout(1000)
            .build();
    builder.setDefaultRequestConfig(requestConfig);

    CloseableHttpClient httpClient = builder.build();
    return httpClient;
  }

  protected LoginContext login() {
    Configuration config = new Configuration() {
      @SuppressWarnings("serial")
      @Override
      public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        Map<String, Object> options = new HashMap<String, Object>() {{
          put("keyTab", kerberosParameter.getKeyTab());
          put("principal", kerberosParameter.getPrincipal());
          put("useTicketCache", "false");
//          put("ticketCache", "/tmp/kerberos/cache");
//          put("renewTGT", "true");
          put("useKeyTab", "true");
          //Krb5 in GSS API needs to be refreshed so it does not throw the error
          //Specified version of key is not available
          put("refreshKrb5Config", "true");
          put("storeKey", "true");
          put("doNotPrompt", "true");
          put("isInitiator", "true");
          put("debug", "true");
        }};
        return new AppConfigurationEntry[]{
                new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)
        };
      }
    };
    Set<Principal> princ = new HashSet<>(1);
    princ.add(new KerberosPrincipal(kerberosParameter.getPrincipal()));
    Subject sub = new Subject(false, princ, new HashSet<Object>(), new HashSet<Object>());
    try {
      LoginContext lc = new LoginContext("", sub, null, config);
      lc.login();
      return lc;
    } catch (LoginException e) {
      throw new RuntimeException(e);
    }
  }

  public void logout() {
    if (loginContext != null) {
      try {
        loginContext.logout();
        loginContext = null;
      } catch (LoginException e) {
        log.warn("logout error, message:{}", e.getMessage());
      }
    }
  }

  protected HttpResponse callRestUrl(HttpUriRequest request) {
    Subject serviceSubject = null;
    if (loginContext == null) {
      loginContext = login();
    }
    serviceSubject = loginContext.getSubject();
    return Subject.doAs(serviceSubject, new PrivilegedAction<HttpResponse>() {
      HttpResponse httpResponse = null;

      @Override
      public HttpResponse run() {
        try {
          httpResponse = httpClient.execute(request);
          return httpResponse;
        } catch (IOException ioe) {
          throw new RuntimeException(ioe.getMessage());
        }
      }
    });
  }

  public static KerberosParameter makeKerberOsParameter(String clusterName, Environment environment) {
    String principal = environment.getProperty(String.format(SECURITY_PRINCIPAL, clusterName));
    String keyTable = environment.getProperty(String.format(SECURITY_KEYTABLE, clusterName));
    String krb5Conf = environment.getProperty(String.format(SECURITY_KRB5_CONF, clusterName));
    if (krb5Conf == null || keyTable == null || principal == null) {
      throw new IllegalArgumentException("can find kerberos properties for cluster:" + clusterName
              + " , please set the properties:" + String.format(SECURITY_PRINCIPAL, clusterName)
              + " , " + String.format(SECURITY_KEYTABLE, clusterName)
              + " , " + String.format(SECURITY_KRB5_CONF, clusterName));
    }
    log.info("cluster:{} kerberos is enabled,principal:{},keyTable:{},krb5Conf:{}", clusterName, principal, keyTable, krb5Conf);
    return new KerberosParameter(principal, keyTable, krb5Conf);
  }

  @Getter
  @Setter
  public static class KerberosParameter {
    private String principal;
    private String keyTab;
    private String krb5Conf;

    public KerberosParameter(String principal, String keyTab, String krb5Conf) {
      this.principal = principal;
      this.keyTab = keyTab;
      this.krb5Conf = krb5Conf;
    }
  }
}
