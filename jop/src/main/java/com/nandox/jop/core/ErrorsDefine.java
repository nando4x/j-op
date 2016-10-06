package com.nandox.jop.core;

import org.jsoup.nodes.Element;
/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ErrorsDefine.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class ErrorsDefine {
	/** */
	static final public String JOP_PAGE_NOTFOUND = "Required page don't exist";  
	/** */
	static final public String JOP_ID_DOUBLE = "";  
	/** */
	static final public String JOP_BEAN_SYNTAX = "";  

	public static String FormatDOM(String Msg, Element Elem) {
		return Msg+"\r\n\t\t*********************************************************\n\r"+Elem.outerHtml();
	}
}
