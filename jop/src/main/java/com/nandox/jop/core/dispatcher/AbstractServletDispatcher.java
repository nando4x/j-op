package com.nandox.jop.core.dispatcher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    AbstractServletDispatcher.java
 * 
 * @date      19 set 2016 - 19 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public abstract class AbstractServletDispatcher extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected Dispatcher dsp;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig Config) throws ServletException {
		super.init(Config);
		this.dsp = new Dispatcher();
		dsp.initFromServlet(Config);
	}

	/**
	 * Complete process of page to interpreter and render it
	 * @param	  ContentPage:	html content of the page
	 * @date      19 set 2016 - 19 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException - for dom systax error
	 * @return	  rendered html
	 */
	protected String processPage(String ContentPage) throws Exception {
		return this.dsp.processPage(ContentPage);
	}
}
