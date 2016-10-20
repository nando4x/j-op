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
	static final public String JOP_ATTR_NOTFOUND = "Attributes wrong or don't exist";  
	/** */
	static final public String JOP_ID_DOUBLE = "Block id duplicated";  
	/** */
	static final public String JOP_BEAN_SYNTAX = "Syntax error in page bean";  
	/** */
	static final public String JOP_BEAN_NOTFOUND = "Page bean not found";  
	/** */
	static final public String JOP_BEAN_TYPEWRONG = "Page bean return type wrong";  

	public static String FormatDOM(String Msg, Element Elem) {
		return Msg+"\r\n\t\t*********************************************************\r\n"+Elem.outerHtml();
	}
}
