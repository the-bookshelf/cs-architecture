package com.books.chapters.restfulapi.patterns.parttwo;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class ShoppingCartServiceApp {

	public static void main(String... args) {
		SpringApplication.run(ShoppingCartServiceApp.class, args);
	}
}
