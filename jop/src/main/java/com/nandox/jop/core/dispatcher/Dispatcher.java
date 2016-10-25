package com.nandox.jop.core.dispatcher;

import java.util.HashMap;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageApp;
import com.nandox.jop.core.processor.ParseException;
/**
 * Real Dispatcher to process page requested
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Dispatcher.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class Dispatcher {
	public static final String DSP_SERVICE_SERVLET_NAME = "";
	public static final String DSP_SERVICE_SERVLET_URL = "";
	public static final Class<? extends Servlet> DSP_SERVICE_SERVLET_CLASS = ServletDispatcher.class;

	private WebAppContext appCtx;
	/**
	 * Init used by servlet
	 * @param	  config servlet configuration
	 * @date      17 set 2016 - 17 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception ServletException @see {@link javax.servlet.Servlet#init(ServletConfig)} 
	 */
	protected void initFromServlet(ServletConfig config) throws ServletException {
		// read parameters and map them
		
		// init environment
		this.initEnv(config.getServletContext(), null);
	}
	/**
	 * Init used by filter
	 * @param	  config filter configuration
	 * @date      17 set 2016 - 17 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception ServletException @see {@link javax.servlet.Servlet#init(ServletConfig)} 
	 */
	protected void initFromFilter(FilterConfig config) throws ServletException {
		// read parameters and map them
		
		// init environment
		this.initEnv(config.getServletContext(), null);
	}
	/**
	 * Complete process page: parse check and render
	 * @param	  PageId	page identificator
	 * @param	  Page		page content
	 * @date      17 set 2016 - 17 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception ParseException if parsing and check synstax error
	 * @return	  html rendered
	 */
	protected String processPage(String PageId, String PageContent) throws ParseException {
		PageApp page = new PageApp(this.appCtx,PageId,PageContent);
		return page.Render(appCtx);
	}
	// Init environment: create application context and attach the spring context
	//
	//
	private void initEnv(ServletContext ctx, HashMap<String,String> params) {
		if ( (this.appCtx = (WebAppContext)ctx.getAttribute("JopWebAppContext")) == null )
			this.appCtx = new WebAppContext();
		this.appCtx.setSpringCtx(WebApplicationContextUtils.getWebApplicationContext(ctx));
		// manage parameters
		
		// add runtime service servlet
		//ServletRegistration s = ctx.addServlet(DSP_SERVICE_SERVLET_NAME, DSP_SERVICE_SERVLET_CLASS);
		//s.addMapping(DSP_SERVICE_SERVLET_URL);
	}
}
