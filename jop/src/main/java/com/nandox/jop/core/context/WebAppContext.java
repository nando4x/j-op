package com.nandox.jop.core.context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

import com.nandox.jop.bean.BeanAppContext;
import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.dispatcher.Dispatcher;
import com.nandox.jop.core.processor.PageApp;
import com.nandox.jop.core.context.BeanAppContextImpl;

/**
 * Application Context to resolve and invoke bean.<br>
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

	private ApplicationContext springCtx;	// spring application context
	private HashMap<String,PageApp> pages;	// list of pages
	private ExpressionCompiler bcmpl;		// Expression compiler
	private ServletContext ctx;				// servlet context
	private Dispatcher dsp;					// dispatcher
	private static ThreadLocal<BeanAppContext> instance = new ThreadLocal<BeanAppContext>(); // current thread BeanAppContext
	
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
	}
	/**
	 * Search and return bean instance by name 
	 * @param 	  BeanName	bean name 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  Bean instance
	 * @exception TODO:
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
	 * @exception TODO:
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
	 * @param	  Bean Bean instance, null to reset current thread
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	public void setCurrentBeanAppContext(BeanAppContext Bean) {
		if (Bean == null) {
			((BeanAppContextImpl)instance.get()).setRecursiveCnt(((BeanAppContextImpl)instance.get()).getRecursiveCnt()-1);
			if ( ((BeanAppContextImpl)instance.get()).getRecursiveCnt() <= 0 )
				instance.remove();
		}else {
			instance.set(Bean);
			((BeanAppContextImpl)Bean).setRecursiveCnt(((BeanAppContextImpl)Bean).getRecursiveCnt()+1);
		}
	}
	/**
	 * Return BeanAppContext of current thread
	 * @param	  Bean Bean instance 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  Bean instance
	 */
	public BeanAppContext getCurrentBeanAppContext() {
		return instance.get();
	}
	/**
	 * detach BeanAppContext of current thread bat not reset thread 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	public void detachCurrentBeanAppContext() {
		((BeanAppContextImpl)instance.get()).setRecursiveCnt(((BeanAppContextImpl)instance.get()).getRecursiveCnt()-1);
	}
}