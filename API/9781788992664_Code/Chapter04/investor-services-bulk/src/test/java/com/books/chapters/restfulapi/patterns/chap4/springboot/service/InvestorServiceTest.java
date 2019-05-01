package com.books.chapters.restfulapi.patterns.chap4.springboot.service;

import org.junit.Test;

import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Stock;

import junit.framework.TestCase;

public class InvestorServiceTest extends TestCase {

	@Override
	protected void setUp() throws Exception {

	}

	@Test
	public void testAddNewStocksToTheInvestorPortfolio() {
		Stock actualStock = new Stock("EXe", 150, 18.5);
		getInvestorServiceForTest().addNewStockToTheInvestorPortfolio("invr_1", actualStock);
		Stock expectedStock = getInvestorServiceFetchOperationsForTest()
				.fetchSingleStockByInvestorIdAndStockSymbol("invr_1", "EXe");
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
		getInvestorServiceForTest().deleteStockFromTheInvestorPortfolio("invr_1", actualStock.getSymbol());
		Stock expectedStock = getInvestorServiceFetchOperationsForTest()
				.fetchSingleStockByInvestorIdAndStockSymbol("invr_1", "EXa");
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

	private InvestorServicesFetchOperations getInvestorServiceFetchOperationsForTest() {
		return new InvestorServicesFetchOperations();
	}

}
