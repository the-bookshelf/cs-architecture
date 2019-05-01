package com.books.chapters.restfulapi.patterns.chap4.springboot.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Stock;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.errorsandexceptions.InvestorNotFoundException;
import com.books.chapters.restfulapi.patterns.chap4.springboot.service.InvestorService;

@RestController
public class InvestorController {

	private static final String ID = "/{id}";
	@Autowired
	private InvestorService investorService;

	@GetMapping(value = "/welcome", produces = "text/plain;charset=UTF-8")
	public String welcomePageWhichProducesCharset() {
		return "ウェルカムインベスター (\"Welcome Investor!\" in Japanese)";
	}

	@GetMapping("/investors")
	public String welcomeUsers() {
		return "You are in right API, However please refer the API docs for specific URLs that can be accessed with your Roles";
	}

	@GetMapping("/investors/admin")
	public List<Investor> fetchAllInvestors() {
		return investorService.fetchAllInvestors();
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

	/**
	 * method example which produces both xml and json output, other methods
	 * produces only json response and for other content type it errors out 406
	 * content not allowed
	 */
	@GetMapping(path = "/investors/{investorId}/stocks/{symbol}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public Stock fetchAStockByInvestorIdAndStockId(@PathVariable String investorId, @PathVariable String symbol) {
		return investorService.fetchSingleStockByInvestorIdAndStockSymbol(investorId, symbol);
	}

	@PostMapping("/investors/{investorId}/stocks")
	public ResponseEntity<Void> addNewStockToTheInvestorPortfolio(@PathVariable String investorId,
			@RequestBody Stock newStock) {
		Stock insertedStock = investorService.addNewStockToTheInvestorPortfolio(investorId, newStock);
		if (insertedStock == null) {
			return ResponseEntity.noContent().build();
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
				.buildAndExpand(insertedStock.getSymbol()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PutMapping("/investors/{investorId}/stocks")
	public ResponseEntity<Void> updateAStockOfTheInvestorPortfolio(@PathVariable String investorId,
			@RequestBody Stock stockTobeUpdated) {
		Stock updatedStock = investorService.updateAStockByInvestorIdAndStock(investorId, stockTobeUpdated);
		if (updatedStock == null) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
				.buildAndExpand(updatedStock.getSymbol()).toUri();
		return ResponseEntity.created(location).build();
	}

	@PatchMapping("/investors/{investorId}/stocks/{symbol}")
	public ResponseEntity<Void> updateAStockOfTheInvestorPortfolio(@PathVariable String investorId,
			@PathVariable String symbol, @RequestBody Stock stockTobeUpdated) {
		Stock updatedStock = investorService.updateAStockByInvestorIdAndStock(investorId, symbol, stockTobeUpdated);
		if (updatedStock == null) {
			return ResponseEntity.noContent().build();
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(ID)
				.buildAndExpand(updatedStock.getSymbol()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/investors/{investorId}/stocks/{symbol}")
	public ResponseEntity<Void> deleteAStockFromTheInvestorPortfolio(@PathVariable String investorId,
			@PathVariable String symbol) {
		if (investorService.deleteStockFromTheInvestorPortfolio(investorId, symbol)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(null);
	}
}
