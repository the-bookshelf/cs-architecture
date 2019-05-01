package com.books.chapters.restfulapi.patterns.chap4.springboot.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Investor;
import com.books.chapters.restfulapi.patterns.chap4.springboot.models.Stock;

@Component
public class InvestorServicesFetchOperations {

	public List<Investor> fetchAllInvestors() {
		return InvestorService.investorsList;
	}

	public Investor fetchInvestorById(String investorId) {
		return InvestorService.investorsList.stream()
				.filter(investors -> investorId.equalsIgnoreCase(investors.getId())).findAny().orElse(null);

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
}
