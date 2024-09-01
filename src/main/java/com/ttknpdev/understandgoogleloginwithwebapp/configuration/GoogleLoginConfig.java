package com.ttknpdev.understandgoogleloginwithwebapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

@Configuration
public class GoogleLoginConfig {
    private String clientId;

    private String clientSecret;

    private String pathRedirect;

    // work for import like key classpath as spring.config.import=classpath:<Path> another case won work
    public GoogleLoginConfig(@Value("${CLIENT.ID}") String clientId, @Value("${CLIENT.SECRET.ID}") String clientSecret, @Value("${PATH.REDIRECT}") String pathRedirect) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.pathRedirect = pathRedirect;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    /*
        May can use Spring Boot Auto-configuration set all detail on application.yml, application.properties
    */
    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(pathRedirect)
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

}
