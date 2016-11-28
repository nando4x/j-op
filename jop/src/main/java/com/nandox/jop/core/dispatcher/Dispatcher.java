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
	
	protected static final String ATTR_APPLCONTEXT = "JopWebAppContext";
	protected static final String INIT_PARAM_COMPILER_DESTPATH ="jop.param.compiler.destpath";

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
		// check if page is changed, in this case or if not exist create new
		PageApp page;
		if ( (page = this.appCtx.GetPagesMap().get(PageId)) != null ) {
			if ( page.getHash() != PageContent.hashCode() )
				page = null;
		}
		if ( page == null ) { // create new
			page = new PageApp(this.appCtx,PageId,PageContent);
			this.appCtx.GetPagesMap().put(PageId, page);
		}
		// render page
		return page.Render(this.appCtx);
	}
	// Init environment: create application context and attach the spring context
	//
	//
	private void initEnv(ServletContext ctx, HashMap<String,String> params) {
		if ( (this.appCtx = (WebAppContext)ctx.getAttribute(ATTR_APPLCONTEXT)) == null )
			this.appCtx = new WebAppContext();
		this.appCtx.setSpringCtx(WebApplicationContextUtils.getWebApplicationContext(ctx));

		// manage parameters
		this.appCtx.SetCompilerPath(ctx.getInitParameter(INIT_PARAM_COMPILER_DESTPATH));
		
		// add runtime service servlet
		//ServletRegistration s = ctx.addServlet(DSP_SERVICE_SERVLET_NAME, DSP_SERVICE_SERVLET_CLASS);
		//s.addMapping(DSP_SERVICE_SERVLET_URL);
	}
}
