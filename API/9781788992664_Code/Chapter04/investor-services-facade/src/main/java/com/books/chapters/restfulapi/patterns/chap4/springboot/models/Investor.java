package com.books.chapters.restfulapi.patterns.chap4.springboot.models;

import java.util.List;

public class Investor {
	private String id;
	private String name;
	private String description;
	private List<Stock> stocks;

	public Investor(String id, String name, String description, List<Stock> stocks) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.stocks = stocks;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	@Override
	public String toString() {
		return String.format("Investor [id=%s, name=%s, description=%s, stocks=%s]", id, name, description, stocks);
	}
}
