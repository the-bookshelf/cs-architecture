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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Stock;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.errorsandexceptions.InvestorNotFoundException;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.errorsandexceptions.VersionNotSupportedException;
import com.books.chapters.restfulapi.patterns.chap4.springboot.service.InvestorService;

@RestController
public class InvestorController {

	private static final String ID = "/{id}";
	@Autowired
	private InvestorService investorService;

	// URI path version implementation
	@GetMapping({ "/v1/investors", "/v1.1/investors", "/v2/investors" })
	public List<Investor> fetchAllInvestors() {
		return investorService.fetchAllInvestors();
	}

	// sample implementation of version as parameters
	@GetMapping("/investors")
	public List<Investor> fetchAllInvestorsForGivenVersionAsParameter(@RequestParam("version") String version)
			throws VersionNotSupportedException {
		return getResultsAccordingToVersion(version);
	}

	 
	// sample implementation of version as accept header x-resource-version
	@GetMapping("/investorsbycustomheaderversion")
	public List<Investor> fetchAllInvestorsForGivenVersionAsCustomHeader(
			@RequestHeader("x-resource-version") String version) throws VersionNotSupportedException {
		return getResultsAccordingToVersion(version);
	}

	// sample implementation of version as accept header
	@GetMapping(value = "/investorsbyacceptheader", headers = "Accept=application/investors-v1+json, application/investors-v1.1+json")
	public List<Investor> fetchAllInvestorsForGivenVersionAsAcceptHeader() throws VersionNotSupportedException {
		return getResultsAccordingToVersion("1.1");
	}

	// sample implementation of version as accept header
	@GetMapping(value = "/investorsbyacceptheader", headers = "Accept=application/investors-v2+json")
	public List<Investor> fetchAllInvestorsForGivenVersionAsAcceptHeaderv2() throws VersionNotSupportedException {
		String version = "2.0"; // setting up version 2.0 as getting the version
								// as v2 in accept header 
		return getResultsAccordingToVersion(version);
	}

	// please note that no specific business logic implemented for versions
	// and simple parameter implemented to show case the capability of version
	// implementation
	private List<Investor> getResultsAccordingToVersion(String version) throws VersionNotSupportedException {
		if (!(version.equals("1.1") || version.equals("1.0"))) {
			throw new VersionNotSupportedException("version " + version);
		}
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
