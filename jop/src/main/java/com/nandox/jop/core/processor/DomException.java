package com.nandox.jop.core.processor;

/**
 * DOM exception thrown in case of syntax error
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    DomException.java
 * 
 * @date      30 set 2016 - 30 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class DomException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * @param	  message specific message of exception 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public DomException(String message) {
		super(message);
	}

}
