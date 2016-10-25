package com.nandox.jop.core.context;

import java.util.HashMap;
import java.lang.reflect.Method;
import org.springframework.context.ApplicationContext;
import com.nandox.jop.core.processor.PageApp;

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

	private ApplicationContext springCtx;
	private HashMap<String,BeanInvoker> beans;
	private HashMap<String,PageApp> pages;
	
	/**
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public WebAppContext() {
		this.beans = new HashMap<String,BeanInvoker>();
		this.pages = new HashMap<String,PageApp>();
	}
	/**
	 * Get bean invoker (or create new if not exist) by bean name and method name
	 * @param 	  BeanName	bean name 
	 * @param 	  Method	bean method
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception BeanException if bean not found or if not have method 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BeanInvoker GetBeanInvoker(String BeanName, String Method) throws BeanException {
		try {
			Class cl = this.springCtx.getType(BeanName);
			Method m = cl.getMethod(Method);
			String key;
			if ( this.beans.containsKey((key=BeanInvoker.ComputeHash(cl, m))) ) {
				return this.beans.get(key);
			} else {
				BeanInvoker bi = new BeanInvoker(cl,m);
				this.beans.put(bi.GetHash(),bi);
				return bi;
			}
		} catch (Exception e) { throw new BeanException(); }
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
	public Object GetBeanInstance(String BeanName) {
		return this.springCtx.getBean(BeanName);
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
	public HashMap<String,PageApp> GetPagesMap() {
		return this.pages;
	}
}
