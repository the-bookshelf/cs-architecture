package com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceRequest;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;

public class RpcServer {
	private static final Logger LOG = LoggerFactory.getLogger(RpcServer.class);

	@Autowired
	EventHandler eventHandler;

	@RabbitListener(queues = "#{serviceQueue.name}")
	public ServiceResponse process(ServiceRequest request) {
		LOG.trace("RPC service request: {}", request);
		LOG.trace("Handled by: {}", eventHandler.getClass());
		return eventHandler.processRequest(request);
	}
}
