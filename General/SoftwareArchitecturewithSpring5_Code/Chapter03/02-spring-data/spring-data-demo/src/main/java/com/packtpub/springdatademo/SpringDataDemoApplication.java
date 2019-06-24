package com.packtpub.springdatademo;

import com.packtpub.springdatademo.domain.Country;
import com.packtpub.springdatademo.domain.repositories.CountryRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataDemoApplication {

    @Bean
    InitializingBean populateDatabase(CountryRepository countryRepository) {
        return () -> {
            countryRepository.save(new Country(1, "USA"));
            countryRepository.save(new Country(2, "Ecuador"));
        };
    }

    @Bean
    CommandLineRunner queryDatabase(CountryRepository countryRepository) {
        return args ->
            countryRepository.findAll().forEach(System.out::println);
    }

    @Bean
    CommandLineRunner queryByName(CountryRepository countryRepository) {
        return args ->
            countryRepository.findByName("Ecuador").forEach(System.out::println);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringDataDemoApplication.class, args);
    }
}
