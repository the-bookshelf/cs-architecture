package com.books.chapters.restfulapi.patterns.chap4.springboot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.books.chapters.restfulapi.patterns.chap4.springboot.models.IndividualInvestorPortfolio;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Stock;

@Component
public class InvestorService {

	private static final int OPERATION_ADD = 1;
	private static final int OPERATION_DELETE = 2;
	static List<Investor> investorsList = new ArrayList<>();
	private static Map<String, IndividualInvestorPortfolio> portfoliosMap = new HashMap<>();

	private static final Logger logger = LoggerFactory.getLogger(InvestorService.class);

	private InvestorServicesFetchOperations investorServicesFetchOperations = new InvestorServicesFetchOperations();

	public Stock addNewStockToTheInvestorPortfolio(String investorId, Stock newStock) {
		if (isPreConditionSuccess(investorId, newStock) && isNewStockInsertSucess(investorId, newStock)) {
			designForIntentCascadePortfolioAdd(investorId);
			return investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
					newStock.getSymbol());
		}
		return null;
	}

	public boolean deleteStockFromTheInvestorPortfolio(String investorId, String stockTobeDeletedSymbol) {
		boolean deletedStatus = false;
		Stock stockTobeDeleted = investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
				stockTobeDeletedSymbol);
		if (stockTobeDeleted != null) {
			Investor investor = investorServicesFetchOperations.fetchInvestorById(investorId);
			deletedStatus = investor.getStocks().remove(stockTobeDeleted);
		}
		designForIntentCascadePortfolioDelete(investorId, deletedStatus);
		return deletedStatus;
	}

	public Stock updateAStockByInvestorIdAndStock(String investorId, Stock stockTobeUpdated) {
		Investor investor = investorServicesFetchOperations.fetchInvestorById(investorId);
		if (investor == null) {
			return null;
		}
		Stock currentStock = investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
				stockTobeUpdated.getSymbol());
		if (currentStock == null) {
			return null;
		}
		currentStock.setNumberOfSharesHeld(stockTobeUpdated.getNumberOfSharesHeld());
		currentStock.setTickerPrice(stockTobeUpdated.getTickerPrice());
		return currentStock;
	}

	// slight variance of updateAStockByInvestorIdAndStock for PATCH method
	// please note that spring boot provides annotations based validations for
	// JSON, however this
	// method is not using those annotations for keeping the scope simple for
	// patching examples
	public Stock updateAStockByInvestorIdAndStock(String investorId, String symbol, Stock stockTobeUpdated) {
		Investor investor = investorServicesFetchOperations.fetchInvestorById(investorId);
		if (investor == null) {
			return null;
		}
		Stock currentStock = investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
				symbol);
		if (currentStock == null) {
			return null;
		}
		if (stockTobeUpdated.getNumberOfSharesHeld() > 0) {
			currentStock.setNumberOfSharesHeld(stockTobeUpdated.getNumberOfSharesHeld());
		}
		if (stockTobeUpdated.getTickerPrice() > 0) {
			currentStock.setTickerPrice(stockTobeUpdated.getTickerPrice());
		}
		return currentStock;
	}

	public List<Stock> bulkUpdateOfStocksByInvestorId(String investorId, List<Stock> stocksTobeUpdated) {
		// get the current stocks
		Investor investor = investorServicesFetchOperations.fetchInvestorById(investorId);
		List<Stock> updatedStock = new ArrayList<>();
		if (investor != null) {
			// not the best way, however to keep it simple and focus on bulk
			// operations
			// and so continue to call updateAStockByInvestorIdAndStock as many
			// as stocks to be updated
			stocksTobeUpdated.forEach(
					stock -> updatedStock.add(updateAStockByInvestorIdAndStock(investorId, stock.getSymbol(), stock)));
		}

		return updatedStock;
	}

	private static IndividualInvestorPortfolio updateInvestorPortfolioByInvestorId(Investor investor) {
		return new IndividualInvestorPortfolio(investor.getId(), investor.getStocks().size());

	}

	private boolean isPreConditionSuccess(String investorId, Stock newStock) {
		return investorServicesFetchOperations.fetchInvestorById(investorId) != null && isUnique(investorId, newStock);
	}

	private boolean isNewStockInsertSucess(String investorId, Stock newStock) {
		return investorServicesFetchOperations.fetchInvestorById(investorId).getStocks().add(newStock);
	}

	private boolean isUnique(String investorId, Stock newStock) {
		return investorServicesFetchOperations.fetchSingleStockByInvestorIdAndStockSymbol(investorId,
				newStock.getSymbol()) == null;
	}

	private void updateIndividualInvestorPortfolio(String investorId, int operation) {
		IndividualInvestorPortfolio individualInvestorPortfolio = portfoliosMap
				.get(investorServicesFetchOperations.fetchInvestorById(investorId).getId());
		if (operation == OPERATION_ADD) {
			individualInvestorPortfolio.setStocksHoldCount(individualInvestorPortfolio.getStocksHoldCount() + 1);
			logger.info("updated the portfolio for ADD stocks operation");
		} else if (operation == OPERATION_DELETE) {
			individualInvestorPortfolio.setStocksHoldCount(individualInvestorPortfolio.getStocksHoldCount() - 1);
			logger.info("updated the portfolio for Delete stocks operation");
		}

	}

	private void designForIntentCascadePortfolioAdd(String investorId) {
		updateIndividualInvestorPortfolio(investorId, OPERATION_ADD);
	}

	private void designForIntentCascadePortfolioDelete(String investorId, boolean deletedStatus) {
		if (deletedStatus) {
			updateIndividualInvestorPortfolio(investorId, OPERATION_DELETE);
		}
	}
	
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

		// Design for Intent example
		// get the portfolio of individual investor updated
		IndividualInvestorPortfolio portfolioOfInvestorOne = updateInvestorPortfolioByInvestorId(investorOne);
		IndividualInvestorPortfolio portfolioOfInvestorTwo = updateInvestorPortfolioByInvestorId(investorTwo);
		portfoliosMap.put(investorOne.getId(), portfolioOfInvestorOne);
		portfoliosMap.put(investorTwo.getId(), portfolioOfInvestorTwo);

	}
}