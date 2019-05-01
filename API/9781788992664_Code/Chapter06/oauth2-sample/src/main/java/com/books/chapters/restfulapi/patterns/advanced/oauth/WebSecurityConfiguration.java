package com.books.chapters.restfulapi.patterns.advanced.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@SpringBootApplication
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(WebSecurityConfiguration.class, args);
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
	       return super.authenticationManagerBean();
	}
	
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
    	
    	UserDetails user1=User.builder().username("user1").password(passwordEncoder().encode("secret!23")).
    			roles("USER").build();
    	UserDetails user2=User.builder().username("user2").password(passwordEncoder().encode("secret1@3")).
    			roles("USER").build();
    	UserDetails adminUser=User.builder().username("admin").password(passwordEncoder().encode("secret@dmin")).
    			roles("ADMIN").build();
        return new InMemoryUserDetailsManager(user1, user2, adminUser);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new  BCryptPasswordEncoder();
    }
   

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
            .authorizeRequests()
            	.antMatchers("/","/index","/welcome").permitAll()
            	.antMatchers("/users").authenticated()
            	.antMatchers("/webadmin").hasRole("ADMIN").and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout() 
                .permitAll();
    }

}
