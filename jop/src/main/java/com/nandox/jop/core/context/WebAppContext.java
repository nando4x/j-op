package com.nandox.jop.core.context;

import java.util.HashMap;
import java.lang.reflect.Method;
import org.springframework.context.ApplicationContext;
/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ApplicationContext.java
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
	/**
	 * Costruttore
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	
	public WebAppContext() {
		this.beans = new HashMap<String,BeanInvoker>();
	}
	/**
	 * Descrizione
	 * @param 	  BeanName
	 * @param 	  Method
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
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
	 * @param springCtx the springCtx to set
	 */
	public void setSpringCtx(ApplicationContext springCtx) {
		this.springCtx = springCtx;
	}
}
