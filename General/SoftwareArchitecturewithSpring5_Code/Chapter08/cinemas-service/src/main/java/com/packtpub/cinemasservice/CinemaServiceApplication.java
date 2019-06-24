package com.packtpub.cinemasservice;

import com.packtpub.cinemasservice.domain.Cinema;
import com.packtpub.cinemasservice.persistence.CinemaRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
public class CinemaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaServiceApplication.class, args);
    }


    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    InitializingBean populateDatabase(CinemaRepository movieRepository) {
        return () -> {
            movieRepository.save(new Cinema(1, "Alamo", null));
            movieRepository.save(new Cinema(2, "4DX", null));
        };
    }

}
