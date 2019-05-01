package com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceRequest;

public class AmqpSubscriber {
	private static final Logger LOG = LoggerFactory.getLogger(AmqpSubscriber.class);

	@Autowired
	EventHandler eventHandler;

	@RabbitListener(queues = "#{eventQueue.name}")
	public void process(ServiceRequest request) {
		LOG.trace("Subscribed event: {}", request);
		LOG.trace("Handled by: {}", eventHandler.getClass());
		eventHandler.processEvent(request);
	}
}
