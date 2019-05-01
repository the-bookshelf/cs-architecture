package com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceRequest;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;

public class AmqpPublisher {
	private static final Logger LOG = LoggerFactory.getLogger(AmqpPublisher.class);

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private TopicExchange pubExchange;

	public void publishEvent(ServiceRequest request) {
		LOG.trace("Exchange: {} ", pubExchange);
		LOG.trace("Service Request: {}", request);
		String routingKey = request.getServiceName() + "." + request.getServiceAction();
		template.convertAndSend(pubExchange.getName(), routingKey, request);
	}

	public void publishError(ServiceResponse errorResponse, String routingKey) {
		LOG.trace("Exchange: {}", pubExchange);
		LOG.trace("Service Error: {}", errorResponse);
		template.convertAndSend(pubExchange.getName(), routingKey, errorResponse);
	}
}
