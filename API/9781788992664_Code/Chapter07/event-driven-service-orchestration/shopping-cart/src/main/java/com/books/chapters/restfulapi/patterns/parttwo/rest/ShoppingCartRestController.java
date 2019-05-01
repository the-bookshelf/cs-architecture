package com.books.chapters.restfulapi.patterns.parttwo.rest;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.books.chapters.restfulapi.patterns.parttwo.bpm.ProcessConstants;
import com.books.chapters.restfulapi.patterns.parttwo.bpm.ProcessContext;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartRestController {

	@Autowired
	private ProcessEngine camunda;

	@RequestMapping(value = "/{scId}/submit", method = RequestMethod.POST)
	public ResponseEntity<?> placeOrderPOST(@PathVariable("scId") String scId) {
		ProcessContext context = new ProcessContext();
		submitShoppingCart(scId, context);
		if (context.getError() != null) {
			return new ResponseEntity<>(context.getError(), HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>(context.getResponse(), HttpStatus.OK);

	}

	private ProcessInstance submitShoppingCart(String scId, ProcessContext context) {
		return camunda.getRuntimeService().startProcessInstanceByKey(//
				"submitShoppingCart", //
				Variables //
						.putValue(ProcessConstants.VAR_SC_ID, scId).putValue(ProcessConstants.VAR_CTX, context));
	}
}
