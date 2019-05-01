
package com.books.chapters.restfulapi.patterns.parttwo.adapter.amqp.consumer;

import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceRequest;
import com.books.chapters.restfulapi.patterns.parttwo.adapter.entity.ServiceResponse;


public interface EventHandler {
	public ServiceResponse processRequest(ServiceRequest request);

	public void processEvent(ServiceRequest request);
}
