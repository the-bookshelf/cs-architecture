package com.books.chapters.restfulapi.patterns.chap4.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Stock;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.errorsandexceptions.InvestorNotFoundException;
import com.books.chapters.restfulapi.patterns.chap4.springboot.service.InvestorService;

@EnableCircuitBreaker
@RestController
public class InvestorController {

	@Autowired
	private InvestorService investorService;


	// call the downstream service circuit-breaker-service 
	@GetMapping(value="/welcome", produces="text/plain;charset=UTF-8")
	public String welcomePageWhichProducesCharset() {
		return investorService.circuitBreakerImplWelcome();
	}
	
	
	@GetMapping("/investors/{investorId}")
	public Investor fetchInvestorById(@PathVariable String investorId) {
		Investor resultantInvestor = investorService.fetchInvestorById(investorId);
		if (resultantInvestor == null) {
			throw new InvestorNotFoundException("Investor Id-" + investorId);
		}
		return resultantInvestor;
	}

	@GetMapping(path = "/investors/{investorId}/stocks")
	public List<Stock> fetchStocksByInvestorId(@PathVariable String investorId,
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "5") int limit) {
		return investorService.fetchStocksByInvestorId(investorId, offset, limit);
	}
}
