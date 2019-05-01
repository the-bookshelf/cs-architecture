package com.books.chapters.restfulapi.patterns.chap4.springboot.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Stock;

public class InvestorServicesFetchOperationsTest {

	@Test
	public void testFetchAllInvestors() {
		int expectedSize = 2;
		int actualListSize = new InvestorServicesFetchOperations().fetchAllInvestors().size();
		assertEquals(expectedSize, actualListSize);
	}

	@Test
	public void testFetchInvestorById() {
		Investor actualInvestor = getInvestorServiceForTest().fetchInvestorById("invr_1");
		Investor expectedInvestor = new Investor("INVR_1", "Investor ONE", "conservative investor", getStocksSetOne());
		assertEquals(actualInvestor.getId(), expectedInvestor.getId());
		actualInvestor = getInvestorServiceForTest().fetchInvestorById("invr_2");
		expectedInvestor = new Investor("INVR_2", "Investor TWO", "Moderate Risk investor", getStocksSetTwo());
		assertEquals(expectedInvestor.getId(), actualInvestor.getId());

	}

	private InvestorServicesFetchOperations getInvestorServiceForTest() {
		return new InvestorServicesFetchOperations();
	}

	@Test
	public void testFetchStocksByInvestorId() {
		Investor expectedInvestor = getInvestorServiceForTest().fetchInvestorById("invr_1");
		String expectedStockSymbol = "EXB";
		assertEquals(4,expectedInvestor.getStocks().size());
		Stock expectedStock = expectedInvestor.getStocks().stream()
				.filter(stock -> expectedStockSymbol.equalsIgnoreCase(stock.getSymbol())).findAny().orElse(null);
		assertNotNull(expectedStock);

	}

	@Test
	public void testFetchSingleStockByInvestorIdAndStockSymbol() {
		String investorId = "Invr_1";
		String expectedSymbol = "EXA";
		String actualSymbol = (getInvestorServiceForTest()
				.fetchSingleStockByInvestorIdAndStockSymbol(investorId, expectedSymbol).getSymbol());
		assertEquals(expectedSymbol, actualSymbol);
	}

	private List<Stock> getStocksSetOne() {
		ArrayList<Stock> stocksLotOne = new ArrayList<>();
		Stock stocksSampleOne = new Stock("EXA", 200, 20);
		Stock stocksSampleTwo = new Stock("EXB", 100, 60);

		stocksLotOne.add(stocksSampleTwo);
		stocksLotOne.add(stocksSampleOne);
		return stocksLotOne;
	}

	private List<Stock> getStocksSetTwo() {
		ArrayList<Stock> stocksLotTwo = new ArrayList<>();
		Stock stocksSampleThree = new Stock("EXC", 300, 200);
		Stock stocksSampleFour = new Stock("EXD", 150, 40);

		stocksLotTwo.add(stocksSampleThree);
		stocksLotTwo.add(stocksSampleFour);
		return stocksLotTwo;
	}
}
