package com.rabiloo.custom.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/libs/**", "/custom/**", "/js/**", "/icon/**", "/images/**", "/favicon.ico/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("Enable application security.");
        http.authorizeRequests()
                .antMatchers("/swagger-ui.html").hasRole("SYSTEM_ADMIN")
                .antMatchers("/api/v1/app/**").permitAll()
                .antMatchers("/api/v1/web/**").permitAll()
                .antMatchers("/check-healthy").permitAll()
                .anyRequest()
                .authenticated();
        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/login");
        http.authenticationProvider(authenticationProvider);
        http.httpBasic().disable();

        /*only disable csrf for api mobile app, enable csrf protection for browser*/
        http.csrf().disable();

    }

}
