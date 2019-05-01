package com.books.chapters.restfulapi.patterns.chap4.springboot.models.errorsandexceptions;

public class CustomHeaderNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public CustomHeaderNotFoundException(String exception) {
		super(exception);
	}
}
