package com.books.chapters.restfulapi.patterns.advanced.oauth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {
	
	 @RequestMapping({"/","index"})
	  public String index() {
	    return "index";
	  }

	 @RequestMapping("/users")
	  public String users() {
	    return "users";
	  }
	 @RequestMapping("/welcome")
	  public String welcome() {
	    return "welcome";
	  }
	 @RequestMapping("/webadmin")
	  public String admin() {
	    return "admin";
	  }
	 @RequestMapping("/login")
	  public String login() {
	    return "login";
	  }
}
