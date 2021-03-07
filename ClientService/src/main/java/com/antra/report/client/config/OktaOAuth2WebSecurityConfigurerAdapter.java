package com.antra.report.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // allow antonymous access to the root page
//                .antMatchers("/").permitAll()
                // all other requests
                .anyRequest().authenticated()

                // set logout URL
                .and().logout().logoutSuccessUrl("/home")

                // enable OAuth2/OIDC
                .and().oauth2Client()
                .and().oauth2Login()
                .and().cors().and().csrf().disable();
    }
}
