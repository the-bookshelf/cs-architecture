package com.books.chapters.restfulapi.patterns.parttwo.stub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.books.chapters.restfulapi.patterns.parttwo")
public class StubServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StubServiceApplication.class, args);
	}

}
