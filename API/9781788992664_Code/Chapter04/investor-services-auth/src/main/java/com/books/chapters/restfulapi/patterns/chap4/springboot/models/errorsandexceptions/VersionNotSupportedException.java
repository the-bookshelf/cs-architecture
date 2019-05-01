package com.books.chapters.restfulapi.patterns.chap4.springboot.models.errorsandexceptions;

public class VersionNotSupportedException extends Exception {
	private static final long serialVersionUID = 1L;

	public VersionNotSupportedException(String exception)
	{
		super(exception);
	}
}
