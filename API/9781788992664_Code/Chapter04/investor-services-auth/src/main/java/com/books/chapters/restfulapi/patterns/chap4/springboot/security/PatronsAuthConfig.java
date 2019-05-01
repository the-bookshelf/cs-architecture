package com.books.chapters.restfulapi.patterns.chap4.springboot.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class PatronsAuthConfig extends WebSecurityConfigurerAdapter {

	private static final String NO_RESTRICT_WELCOME_URI = "/welcome";
	private static final String DEFAULT_ADMIN_SECRET = "admSecret";
	private static final String DEFAULT_ADMIN_ID = "admin";
	private static final String DEFAULT_USER_ROLE = "USER";
	private static final String DEFAULT_ADMIN_ROLE = "ADMIN";
	private static final String DEFAULT_USER_SECRET = "usrSecret";
	private static final String DEFAULT_USER_ID = "user";

	@Override
	protected void configure(AuthenticationManagerBuilder authMgrBldr) throws Exception {
		authMgrBldr.inMemoryAuthentication()
				.passwordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
				.withUser(DEFAULT_USER_ID).password(DEFAULT_USER_SECRET).authorities(DEFAULT_USER_ROLE).and()
				.withUser(DEFAULT_ADMIN_ID).password(DEFAULT_ADMIN_SECRET)
				.authorities(DEFAULT_USER_ROLE, DEFAULT_ADMIN_ROLE);
	}

	// for any URL applying the security (basic authentication)
	@Override
	protected void configure(HttpSecurity httpSec) throws Exception {
		// force for credentials every time (stateless)
		httpSec.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Invalid CSRF Token 'null' was found on the request parameter '_csrf'
		// or header 'X-CSRF-TOKEN'
		httpSec.csrf().disable();

		// welcome page no authentication required
		// /investors/admin only admin should be able to access
		// other pages Users should also be able to access
        httpSec
        .csrf().disable()
        .authorizeRequests()
        .and()
        .authorizeRequests()
        .antMatchers(NO_RESTRICT_WELCOME_URI).permitAll()
        .antMatchers("/investors/admin").hasAuthority(DEFAULT_ADMIN_ROLE)
        .antMatchers("/investors/invr*/**").access("hasAuthority('"+DEFAULT_USER_ROLE+"')")
        .anyRequest().authenticated()
        .and()
        .httpBasic()
        .and()
        .logout();
	}

}
