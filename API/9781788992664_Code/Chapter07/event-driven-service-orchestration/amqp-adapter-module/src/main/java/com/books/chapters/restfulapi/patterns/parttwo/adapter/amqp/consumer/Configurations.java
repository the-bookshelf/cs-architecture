package com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile({ "amqp-consumer" })
@Configuration
public class Configurations {
	private Map<String, TopicExchange> exchanegMap = new HashMap<>();

	@Autowired
	RabbitMqConfig rabbitMqConfig;

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public Queue serviceQueue() {
		return new Queue(rabbitMqConfig.getServiceQueueName());
	}

	@Bean
	public List<TopicExchange> exchanges() {
		return Stream.concat(serviceExchanges().stream(), eventExchanges().stream()).collect(Collectors.toList());
	}

	@Bean
	public List<Binding> bindings() {
		return Stream.concat(serviceBindings().stream(), eventBindings().stream()).collect(Collectors.toList());
	}

	@Bean
	public RpcServer server() {
		return new RpcServer();
	}

	@Bean
	public Queue eventQueue() {
		return new Queue(rabbitMqConfig.getEventQueueName());
	}

	@Bean
	public AmqpSubscriber subscriber() {
		return new AmqpSubscriber();
	}

	private List<TopicExchange> serviceExchanges() {
		return rabbitMqConfig.getServiceRoutings().stream().map(rabbitMqConfig::getExchangeName).map(e -> {
			TopicExchange exchange = new TopicExchange(e);
			exchanegMap.put(e, exchange);
			return exchange;
		}).collect(Collectors.toList());
	}

	private List<TopicExchange> eventExchanges() {
		return rabbitMqConfig.getEventRoutings().stream().map(rabbitMqConfig::getExchangeName).map(e -> {
			TopicExchange exchange = new TopicExchange(e);
			exchanegMap.put(e, exchange);
			return exchange;
		}).collect(Collectors.toList());
	}

	private List<Binding> serviceBindings() {
		return rabbitMqConfig
				.getServiceRoutings().stream().map(r -> BindingBuilder.bind(serviceQueue())
						.to(exchanegMap.get(rabbitMqConfig.getExchangeName(r))).with(rabbitMqConfig.getRoutingKey(r)))
				.collect(Collectors.toList());
	}

	private List<Binding> eventBindings() {
		return rabbitMqConfig
				.getEventRoutings().stream().map(r -> BindingBuilder.bind(eventQueue())
						.to(exchanegMap.get(rabbitMqConfig.getExchangeName(r))).with(rabbitMqConfig.getRoutingKey(r)))
				.collect(Collectors.toList());
	}
}
