package com.merce.woven.cas.client.reactive.web.authentication.validation;

import com.merce.woven.cas.client.reactive.config.HttpsRestTemplateFactory;
import com.merce.woven.cas.client.reactive.util.SamlUtils;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.util.*;
import org.jasig.cas.client.validation.AbstractUrlBasedTicketValidator;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.jasig.cas.client.validation.TicketValidationException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public final class Saml11TicketValidator extends AbstractUrlBasedTicketValidator {
    public static final String AUTH_METHOD_ATTRIBUTE = "samlAuthenticationStatement::authMethod";
    private static final String SAML_REQUEST_TEMPLATE;
    private static final NamespaceContext NS_CONTEXT;
    private static final ThreadLocalXPathExpression XPATH_ASSERTION_DATE_START;
    private static final ThreadLocalXPathExpression XPATH_ASSERTION_DATE_END;
    private static final ThreadLocalXPathExpression XPATH_NAME_ID;
    private static final ThreadLocalXPathExpression XPATH_AUTH_METHOD;
    private static final ThreadLocalXPathExpression XPATH_ATTRIBUTES;
    private static final String HEX_CHARS = "0123456789abcdef";
    private long tolerance;
    private final Random random;
    private final RestTemplate restTemplate;

    public Saml11TicketValidator(final String casServerUrlPrefix) {
        super(casServerUrlPrefix);
        this.tolerance = 1000L;
        this.restTemplate = HttpsRestTemplateFactory.getInstance();
        try {
            this.random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Cannot find required SHA1PRNG algorithm");
        }
    }

    protected String getUrlSuffix() {
        return "samlValidate";
    }

    protected void populateUrlAttributeMap(final Map<String, String> urlParameters) {
        final String service = urlParameters.get("service");
        urlParameters.remove("service");
        urlParameters.remove("ticket");
        urlParameters.put("TARGET", service);
    }

    protected Assertion parseResponseFromServer(final String response) throws TicketValidationException {
        try {
            final Document document = XmlUtils.newDocument(response);
            final Date assertionValidityStart = SamlUtils
                .parseUtcDate(Saml11TicketValidator.XPATH_ASSERTION_DATE_START.evaluateAsString(document));
            final Date assertionValidityEnd = SamlUtils
                .parseUtcDate(Saml11TicketValidator.XPATH_ASSERTION_DATE_END.evaluateAsString(document));
            if (!this.isValidAssertion(assertionValidityStart, assertionValidityEnd)) {
                throw new TicketValidationException("Invalid SAML assertion");
            }
            final String nameId = Saml11TicketValidator.XPATH_NAME_ID.evaluateAsString(document);
            if (nameId == null) {
                throw new TicketValidationException("SAML assertion does not contain NameIdentifier element");
            }
            final String authMethod = Saml11TicketValidator.XPATH_AUTH_METHOD.evaluateAsString(document);
            final NodeList attributes = Saml11TicketValidator.XPATH_ATTRIBUTES.evaluateAsNodeList(document);
            final Map<String, Object> principalAttributes = new HashMap<String, Object>(attributes.getLength());
            for (int i = 0; i < attributes.getLength(); ++i) {
                final Element attribute = (Element) attributes.item(i);
                final String name = attribute.getAttribute("AttributeName");
                this.logger.trace("Processing attribute {}", (Object) name);
                final NodeList values = attribute.getElementsByTagNameNS("*", "AttributeValue");
                if (values.getLength() == 1) {
                    principalAttributes.put(name, values.item(0).getTextContent());
                } else {
                    final Collection<Object> items = new ArrayList<Object>(values.getLength());
                    for (int j = 0; j < values.getLength(); ++j) {
                        items.add(values.item(j).getTextContent());
                    }
                    principalAttributes.put(name, items);
                }
            }
            return new AssertionImpl(new AttributePrincipalImpl(nameId, principalAttributes), assertionValidityStart,
                assertionValidityEnd, new Date(),
                Collections.singletonMap("samlAuthenticationStatement::authMethod", authMethod));
        } catch (Exception e) {
            throw new TicketValidationException("Error processing SAML response", (Throwable) e);
        }
    }

    private boolean isValidAssertion(final Date notBefore, final Date notOnOrAfter) {
        if (notBefore == null || notOnOrAfter == null) {
            this.logger.debug("Assertion is not valid because it does not have bounding dates.");
            return false;
        }
        final DateTime currentTime = new DateTime(DateTimeZone.UTC);
        final Interval validityRange = new Interval(new DateTime(notBefore).minus(this.tolerance),
            new DateTime(notOnOrAfter).plus(this.tolerance));
        if (validityRange.contains(currentTime)) {
            this.logger.debug("Current time is within the interval validity.");
            return true;
        }
        if (currentTime.isBefore(validityRange.getStart())) {
            this.logger.debug("Assertion is not yet valid");
        } else {
            this.logger.debug("Assertion is expired");
        }
        return false;
    }

    protected String retrieveResponseFromServer(final URL validationUrl, final String ticket) {
        final String request = String.format(Saml11TicketValidator.SAML_REQUEST_TEMPLATE, this.generateId(),
            SamlUtils.formatForUtcTime(new Date()), ticket);
        final Charset charset = CommonUtils.isNotBlank(this.getEncoding()) ? Charset.forName(this.getEncoding())
            : IOUtils.UTF8;
        final List<Charset> charsets = new ArrayList<Charset>();
        charsets.add(charset);
        try {
            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "text/xml");
            httpHeaders.setAcceptCharset(charsets);
            httpHeaders.add("SOAPAction", "http://www.oasis-open.org/committees/security");
            final HttpEntity<String> entity = new HttpEntity<>(request, httpHeaders);
            final ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(validationUrl.toURI(), entity,
                String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new RuntimeException("IO error sending HTTP request to /samlValidate", e);
        }
    }

    public void setTolerance(final long tolerance) {
        this.tolerance = tolerance;
    }

    private String generateId() {
        final byte[] data = new byte[16];
        this.random.nextBytes(data);
        final StringBuilder id = new StringBuilder(33);
        id.append('_');
        for (int i = 0; i < data.length; ++i) {
            id.append(HEX_CHARS.charAt((data[i] & 0xF0) >> 4));
            id.append(HEX_CHARS.charAt(data[i] & 0xF));
        }
        return id.toString();
    }

    static {
        NS_CONTEXT = new MapNamespaceContext(new String[]{"soap->http://schemas.xmlsoap.org/soap/envelope/",
            "sa->urn:oasis:names:tc:SAML:1.0:assertion", "sp->urn:oasis:names:tc:SAML:1.0:protocol"});
        XPATH_ASSERTION_DATE_START = new ThreadLocalXPathExpression("//sa:Assertion/sa:Conditions/@NotBefore",
            Saml11TicketValidator.NS_CONTEXT);
        XPATH_ASSERTION_DATE_END = new ThreadLocalXPathExpression("//sa:Assertion/sa:Conditions/@NotOnOrAfter",
            Saml11TicketValidator.NS_CONTEXT);
        XPATH_NAME_ID = new ThreadLocalXPathExpression("//sa:AuthenticationStatement/sa:Subject/sa:NameIdentifier",
            Saml11TicketValidator.NS_CONTEXT);
        XPATH_AUTH_METHOD = new ThreadLocalXPathExpression("//sa:AuthenticationStatement/@AuthenticationMethod",
            Saml11TicketValidator.NS_CONTEXT);
        XPATH_ATTRIBUTES = new ThreadLocalXPathExpression("//sa:AttributeStatement/sa:Attribute",
            Saml11TicketValidator.NS_CONTEXT);
        try {
            SAML_REQUEST_TEMPLATE = IOUtils.readString(
                Saml11TicketValidator.class.getResourceAsStream("/META-INF/cas/samlRequestTemplate.xml"));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load SAML request template from classpath", e);
        }
    }
}
