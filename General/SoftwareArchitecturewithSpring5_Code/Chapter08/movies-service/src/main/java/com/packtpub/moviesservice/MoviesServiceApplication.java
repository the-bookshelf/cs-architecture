package com.packtpub.moviesservice;

import com.packtpub.moviesservice.domain.Movie;
import com.packtpub.moviesservice.persistence.MovieRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class MoviesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesServiceApplication.class, args);
    }

    @Bean
    InitializingBean populateDatabase(MovieRepository movieRepository) {
        return () -> {
            movieRepository.save(new Movie(1, "Matrix"));
            movieRepository.save(new Movie(2, "The Simpsons Movie"));
            movieRepository.save(new Movie(3, "Pirates of the Caribbean"));
        };
    }

}
