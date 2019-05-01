package com.books.chapters.restfulapi.patterns.advanced.oauth;



import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableResourceServer
@RestController
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter
{
	  @RequestMapping("/try")
	  public String welcome() {
	    return "Welcome";
	  }
	  @RequestMapping("/userService")
	  public String userService() {
	    return "Users";
	  }
	  @RequestMapping("/admin")
	  public String admin() {
	    return "Administrator";
	  }
	  @Override
		public void configure(HttpSecurity http) throws Exception {
			http
			.authorizeRequests().antMatchers("/oauth/token", "/oauth/authorize**", "/try").permitAll();
			http.requestMatchers().antMatchers("/userService")
			.and().authorizeRequests()
			.antMatchers("/userService").access("hasRole('USER')")
			.and().requestMatchers().antMatchers("/admin")
			.and().authorizeRequests()
			.antMatchers("/admin").access("hasRole('ADMIN')");
		}   

}

