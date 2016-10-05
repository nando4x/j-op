package com.nandox.jop.core.processor;

/**
 * Descrizione classe
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

	/**
	 * Costruttore
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public DomException(String message) {
		super("error"+message);
	}

}
