package com.books.chapters.restfulapi.patterns.parttwo.stub;

import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.consumer.EventHandler;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ErrorMessage;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceRequest;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;

@Service
public class ServiceStubEventHandler implements EventHandler {
	private static final String INVALID_REQ = "invalid";
	private static final String ROLLBACK_ACTION = "release";
	private static final Logger LOG = LoggerFactory.getLogger(ServiceStubEventHandler.class);

	@Override
	public ServiceResponse processRequest(ServiceRequest request) {
		String serviceName = request.getServiceName();
		String serviceAction = request.getServiceAction();
		String requestId = request.getId();
		ServiceResponse response = generateResponse(request,
				requestId.equals(INVALID_REQ + "-" + serviceName) && (!ROLLBACK_ACTION.equals(serviceAction)));
		LOG.trace("RPC response {}", response);
		return response;
	}

	@Override
	public void processEvent(ServiceRequest request) {
		LOG.trace("Event is consumed by subscriber.");
	}

	private ServiceResponse generateResponse(ServiceRequest request, boolean isInvalid) {
		if (isInvalid) {
			return new ServiceResponse().withCreatedBy(request.getServiceName()).withCreatedDate(new Date())
					.withStatusCode(Response.Status.FORBIDDEN.toString()).withRelatedRequest(request.getId())
					.withErrorMessage(buildErrorMessage(request.getServiceName()));
		}
		return new ServiceResponse().withId(UUID.randomUUID().toString()).withCreatedBy(request.getServiceName())
				.withCreatedDate(new Date()).withStatusCode(Response.Status.OK.toString())
				.withRelatedRequest(request.getId());
	}

	private ErrorMessage buildErrorMessage(String serviceName) {
		if ("LocationService".equals(serviceName)) {
			return new ErrorMessage().withCode("ERR_INVALID_ADDRESS").withMessage("Invalid address.")
					.withDetails("Shipping Address is invalid, please contact the system administrator.");
		} else if ("PaymentService".equals(serviceName)) {
			return new ErrorMessage().withCode("ERR_PAYMENT_FAILURE").withMessage("Failed to collect payment.")
					.withDetails("Failed to collect payment. Please contact the system administrator.");
		} else if ("InventoryService".equals(serviceName)) {
			return new ErrorMessage().withCode("ERR_OUT_OF_STOCK").withMessage("Product out of stock.")
					.withDetails("Internal Error: Product out of stock. Please contact the system administrator.");
		} else if ("OrderService".equals(serviceName)) {
			return new ErrorMessage().withCode("ERR_ORDER_FAILURE").withMessage("Unable to process order.")
					.withDetails("Internal Error: Unable to process order, please contact the system administrator.");
		} else {
			return new ErrorMessage().withCode("ERR_BAD_REQUEST").withMessage("Invalid service request.")
					.withDetails("Invalid service request.");
		}
	}

}
