package com.ttknpdev.understandgoogleloginwithwebapp.configuration.secure;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;



@Configuration
@EnableWebSecurity
public class SecurityConfig implements AuthenticationSuccessHandler {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((authenticate) ->
                        authenticate
                                .requestMatchers("/api/user").authenticated()
                                .requestMatchers("/api/user/profile").authenticated()
                                .requestMatchers("/api/hello-world").permitAll()
                                .requestMatchers("/api/login").permitAll()
                                // for my static folder
                                .requestMatchers("/resources/**", "/bootstrap/**", "/static/**", "/css/**", "/js/**").permitAll()
                )
                // we can set redirect to "/api/user/profile" if user logged in successes by AuthenticationSuccessHandler interface class
                .oauth2Login(loginConfigurer -> {
                    loginConfigurer.loginPage("/api/login"); // first if api have auth it will redirect to /api/login
                    loginConfigurer.successHandler(this::onAuthenticationSuccess); // ** if user logged in successes t will redirect to /api/user/profile
                });
                // .formLogin(withDefaults()); // or use Spring security's login template
        return httpSecurity.build();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.sendRedirect("/api/user/profile");
    }



}
