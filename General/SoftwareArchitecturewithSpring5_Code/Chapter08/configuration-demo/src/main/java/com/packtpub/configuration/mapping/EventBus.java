package com.packtpub.configuration.mapping;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("event-bus")
public class EventBus {

    private String domain;
    private String protocol;
}
