package com.books.chapters.restfulapi.patterns.chap4.springboot.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Stock;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class InvestorService {

	private static List<Investor> investorsList = new ArrayList<>();
	private static final Logger logger = LoggerFactory.getLogger(InvestorService.class);
	private static final String CIRCUIT_BREAKER_SERVICE_URL = "http://localhost:8090/investors/welcome";
	private static final String WELCOME_URI_FALLBACK_MESG = "Welcome Investor!";

	static {
		// create some data
		Stock stocksSampleOne = new Stock("EXA", 200, 20);
		Stock stocksSampleTwo = new Stock("EXB", 100, 60);

		Stock stocksSampleThree = new Stock("EXC", 300, 200);
		Stock stocksSampleFour = new Stock("EXD", 150, 40);

		Stock stockSampleFive = new Stock("EX5", 200, 20);
		Stock stockSampleSix = new Stock("EX6", 200, 20);

		ArrayList<Stock> stocksLotOne = new ArrayList<>();
		stocksLotOne.add(stocksSampleOne);
		stocksLotOne.add(stocksSampleTwo);
		stocksLotOne.add(stockSampleFive);
		stocksLotOne.add(stockSampleSix);
		ArrayList<Stock> stocksLotTwo = new ArrayList<>();
		stocksLotTwo.add(stocksSampleThree);
		stocksLotTwo.add(stocksSampleFour);
		stocksLotTwo.add(stockSampleFive);
		stocksLotTwo.add(stockSampleSix);

		Investor investorOne = new Investor("INVR_1", "Investor ONE", "conservative investor", stocksLotOne);
		Investor investorTwo = new Investor("INVR_2", "Investor TWO", "Moderate Risk investor", stocksLotTwo);

		investorsList.add(investorOne);
		investorsList.add(investorTwo);

	}

	public List<Investor> fetchAllInvestors() {
		return investorsList;
	}

	public Investor fetchInvestorById(String investorId) {
		return investorsList.stream().filter(investors -> investorId.equalsIgnoreCase(investors.getId())).findAny()
				.orElse(null);

	}

	public List<Stock> fetchStocksByInvestorId(String investorId, int offset, int limit) {
		Investor investor = fetchInvestorById(investorId);
		return investor.getStocks().subList(getStartFrom(offset, investor), getToIndex(offset, limit, investor));
	}

	private int getToIndex(int offset, int limit, Investor investor) {
		int toIndex = offset + limit;
		return (toIndex) > investor.getStocks().size() ? investor.getStocks().size() : toIndex;
	}

	private int getStartFrom(int offset, Investor investor) {
		return (offset) >= investor.getStocks().size() ? investor.getStocks().size() : offset;
	}

	public Stock fetchSingleStockByInvestorIdAndStockSymbol(String investorId, String symbol) {
		Investor investor = fetchInvestorById(investorId);
		return investor.getStocks().stream().filter(stock -> symbol.equalsIgnoreCase(stock.getSymbol())).findAny()
				.orElse(null);
	}

	@HystrixCommand(fallbackMethod="welcomeUrlFailureFallback")
	public String circuitBreakerImplWelcome() {
		logger.info("reached circuit breaker consumer circuit breaker impl");
		RestTemplate restTemplate = new RestTemplate();
		URI circuitBreakerServiceURI = URI.create(CIRCUIT_BREAKER_SERVICE_URL);
		return restTemplate.getForObject(circuitBreakerServiceURI, String.class);
	}
	
	// fall back method for welcome page failures
	public String welcomeUrlFailureFallback(){
		logger.info("lucky we have a fallback method");
		return WELCOME_URI_FALLBACK_MESG;
	}
}