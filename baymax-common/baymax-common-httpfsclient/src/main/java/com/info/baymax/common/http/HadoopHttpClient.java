package com.info.baymax.common.http;

import jodd.net.URLCoder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;

import java.io.IOException;

/**
 * create by pengchuan.chen on 2020/10/27
 */
@Slf4j
public class HadoopHttpClient extends KerberosAuthenticator {

    protected boolean kerberosEnable;

    @Getter
    @Setter
    protected String http_fs_url;

    public HadoopHttpClient() {
        super();
        this.kerberosEnable = false;
        hadoop_user_name = System.getenv("HADOOP_USER_NAME");
        if (StringUtils.isEmpty(hadoop_user_name)) {
            hadoop_user_name = DEFAULT_HADOOP_USER_NAME;
        }
    }

    public HadoopHttpClient(KerberosParameter kerberosParameter) {
        super(kerberosParameter);
        this.kerberosEnable = true;

        String principal = kerberosParameter.getPrincipal();
        if (principal.indexOf("/") > 1) {
            this.hadoop_user_name = principal.substring(0, principal.indexOf("/"));
        } else if (principal.indexOf("@") > 0) {
            this.hadoop_user_name = principal.substring(0, principal.indexOf("@"));
        }
    }

    protected void initHttpUrl(String http_fs_url) {
        if (StringUtils.isEmpty(http_fs_url)) {
            throw new IllegalArgumentException("http_fs_url can't be null or empty.");
        }
        if (http_fs_url.startsWith("http://")) {
            this.http_fs_url = http_fs_url;
        } else {
            this.http_fs_url = "http://" + http_fs_url;
        }
    }

    public String createURL(String path) {
        StringBuilder sb = new StringBuilder();
        log.info("address:{},operation path:{}", http_fs_url, path);
        sb.append(http_fs_url).append(path);
        return URLCoder.encodeHttpUrl(sb.toString());
    }

    public String getAsJson(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        HttpResponse response = this.execute(request);
        String json = new String(IOUtils.toByteArray(response.getEntity().getContent()), "UTF-8");
        request.releaseConnection();
        if (response.getStatusLine().getStatusCode() < 300) {
            return json;
        } else {
            throw new RuntimeException("status:" + response.getStatusLine() + ",error mesage:" + json);
        }
    }

    public HttpResponse get(String url) throws IOException {
        return execute(new HttpGet(url));
    }

    public HttpResponse post(String url, HttpEntity entityParam) throws IOException {
        HttpPost request = new HttpPost(url);
        if (entityParam != null) {
            request.setEntity(entityParam);
        }
        HttpResponse response = execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse put(String url, HttpEntity entityParam) throws IOException {
        HttpPut request = new HttpPut(url);
        if (entityParam != null) {
            request.setEntity(entityParam);
        }
        HttpResponse response = execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse delete(String url) throws IOException {
        HttpDelete delete = new HttpDelete(url);
        HttpResponse response = execute(delete);
        //TODO HttpDelete必须手动断开连接，不然在同一个循环里只能被连续调用两次，第三次请求会一直挂起不返回.
        delete.releaseConnection();
        return response;
    }

    protected HttpResponse execute(HttpUriRequest request) throws IOException {
        if (kerberosEnable) {
            return super.callRestUrl(request);
        } else {
            return httpClient.execute(request);
        }
    }

    @Override
    public void logout() {
        if (kerberosEnable) {
            super.logout();
            log.info("logout succeed.");
        }
    }
}
