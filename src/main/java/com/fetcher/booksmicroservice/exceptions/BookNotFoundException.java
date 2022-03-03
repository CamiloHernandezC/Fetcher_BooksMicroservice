package com.fetcher.booksmicroservice.exceptions;

public class BookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookNotFoundException() {
	    super("Could not find book");
	}
}
