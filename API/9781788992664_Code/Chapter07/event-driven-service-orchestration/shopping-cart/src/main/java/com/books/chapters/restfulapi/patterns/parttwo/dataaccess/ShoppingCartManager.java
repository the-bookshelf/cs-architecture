package com.books.chapters.restfulapi.patterns.parttwo.dataaccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.books.chapters.restfulapi.patterns.parttwo.bpm.ProcessConstants;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.BusinessEntity;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ErrorMessage;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;

@Component
public class ShoppingCartManager {
	@Autowired
	BusinessEntityRepository repository;

	public ServiceResponse getShoppingCart(String id) {
		BusinessEntity sc = BusinessEntityTranslator
				.fromJpa(repository.findById(id).orElseGet(() -> generateShoppingCart(id)));
		return validateShoppingCart(sc);
	}

	public ServiceResponse validateShoppingCart(BusinessEntity shoppingCart) {
		boolean isInvalidSC = false;
		if (ProcessConstants.SC_STATUS_CLOSED.equalsIgnoreCase(shoppingCart.getStatus())) {
			isInvalidSC = true;
		}
		return generateResponse(shoppingCart, isInvalidSC);
	}

	public ServiceResponse updateShoppingCart(BusinessEntity shoppingCart) {
		repository.save(BusinessEntityTranslator.toJpa(shoppingCart));
		return generateResponse(shoppingCart, false);
	}

	public ServiceResponse closeShoppingCart(BusinessEntity shoppingCart) {
		boolean isInvalidSC = "invalid-ShoppingCart".equalsIgnoreCase(shoppingCart.getId());
		if (!isInvalidSC) {
			shoppingCart.setStatus(ProcessConstants.SC_STATUS_CLOSED);
			repository.save(BusinessEntityTranslator.toJpa(shoppingCart));
		}
		return generateResponse(shoppingCart, isInvalidSC);
	}

	private ServiceResponse generateResponse(BusinessEntity sc, boolean isInvalid) {
		List<BusinessEntity> items = new ArrayList<>();
		items.add(sc);
		if (isInvalid) {
			return new ServiceResponse().withCreatedBy("DataAccessService").withCreatedDate(new Date())
					.withStatusCode(Response.Status.FORBIDDEN.toString()).withRelatedRequest(sc.getId())
					.withErrorMessage(new ErrorMessage().withCode("ERR_INVALID_SHOPPINGCART")
							.withMessage("Invalid Shopping Cart.")
							.withDetails("Shopping cart is invalid. Please contact the system administrator."))
					.withItems(items);
		}
		return new ServiceResponse().withId(UUID.randomUUID().toString()).withCreatedBy("DataAccessService")
				.withCreatedDate(new Date()).withStatusCode(Response.Status.OK.toString())
				.withRelatedRequest(sc.getId()).withItems(items);
	}

	private BusinessEntityJpa generateShoppingCart(String id) {
		BusinessEntityJpa sc = new BusinessEntityJpa().withId(id).withName("MySchoppingCart_" + id)
				.withStatus(ProcessConstants.SC_STATUS_OPEN).withEntityType(ProcessConstants.ENTITY_TYPE_SHOPPINGCART)
				.withEntitySpecification("consumerSC");
		BusinessEntityJpa addr = new BusinessEntityJpa().withId(UUID.randomUUID().toString())
				.withName("MyShippingAddress_" + id).withEntityType(ProcessConstants.ENTITY_TYPE_LOCATION)
				.withEntitySpecification("shippingAddr");
		BusinessEntityJpa payment = new BusinessEntityJpa().withId(UUID.randomUUID().toString())
				.withName("MyPayment_" + id).withEntityType(ProcessConstants.ENTITY_TYPE_PAYMENT)
				.withEntitySpecification("creditCartPayment");
		BusinessEntityJpa product = new BusinessEntityJpa().withId(UUID.randomUUID().toString())
				.withName("MyProduct_" + id).withEntityType(ProcessConstants.ENTITY_TYPE_PRODUCT)
				.withEntitySpecification("iphoneX_Gold_128G");
		BusinessEntityJpa product2 = new BusinessEntityJpa().withId(UUID.randomUUID().toString())
				.withName("MyProduct_" + id).withEntityType(ProcessConstants.ENTITY_TYPE_PRODUCT)
				.withEntitySpecification("iphoneX_Case");
		sc.getRelatedEntities().add(addr);
		sc.getRelatedEntities().add(payment);
		sc.getRelatedEntities().add(product);
		sc.getRelatedEntities().add(product2);
		repository.save(sc);
		repository.save(addr);
		repository.save(payment);
		repository.save(product);
		repository.save(product2);
		return sc;
	}

}
