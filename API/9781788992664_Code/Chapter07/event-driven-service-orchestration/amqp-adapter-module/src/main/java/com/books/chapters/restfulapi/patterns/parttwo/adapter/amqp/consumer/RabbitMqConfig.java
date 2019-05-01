package com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.consumer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqConfig {
	private static final String DEFAULT_EVENT_QUEUE = "DefaultEventQueue";
	private static final String DEFAULT_SERVICE_QUEUE = "DefaultServiceQueue";
	private List<String> serviceRoutings = new ArrayList<>();
	private List<String> eventRoutings = new ArrayList<>();

	public List<String> getServiceRoutings() {
		return this.serviceRoutings;
	}

	public List<String> getEventRoutings() {
		return this.eventRoutings;
	}

	public String getServiceQueueName() {
		return serviceRoutings.stream().findFirst().map(this::getQueue).orElse(DEFAULT_SERVICE_QUEUE);
	}

	public String getEventQueueName() {
		return eventRoutings.stream().findFirst().map(this::getQueue).orElse(DEFAULT_EVENT_QUEUE);
	}

	public String getExchangeName(String routing) {
		return routing.split("->")[0];
	}

	public String getRoutingKey(String routing) {
		return routing.split("->")[1];
	}

	public String getQueue(String routing) {
		return routing.split("->")[2];
	}
}
