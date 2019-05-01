package com.books.chapters.restfulapi.patterns.parttwo.bpm;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.producer.AmqpPublisher;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;

@Component
public class SubmitErrorActivity implements JavaDelegate {
	private static final Logger LOG = LoggerFactory.getLogger(SubmitErrorActivity.class);
	public static final String SERVICE_ACTION = "notify";

	@Autowired
	AmqpPublisher amqpPubClient;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		LOG.info("execute {} - {}", ProcessConstants.SERVICE_NAME_BACKOFFICE, SERVICE_ACTION);
		ServiceResponse errorResponse = (ServiceResponse) execution.getVariable(ProcessConstants.VAR_RESPONSE);
		amqpPubClient.publishError(errorResponse, ProcessConstants.SERVICE_NAME_BACKOFFICE + "." + SERVICE_ACTION);
		ProcessContext ctx = (ProcessContext) execution.getVariable(ProcessConstants.VAR_CTX);
		ctx.setError(errorResponse.getErrorMessage());
	}
}
