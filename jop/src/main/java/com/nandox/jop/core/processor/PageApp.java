package com.nandox.jop.core.processor;

import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    PageApp.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class PageApp {
	private Document dom;
	private HashMap blocks;
	
	/**
	 * Constructor: parse page content into DOM
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public PageApp(String ContentPage) throws DomException {
		this.dom = Jsoup.parse(ContentPage);
		this.parse();
	}
	//
	//
	//
	private void parse() throws DomException {
		// Search every jop block into dom and create it
			// check for double jop id
			// check syntax error
		// Scan block for own child and attach them  
	}
	
}
