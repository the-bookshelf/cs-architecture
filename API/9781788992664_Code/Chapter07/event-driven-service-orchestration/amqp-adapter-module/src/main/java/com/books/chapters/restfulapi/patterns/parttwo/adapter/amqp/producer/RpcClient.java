package com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.producer;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ErrorMessage;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceRequest;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;

public class RpcClient {
	private static final Logger LOG = LoggerFactory.getLogger(RpcClient.class);

	@Autowired
	private RabbitTemplate template;

	@Autowired
	private TopicExchange rpcExchange;

	public ServiceResponse invokeService(ServiceRequest request) {
		LOG.trace("Exchange: {} ", rpcExchange);
		LOG.trace("Service Request: {}", request);
		String routingKey = request.getServiceName() + "." + request.getServiceAction();
		ServiceResponse response = (ServiceResponse) template.convertSendAndReceive(rpcExchange.getName(), routingKey,
				request);
		LOG.trace("Service Response: {}", response);
		return Optional.ofNullable(response).orElse(generateTimedoutResponse(request));
	}

	private ServiceResponse generateTimedoutResponse(ServiceRequest request) {
		ServiceResponse response = new ServiceResponse().withId(UUID.randomUUID().toString()).withCreatedBy("System")
				.withCreatedDate(new Date())
				.withErrorMessage(new ErrorMessage().withCode("ERR_SERVICE_UNAVAIL").withMessage("Service Unavaialble")
						.withDetails("Internal Error: we are sorry, the " + request.getServiceName()
								+ " is not reachable. Please try again later."))
				.withStatusCode(Response.Status.SERVICE_UNAVAILABLE.toString()).withRelatedRequest(request.getId());
		LOG.trace("Service timeout response: {}", response);
		return response;
	}
}
