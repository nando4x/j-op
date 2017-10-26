package com.nandox.jop.core.context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.jsoup.nodes.Element;
import org.springframework.context.ApplicationContext;

import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.dispatcher.Dispatcher;
import com.nandox.jop.core.processor.DomException;
import com.nandox.jop.core.processor.PageApp;
import com.nandox.jop.core.processor.JopId;
import com.nandox.jop.core.processor.JopElement;
import com.nandox.jop.core.processor.expression.ExpressionCompiler;
import com.nandox.jop.widget.WidgetBlock;

/**
 * Application Context to resolve and invoke bean.<p>
 * The bean is search in spring environment
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    WebAppContext.java
 * 
 * @date      07 ott 2016 - 07 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class WebAppContext {

	protected static final String ATTR_BLOCKPVARS = "JopBlockPVars";
	private ApplicationContext springCtx;	// spring application context
	private HashMap<String,PageApp> pages;	// list of pages
	private ExpressionCompiler bcmpl;		// Expression compiler
	private ServletContext ctx;				// servlet context
	private Dispatcher dsp;					// dispatcher
	private static ThreadLocal<RequestContext> instance = new ThreadLocal<RequestContext>(); // current thread RequestContext
	private String[] widgetPkg;				// array of custom widget class packages
	private Map<String,String> initPar; 	// init parameters
	
	/**
	 * @param	  Context Servlet context 
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public WebAppContext(ServletContext Context, Dispatcher Dsp) {
		this.pages = new HashMap<String,PageApp>();
		this.bcmpl = new ExpressionCompiler();
		this.ctx = Context;
		this.dsp = Dsp;
		this.initPar = new HashMap<String,String>();
		ResourceBundle res = ResourceBundle.getBundle(this.getClass().getPackage().getName()+"."+"default_init_param");
		String keys[] = res.keySet().toArray(new String[0]);
		for ( int ix=0; ix<keys.length; ix++ ) {
			if ( Context.getInitParameter(keys[ix]) != null && !Context.getInitParameter(keys[ix]).isEmpty() )
				this.initPar.put(keys[ix], Context.getInitParameter(keys[ix]));
			else
				this.initPar.put(keys[ix], res.getString(keys[ix]));
		}
		this.widgetPkg = this.initPar.get("jop.customwidget.package").split(",");
	}
	/**
	 * Search and return bean instance by name 
	 * @param 	  BeanName	bean name 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean instance
	 */
	public Object getBeanInstance(String BeanName) {
		return this.springCtx.getBean(BeanName);
	}
	/**
	 * Search and return bean Class by name 
	 * @param 	  BeanName	bean name 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean class
	 */
	public Class<?> getBeanType(String BeanName) {
		return this.springCtx.getType(BeanName);
	}
	/**
	 * Set spring application context where search bean
	 * @param 	  springCtx spring application context
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public void setSpringCtx(ApplicationContext springCtx) {
		this.springCtx = springCtx;
	}
	/**
	 * Return application page map: map with page id and page
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  pages map
	 */
	public HashMap<String,PageApp> getPagesMap() {
		return this.pages;
	}
	/**
	 * @return the ExpressionCompiler
	 */
	public ExpressionCompiler getBeanCompiler() {
		return bcmpl;
	}
	/**
	 * Set compiler destination class path
	 * @param	  Path absolute path
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  pages map
	 */
	public void setCompilerPath(String Path) {
		this.bcmpl.setClasspath(Path);
	}
	/**
	 * Return bean monitor
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean Monitor
	 */
	public BeanMonitoring getBeanMonitor() {
		return BeanMonitoring.Utils.getInstance();
	}
	/**
	 * Return dispatcher
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Dispatcher
	 */
	public Dispatcher getDispatcher() {
		return this.dsp;
	}
	/**
	 * Read resource from file system like a string
	 * @param	  Name resource path name 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception FileNotFoundException if resource not found<br>
	 * 			  IOException if IO errors
	 * @return	  resource data
	 */
	public String getResource(String Name) throws Exception {
		String  out = "";
		String contextPath = this.ctx.getRealPath(File.separator);
		File f = new File(contextPath+File.separator+Name);
		if ( f.canRead() ) {
			InputStream i = this.ctx.getResourceAsStream(Name);
			byte buff[] = new byte[(int)f.length()]; 
			i.read(buff);
			i.close();
			// Process page content
			out = new String(buff);
		} else {
			throw new FileNotFoundException(ErrorsDefine.JOP_PAGE_NOTFOUND); 
		}
		return out;
	}
	/**
	 * Set BeanAppContext to current thread
	 * @param	  ReqCtx request context instance, null to reset current thread
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	public void setCurrentRequestContext(RequestContext ReqCtx) {
		if (ReqCtx == null) {
			instance.remove();
		}else {
			instance.set(ReqCtx);
		}
	}
	/**
	 * Widget factory
	 * @param	  Context	Application context
	 * @param	  Page		parent page
	 * @param	  DomElement	HTML element of the block
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  widget instance
	 */
	public WidgetBlock factoryWidget(WebAppContext Context, JopElement Parent, Element DomElement) throws DomException {
		String name = WidgetBlock.computeWidgetName(DomElement);
		for ( int ix=0; ix<this.widgetPkg.length; ix++ ) {
			try {
				Class<?> c = Class.forName(this.widgetPkg[ix]+"."+name);
				return (WidgetBlock)c.getDeclaredConstructor(WebAppContext.class, JopElement.class, Element.class).newInstance(Context,Parent,DomElement);
			} catch (ClassNotFoundException  e) {
			} catch (Exception e) {
				throw new DomException(e.getCause().getMessage());
			}
		}
		return new WidgetBlock(Context,Parent,DomElement);
	}
	/**
	 * Return BeanAppContext of current thread
	 * @param	  Bean Bean instance 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  Request Context instance
	 */
	static public RequestContext getCurrentRequestContext() {
		return instance.get();
	}
	/**
	 * Return init parameter value.<p>
	 * If is not defined on web.xml use the default value from properties file  
	 * @param	  Name paramenter name 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  Request Context instance
	 */
	public String getInitParameter(String Name) {
		return this.initPar.get(Name);
	}
	/**
	 * Save variables for specific block (identify by jopid) into session.<p>
	 * @param	  JopId composite Jopid
	 * @param	  ParentVars	parent variables to freeze 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	@SuppressWarnings("unchecked")
	public void freezeBlockParentVariables(String JopId, Map<String,Object> ParentVars) {
		Map<String,Map<String,Object>> vars = (Map<String,Map<String,Object>>)getCurrentRequestContext().getSession().getHttpSession().getAttribute(ATTR_BLOCKPVARS);
		if ( vars == null )
			vars = new TreeMap<String, Map<String,Object>>();
		vars.put(JopId, ParentVars);
		getCurrentRequestContext().getSession().getHttpSession().setAttribute(ATTR_BLOCKPVARS, vars);
	}
	/**
	 * Get saved variables for specific block (identify by jopid) from session.<p>
	 * @param	  JopId composite Jopid
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  Variables map [name,value]
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> getfreezedBlockParentVariables(String JopId) {
		Map<String,Map<String,Object>> vars = (Map<String,Map<String,Object>>)getCurrentRequestContext().getSession().getHttpSession().getAttribute(ATTR_BLOCKPVARS);
		if ( vars != null )
			return vars.get(JopId);
		return null;
	}
	/**
	 * Get saved variables for specific all indexed/replicated block (identify by jopid) from session.<p>
	 * @param	  JopId composite Jopid
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  Order list of map [name,value]
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getfreezedBlockIndexedParentVariables(String JopId) {
		Map<String,Map<String,Object>> vars = (Map<String,Map<String,Object>>)getCurrentRequestContext().getSession().getHttpSession().getAttribute(ATTR_BLOCKPVARS);
		List<Map<String,Object>> ret = null;
		try {
			JopId id = new JopId(JopId);
			for (String i: vars.keySet()) {
				JopId vid = new JopId(i);
				if ( vid.getPage().equals(id.getPage()) && vid.getId().equals(id.getId()) ) {
						
				}
			}
		} catch(Exception e) {}
		return ret;
	}
}