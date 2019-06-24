package com.packtpub.bankingapp.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/index").permitAll()
                .antMatchers("/guest/**").permitAll()
                .antMatchers("/customers/**").hasAuthority("ROLE_CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").failureUrl("/login?error").successForwardUrl("/home")
                .usernameParameter("username").passwordParameter("password").permitAll()
                .and()
                .logout().logoutSuccessUrl("/logout")
                .and()
                .csrf();
        ;
    }
}
