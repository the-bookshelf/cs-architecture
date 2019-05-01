
package com.books.chapters.restfulapi.patterns.parttwo.bpm;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.books.chapters.restfulapi.patterns.parttwo.dataaccess.ShoppingCartManager;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.BusinessEntity;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;


@Component
public class CloseShoppingCartActivity implements JavaDelegate {
	private static final Logger LOG = LoggerFactory.getLogger(CloseShoppingCartActivity.class);
	public static final String SERVICE_ACTION = "close";

	@Autowired
	ShoppingCartManager shoppingCartManager;

	@Override
	public void execute(DelegateExecution ctx) throws Exception {
		LOG.info("execute {} - {}", ProcessConstants.SERVICE_NAME_SHOPPINGCART, SERVICE_ACTION);
		BusinessEntity sc = (BusinessEntity) ctx.getVariable(ProcessConstants.VAR_SC);
		ServiceResponse response = shoppingCartManager.closeShoppingCart(sc);
		ProcessUtil.processResponse(ctx, response);
	}
}
