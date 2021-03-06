package com.nandox.jop.core;

import org.jsoup.nodes.Element;
/**
 * Represent errors definition and their messages.<p>
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
	static final public String JOP_EXPR_SYNTAX = "Syntax error in page expression";  
	/** */
	static final public String JOP_BEAN_NOTFOUND = "Bean used in page expression don't exist";  
	/** */
	static final public String JOP_ID_MALFORMED = "Jop ID malformed (must be [page path].idblock)";  

	/**
	 * Format message error to print
	 * @param	  Msg	error message
	 * @param	  Elem	related DOM element
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  formatted string
	 */
	public static String formatDOM(String Msg, Element Elem) {
		return Msg+"\r\n\t\t*********************************************************\r\n"+Elem.outerHtml();
	}
	public static String formatDOM(String Msg, Element Elem, String code) {
		String msg = "\r\n\t\t**********************  Code executed *******************\r\n"+code;
		msg += "\r\n\t\t*********************************************************\r\n";
		return Msg+msg+Elem.outerHtml();
	}
}
