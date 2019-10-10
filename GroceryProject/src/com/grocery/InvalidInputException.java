package com.grocery;

public class InvalidInputException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4594236466978803075L;
	final String msg;
	InvalidInputException(String s){
		msg=s;
	}
	
	@Override
	public String toString() {
		return "InvalidInputException: "+msg;
	}

}
