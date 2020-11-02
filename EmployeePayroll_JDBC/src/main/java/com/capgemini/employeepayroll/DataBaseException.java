package com.capgemini.employeepayroll;

public class DataBaseException extends Exception{

	public enum exceptionType {

		DRIVER_CONNECTION, DATABASE_CONNECTION, EXECUTE_QUERY
	}

	exceptionType type;

	public DataBaseException(String message, exceptionType type) {
		super(message);
		this.type = type;
	}
}
