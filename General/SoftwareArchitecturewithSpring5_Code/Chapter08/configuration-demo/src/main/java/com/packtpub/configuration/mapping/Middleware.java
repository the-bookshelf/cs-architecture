package com.packtpub.configuration.mapping;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("middleware")
public class Middleware {

    private String apiKey;
    private int port;
}
