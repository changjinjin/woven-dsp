package com.info.baymax.security.cas.reactive.authentication;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.cas.authentication.NullStatelessTicketCache;
import org.springframework.security.cas.authentication.StatelessTicketCache;
import org.springframework.security.cas.web.authentication.ServiceAuthenticationDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Slf4j
public class CasReactiveAuthenticationManager
    implements ReactiveAuthenticationManager, InitializingBean, MessageSourceAware {
    public final String CAS_STATEFUL_IDENTIFIER = "_cas_stateful_";
    public final String CAS_STATELESS_IDENTIFIER = "_cas_stateless_";

    private final UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Setter
    private AuthenticationUserDetailsService<CasAssertionAuthenticationToken> authenticationUserDetailsService;
    @Setter
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    @Setter
    private StatelessTicketCache statelessTicketCache = new NullStatelessTicketCache();
    @Setter
    @Getter
    private String key;
    @Getter
    @Setter
    private TicketValidator ticketValidator;
    @Setter
    private ServiceProperties serviceProperties;

    public void afterPropertiesSet() {
        Assert.notNull(this.authenticationUserDetailsService, "An authenticationUserDetailsService must be set");
        Assert.notNull(this.ticketValidator, "A ticketValidator must be set");
        Assert.notNull(this.statelessTicketCache, "A statelessTicketCache must be set");
        Assert.hasText(this.key,
            "A Key is required so CasAuthenticationProvider can identify tokens it previously authenticated");
        Assert.notNull(this.messages, "A message source must be set");
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (!supports(authentication.getClass())) {
            return Mono.empty();
        }

        if (authentication instanceof CasAuthenticationToken) {
            return Mono.just(authentication);
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken
            && (!CAS_STATEFUL_IDENTIFIER.equals(authentication.getPrincipal().toString())
            && !CAS_STATELESS_IDENTIFIER.equals(authentication.getPrincipal().toString()))) {
            // UsernamePasswordAuthenticationToken not CAS related
            return Mono.empty();
        }

        // If an existing CasAuthenticationToken, just check we created it
        if (authentication instanceof CasAuthenticationToken) {
            if (this.key.hashCode() == ((CasAuthenticationToken) authentication).getKeyHash()) {
                return Mono.just(authentication);
            } else {
                throw new BadCredentialsException(messages.getMessage("CasAuthenticationProvider.incorrectKey",
                    "The presented CasAuthenticationToken does not contain the expected key"));
            }
        }

        // Ensure credentials are presented
        if ((authentication.getCredentials() == null) || "".equals(authentication.getCredentials())) {
            throw new BadCredentialsException(messages.getMessage("CasAuthenticationProvider.noServiceTicket",
                "Failed to provide a CAS service ticket to validate"));
        }

        boolean stateless = false;

        if (authentication instanceof UsernamePasswordAuthenticationToken
            && CAS_STATELESS_IDENTIFIER.equals(authentication.getPrincipal())) {
            stateless = true;
        }

        CasAuthenticationToken result = null;

        if (stateless) {
            // Try to obtain from cache
            result = statelessTicketCache.getByTicketId(authentication.getCredentials().toString());
        }

        if (result == null) {
            result = this.authenticateNow(authentication);
            result.setDetails(authentication.getDetails());
        }

        if (stateless) {
            // Add to cache
            statelessTicketCache.putTicketInCache(result);
        }

        return Mono.just(result);
    }

    private CasAuthenticationToken authenticateNow(final Authentication authentication) throws AuthenticationException {
        try {
            final Assertion assertion = this.ticketValidator.validate(authentication.getCredentials().toString(),
                getServiceUrl(authentication));
            final UserDetails userDetails = loadUserByAssertion(assertion);
            userDetailsChecker.check(userDetails);
            return new CasAuthenticationToken(this.key, userDetails, authentication.getCredentials(),
                authoritiesMapper.mapAuthorities(userDetails.getAuthorities()), userDetails, assertion);
        } catch (final TicketValidationException e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    /**
     * Gets the serviceUrl. If the {@link Authentication#getDetails()} is an instance of
     * {@link ServiceAuthenticationDetails}, then {@link ServiceAuthenticationDetails#getServiceUrl()} is used.
     * Otherwise, the {@link ServiceProperties#getService()} is used.
     *
     * @param authentication
     * @return
     */
    private String getServiceUrl(Authentication authentication) {
        String serviceUrl;
        if (authentication.getDetails() instanceof ServiceAuthenticationDetails) {
            serviceUrl = ((ServiceAuthenticationDetails) authentication.getDetails()).getServiceUrl();
        } else if (serviceProperties == null) {
            throw new IllegalStateException(
                "serviceProperties cannot be null unless Authentication.getDetails() implements ServiceAuthenticationDetails.");
        } else if (serviceProperties.getService() == null) {
            throw new IllegalStateException(
                "serviceProperties.getService() cannot be null unless Authentication.getDetails() implements ServiceAuthenticationDetails.");
        } else {
            serviceUrl = serviceProperties.getService();
        }
        if (log.isDebugEnabled()) {
            log.debug("serviceUrl = " + serviceUrl);
        }
        return serviceUrl;
    }

    /**
     * Template method for retrieving the UserDetails based on the assertion. Default is to call configured
     * userDetailsService and pass the username. Deployers can override this method and retrieve the user based on any
     * criteria they desire.
     *
     * @param assertion The CAS Assertion.
     * @return the UserDetails.
     */
    protected UserDetails loadUserByAssertion(final Assertion assertion) {
        final CasAssertionAuthenticationToken token = new CasAssertionAuthenticationToken(assertion, "");
        return this.authenticationUserDetailsService.loadUserDetails(token);
    }

    public boolean supports(final Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication))
            || (CasAuthenticationToken.class.isAssignableFrom(authentication))
            || (CasAssertionAuthenticationToken.class.isAssignableFrom(authentication));
    }

    @Override
    public void setMessageSource(final MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
