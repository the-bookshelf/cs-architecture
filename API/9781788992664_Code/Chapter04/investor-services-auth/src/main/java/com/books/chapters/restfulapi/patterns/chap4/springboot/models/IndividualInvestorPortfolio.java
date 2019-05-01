package com.books.chapters.restfulapi.patterns.chap4.springboot.models;

public class IndividualInvestorPortfolio {
	private String investorId;
	private int stocksHoldCount;
	
	public IndividualInvestorPortfolio(String investorId, int stocksHoldCount){
		this.investorId = investorId;
		this.stocksHoldCount = stocksHoldCount;
	}
	
	public String getInvestorId() {
		return investorId;
	}
	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}
	public int getStocksHoldCount() {
		return stocksHoldCount;
	}
	public void setStocksHoldCount(int stocksCount) {
		stocksHoldCount = stocksCount;
	}

}
