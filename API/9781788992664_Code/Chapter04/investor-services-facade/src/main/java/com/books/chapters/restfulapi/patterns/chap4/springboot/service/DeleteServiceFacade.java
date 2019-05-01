package com.books.chapters.restfulapi.patterns.chap4.springboot.service;

import java.util.List;

public interface DeleteServiceFacade {
	boolean deleteAStock(String investorId, String stockTobeDeletedSymbol);
	boolean deleteStocksInBulk(String investorId, List<String> stocksSymbolsList);
}
