package com.packtpub.configuration;

import com.packtpub.configuration.mapping.EventBus;
import com.packtpub.configuration.mapping.Middleware;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
public class ConfigurationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationDemoApplication.class, args);
	}


	@Autowired
	public void setEventBus(EventBus eventBus){
	    log.info("+++++++++++++++ EVENT BUS CONFIG  ++++++++++++++++++++++");
		log.info(eventBus.toString());
	}

	@Autowired
	public void setMiddleware(Middleware middleware){
        log.info("+++++++++++++++ MIDDLEWARE CONFIG ++++++++++++++++++++++");
		log.info(middleware.toString());
	}
}
