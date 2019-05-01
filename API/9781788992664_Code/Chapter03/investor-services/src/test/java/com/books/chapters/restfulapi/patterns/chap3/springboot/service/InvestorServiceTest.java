package com.books.chapters.restfulapi.patterns.chap3.springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.books.chapters.restfulapi.patterns.chap3.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap3.springboot.models.Stock;

import junit.framework.TestCase;

public class InvestorServiceTest extends TestCase {

	@Override
	protected void setUp() throws Exception {

	}

	@Test
	public void testFetchAllInvestors() {
		int expectedSize = 2;
		int actualListSize = getInvestorServiceForTest().fetchAllInvestors().size();
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

	@Test
	public void testFetchStocksByInvestorId() {
		Investor expectedInvestor = getInvestorServiceForTest().fetchInvestorById("invr_1");
		String expectedStockSymbol = "EXB";
		assertEquals(expectedInvestor.getStocks().size(), 4);
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

	@Test
	public void testAddNewStocksToTheInvestorPortfolio() {
		Stock actualStock = new Stock("EXe", 150, 18.5);
		InvestorService localInvestorService = getInvestorServiceForTest();
		localInvestorService.addNewStockToTheInvestorPortfolio("invr_1", actualStock);
		Stock expectedStock = localInvestorService.fetchSingleStockByInvestorIdAndStockSymbol("invr_1", "EXe");
		assertEquals(expectedStock.getSymbol(), actualStock.getSymbol());
	}

	@Test
	public void testAddNewStocksToTheInvestorPortfolioFailsWhenTryInsertingDuplicate() {
		String investorId = "invr_1";
		String actualStockSymbol = "EXe";
		Stock actualStock = new Stock(actualStockSymbol, 150, 18.5);
		InvestorService localInvestorService = getInvestorServiceForTest();
		assertNotNull(localInvestorService.addNewStockToTheInvestorPortfolio(investorId, actualStock));
		assertNull(localInvestorService.addNewStockToTheInvestorPortfolio(investorId, actualStock));
	}

	@Test
	public void testDeleteAStockToTheInvestorPortfolio() {
		Stock actualStock = new Stock("EXA", 150, 18.5);
		InvestorService localInvestorService = getInvestorServiceForTest();
		localInvestorService.deleteStockFromTheInvestorPortfolio("invr_1", actualStock.getSymbol());
		Stock expectedStock = localInvestorService.fetchSingleStockByInvestorIdAndStockSymbol("invr_1", "EXa");
		assertNull(expectedStock);
	}

	@Test
	public void testUpdateAStockByInvestorId() {
		Stock expectedStock = new Stock("EXA", 150, 18.5);
		InvestorService localInvestorService = getInvestorServiceForTest();
		String investorId = "invr_1";
		Stock actualStock = localInvestorService.updateAStockByInvestorIdAndStock(investorId, expectedStock);
		assertEquals(expectedStock.getNumberOfSharesHeld(), actualStock.getNumberOfSharesHeld());
		assertEquals(expectedStock.getTickerPrice(), actualStock.getTickerPrice());
	}
	
	@Test
	public void testUpdateAStockByInvestorIdWhenInvestorIdNotFound() {
		Stock expectedStock = new Stock("EXA", 150, 18.5);
		InvestorService localInvestorService = getInvestorServiceForTest();
		String investorId = "invr_not_found";
		Stock actualStock = localInvestorService.updateAStockByInvestorIdAndStock(investorId, expectedStock);
		assertNull(actualStock);
	}
	@Test
	public void testUpdateAStockByInvestorIdWhenStockSymbolNotFound() {
		Stock expectedStock = new Stock("not_found", 150, 18.5);
		InvestorService localInvestorService = getInvestorServiceForTest();
		String investorId = "invr_1";
		Stock actualStock = localInvestorService.updateAStockByInvestorIdAndStock(investorId, expectedStock);
		assertNull(actualStock);
	}
	
	private InvestorService getInvestorServiceForTest() {
		return new InvestorService();
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
