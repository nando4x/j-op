package com.nandox.jop.core.processor;

/**
 * Parse exception thrown in case of syntax error
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ParseException.java
 * 
 * @date      30 set 2016 - 30 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class ParseException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * @param	  message specific message of exception 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public ParseException(String message) {
		super(message);
	}

}
