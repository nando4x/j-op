package com.nandox.jop.core.dispatcher;

import java.util.Map;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Abstract Servlet Dispatcher to instance dispatcher and process page requested
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

/**
 * @author Fernando
 *
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
	 * @param	  PageId	page identificator
	 * @param	  ContentPage:	html content of the page
	 * @date      19 set 2016 - 19 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException if dom systax error
	 * @return	  rendered html
	 */
	protected String processPage(String PageId, String ContentPage) throws Exception {
		return this.dsp.processPage(PageId, ContentPage);
	}
	/**
	 * Process data query of submit action
	 * @param	  QueryData array of data like javax.servlet.http.HttpServletRequest.getParametersMap 
	 * @date      23 gen 2017 - 23 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected void processDataQuery(Map<String,String[]> QueryData) {
		// page id from query data
		this.dsp.processPageFormAction(this.dsp.getQueryDataByPage(QueryData));
	}
}
