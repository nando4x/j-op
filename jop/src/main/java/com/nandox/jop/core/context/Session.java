package com.nandox.jop.core.context;

import javax.servlet.http.HttpSession;
/**
 * Implementation Session.<p> 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Session.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class Session {
	private HttpSession httpSess;	// native session
	
	/**
	 * @param	  Session	native session
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public Session (HttpSession Session) {
		this.httpSess = Session;
		this.httpSess.setAttribute("jopsession", this);
	}

	/**
	 * @return the httpSess
	 */
	public HttpSession getHttpSession() {
		return httpSess;
	}

	/**
	 * Set an attribute
	 * @param	  Name	attribute name
	 * @param	  Value	attribute value
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public void setAttribute(String Name, Object Value) {
		this.httpSess.setAttribute("jopsession_"+Name, Value);
	}
	/**
	 * Get an attribute
	 * @param	  Name	attribute name
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Attribute value
	 */
	public Object getAttribute(String Name) {
		return this.httpSess.getAttribute("jopsession_"+Name);
	}
}
