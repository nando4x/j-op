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

	public boolean IsValidBean(String BeanName, String Method) {
		try {
			this.springCtx.getType(BeanName);
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
