package com.orange.devacademy.moviesservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.devacademy.moviesservice.controller.model.RestErrorCode;
import com.orange.devacademy.moviesservice.controller.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;

import static com.orange.devacademy.moviesservice.configuration.ApiConstants.API_V1;


@EnableWebSecurity
@Slf4j
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String API_ENDPOINT =  API_V1 + "/movies";
    private static final String[] ENDPOINTS_NOAUTH = {
            API_V1 + "/status",
            API_V1 + "/version",
            API_V1 + "/doc",
            "/register",
            "/login",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security"};

    private static final String[] IGNORE_RESOURCES = {"**/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin();

        http.authorizeRequests()
                .antMatchers(ENDPOINTS_NOAUTH)
                .permitAll();

        http.authorizeRequests()
                .antMatchers(API_ENDPOINT).authenticated();
        http.httpBasic().authenticationEntryPoint(delegateAuthenticationEntryPoint());
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(IGNORE_RESOURCES);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint delegateAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            final ErrorResponse errorResponse = unauthorizedResponse(request, authException);
            response.setContentType("application/json");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        };
    }


    private ErrorResponse unauthorizedResponse(final HttpServletRequest request, final AuthenticationException authException) {
        final boolean hasAuthorizationHeader = requestHasAuthorizationHeader(request);
        final RestErrorCode restErrorCode = hasAuthorizationHeader ? RestErrorCode.INVALID_CREDENTIALS : RestErrorCode.MISSING_CREDENTIALS;
        return new ErrorResponse(restErrorCode.getCode(), authException.getMessage());
    }

    private boolean requestHasAuthorizationHeader(final HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authHeader != null;
    }
}