package com.github.drinking_buddies.webservices.rest.exceptions;

import org.apache.http.ParseException;

//a REST specific exception
public class RestException extends Exception {

	public RestException(String string) {
		super(string);
	}

	public RestException(String string, Exception e) {
		super(string,e);
	}

	
}
