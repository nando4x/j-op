package com.nandox.jop.core.dispatcher;

import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageApp;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.ParseException;
import com.nandox.jop.core.processor.JopId;
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
		
		test.testbean t = new test.testbean();
		t.getMessage();
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
		if ( (page = this.appCtx.getPagesMap().get(PageId)) != null ) {
			if ( page.getHash() != PageContent.hashCode() )
				page = null;
		}
		if ( page == null ) { // create new
			page = new PageApp(this.appCtx,PageId,PageContent);
			this.appCtx.getPagesMap().put(PageId, page);
		}
		// render page
		return page.render(this.appCtx);
	}
	/**
	 * Return Map of query data per page id
	 * @param	  QueryData array of data like javax.servlet.http.HttpServletRequest.getParametersMap 
	 * @date      23 gen 2017 - 23 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected Map<String,Map<String,String[]>> getQueryDataByPage(Map<String,String[]> QueryData) {
		Map<String,Map<String,String[]>> map = new HashMap<String,Map<String,String[]>>();
		Iterator<String> i = QueryData.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			JopId id = new JopId(key);
				Map<String,String[]> val = map.get(id.getPage());
				if ( val == null ) {
					val = new HashMap<String,String[]>();
				}
				val.put(id.getId(), QueryData.get(key));
				map.put(id.getPage(), val);
		}
		return map;
	}
	protected Map<String,Map<String,String[]>> extractParametersByPage(Map<String,String[]> QueryData) {
		Map<String,Map<String,String[]>> map = new HashMap<String,Map<String,String[]>>();
		Iterator<String> i = QueryData.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			JopId id = new JopId(key);
				Map<String,String[]> val = map.get(id.getPage());
				if ( val == null ) {
					val = new HashMap<String,String[]>();
				}
				val.put(id.getId(), QueryData.get(key));
				map.put(id.getPage(), val);
		}
		return map;
	}
	/**
	 * Process data submit action
	 * @param	  QueryData array of data like javax.servlet.http.HttpServletRequest.getParametersMap 
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected void processPageFormAction(Map<String,Map<String,String[]>> PageData) {
		Iterator<String> i = PageData.keySet().iterator();
		while ( i.hasNext() ) {
			String pageId = i.next();
			PageApp page;
			if ( (page = this.appCtx.getPagesMap().get(pageId)) != null ) {
				page.action(this.appCtx, PageData.get(pageId));
			} else {
				//TODO: manage error page not exist
			}
			
		}
	}
	/**
	 * Process data submit action of single block
	 * @param	  JopId block identifier
	 * @param	  QueryData array of data like javax.servlet.http.HttpServletRequest.getParametersMap 
	 * @date      10 feb 2017 - 10 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void processPageBlockFormAction(JopId Id, Map<String,String[]> QueryData) {
		Map<String,Map<String,String[]>> map = this.extractParametersByPage(QueryData);
		Map<String,String[]> par = map.get(Id.getPage());
		PageApp page = this.getPageApp(Id.getPage());
		if ( page != null ) {
			PageBlock pb = page.getPageBlock(Id.getId());
			if ( pb != null)
				pb.action(this.appCtx, par);
			else {
				// TODO: block not exist
			}
		}
		// TODO: manage page not exist
	}
	/**
	 * Render and return list of page block that changed and to be refresh
	 * @param	  PageId page identifier
	 * @date      10 feb 2017 - 10 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public Map<JopId,String> renderPageBlockToBeRefresh(String PageId) {
		PageApp page = this.getPageApp(PageId);
		Map<JopId,String> lst = new HashMap<JopId,String>();
		if ( page != null ) {
			Iterator<PageBlock> blocks= page.getBlocks().values().iterator();
			while ( blocks.hasNext() ) {
				PageBlock b = blocks.next();
				if ( b.getToBeRefresh() )
					lst.put(new JopId(PageId,b.getId()), b.render(this.appCtx));
			}
		}
		return lst;
	}
	// Init environment: create application context and attach the spring context
	//
	//
	private void initEnv(ServletContext ctx, HashMap<String,String> params) {
		if ( (this.appCtx = (WebAppContext)ctx.getAttribute(ATTR_APPLCONTEXT)) == null ) {
			this.appCtx = new WebAppContext();
			ctx.setAttribute(ATTR_APPLCONTEXT, this.appCtx);
		}
		this.appCtx.setSpringCtx(WebApplicationContextUtils.getWebApplicationContext(ctx));

		// manage parameters
		this.appCtx.setCompilerPath(ctx.getInitParameter(INIT_PARAM_COMPILER_DESTPATH));
		
		// add runtime service servlet
		//ServletRegistration s = ctx.addServlet(DSP_SERVICE_SERVLET_NAME, DSP_SERVICE_SERVLET_CLASS);
		//s.addMapping(DSP_SERVICE_SERVLET_URL);
	}
	// get page
	//
	//
	private PageApp getPageApp(String id) {
		PageApp page;
		if ( (page = this.appCtx.getPagesMap().get(id)) != null ) {
			return page;
		}
		// TODO: manage page not exist
		return null;
	}
}
