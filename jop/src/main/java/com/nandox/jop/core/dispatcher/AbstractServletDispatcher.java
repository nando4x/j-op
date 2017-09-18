package com.nandox.jop.core.dispatcher;

import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.nandox.jop.core.logging.Logger;

/**
 * Abstract Servlet Dispatcher to instance dispatcher and process page requested.<p>
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
	
	/** Logger */
	protected static final Logger LOG = Logger.Factory.getLogger(AbstractServletDispatcher.class);
	/** Request dispatcher */
	protected Dispatcher dsp;
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig Config) throws ServletException {
		super.init(Config);
		if (LOG != null && LOG.isDebugEnabled() )
			LOG.debug("start servlet initialization");
		this.dsp = new Dispatcher();
		dsp.initFromServlet(Config);
	}
	/**
	 * Complete process request: interpreter and render page with form action submit
	 * @param	  PageId	page identificator
	 * @param	  ContentPage:	html content of the page
	 * @param	  Req 		Http request 
	 * @date      19 set 2016 - 19 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException if dom systax error
	 * @return	  rendered html
	 */
	protected String processRequest(String PageId, String ContentPage, HttpServletRequest Req) throws Exception {
		try {
			this.dsp.startProcessing(Req);
			// if is submit action process query data before
			if ( !Req.getParameterMap().isEmpty() )
				this.processDataQuery(Req.getParameterMap());
			return this.processPage(PageId, ContentPage);
		} finally {
			this.dsp.endProcessing();
		}
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
	 * Process data query of submit form action
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
