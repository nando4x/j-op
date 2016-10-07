package com.nandox.jop.core.context;

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

	/**
	 * Descrizione
	 * @param 	  BeanName
	 * @param 	  Method
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public boolean IsValidBean(String BeanName, String Method) {
		try {
			Class cl = this.springCtx.getType(BeanName);
			cl.getMethod(Method);
			return true;
		} catch (Exception e) {return false;} 
	}
	/**
	 * @return the springCtx
	 */
	public ApplicationContext getSpringCtx() {
		return springCtx;
	}

	/**
	 * @param springCtx the springCtx to set
	 */
	public void setSpringCtx(ApplicationContext springCtx) {
		this.springCtx = springCtx;
	}
}
