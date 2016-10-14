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
/**
 * Descrizione classe
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
	 * Descrizione
	 * @date      17 set 2016 - 17 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected void initFromServlet(ServletConfig config) throws ServletException {
		// read parameters and map them
		
		// init environment
		this.initEnv(config.getServletContext(), null);
	}
	/**
	 * Descrizione
	 * @date      17 set 2016 - 17 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected void initFromFilter(FilterConfig config) throws ServletException {
		// read parameters and map them
		
		// init environment
		this.initEnv(config.getServletContext(), null);
	}
	/**
	 * Descrizione
	 * @date      17 set 2016 - 17 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected String processPage(String pageContent) throws Exception {
		PageApp page = new PageApp(this.appCtx,pageContent);
		return page.Render(appCtx);
	}
	//
	//
	//
	private void initEnv(ServletContext ctx, HashMap<String,String> params) {
		this.appCtx = new WebAppContext();
		this.appCtx.setSpringCtx(WebApplicationContextUtils.getWebApplicationContext(ctx));
		// manage parameters
		
		// add runtime service servlet
		//ServletRegistration s = ctx.addServlet(DSP_SERVICE_SERVLET_NAME, DSP_SERVICE_SERVLET_CLASS);
		//s.addMapping(DSP_SERVICE_SERVLET_URL);
	}
}
