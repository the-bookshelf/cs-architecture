package com.books.chapters.restfulapi.patterns.parttwo.bpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.BusinessEntity;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceRequest;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;

public class ProcessUtil {
	private static final String SC_ERROR = "SC_ERROR";

	private ProcessUtil() {
	}

	public static ServiceRequest buildServiceRequest(BusinessEntity shoppingCart, String serviceName,
			String serviceAction) {
		ServiceRequest sr = new ServiceRequest().withId(shoppingCart.getId()).withCreatedBy(serviceName)
				.withCreatedDate(new Date()).withServiceName(serviceName).withServiceAction(serviceAction);
		String entityType = getEntityTypeForService(serviceName);
		if (ProcessConstants.ENTITY_TYPE_SHOPPINGCART.equals(entityType)) {
			List<BusinessEntity> items = new ArrayList<>();
			items.add(shoppingCart);
			sr.setItems(items);
		} else {
			List<BusinessEntity> items = shoppingCart.getRelatedEntities().stream()
					.filter(e -> entityType.equals(e.getEntityType())).collect(Collectors.toList());
			sr.setItems(items);
		}
		return sr;
	}

	public static void processResponse(DelegateExecution ctx, ServiceResponse serviceResponse) throws Exception {
		ctx.setVariable(ProcessConstants.VAR_RESPONSE, serviceResponse);
		if (!Response.Status.OK.toString().equals(serviceResponse.getStatusCode())) {
			ProcessContext pctx = (ProcessContext) ctx.getVariable(ProcessConstants.VAR_CTX);
			pctx.setError(serviceResponse.getErrorMessage());
			throw new BpmnError(SC_ERROR);
		}
	}

	private static String getEntityTypeForService(String serviceName) {
		if (ProcessConstants.SERVICE_NAME_LOCATION.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_LOCATION;
		} else if (ProcessConstants.SERVICE_NAME_PAYMENT.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_PAYMENT;
		} else if (ProcessConstants.SERVICE_NAME_INVENTORY.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_PRODUCT;
		} else if (ProcessConstants.SERVICE_NAME_ORDER.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_PRODUCT;
		} else if (ProcessConstants.SERVICE_NAME_CUSTOMER.equals(serviceName)) {
			return ProcessConstants.ENTITY_TYPE_SHOPPINGCART;
		} else {
			return ProcessConstants.UNKNOWN;
		}
	}

}
