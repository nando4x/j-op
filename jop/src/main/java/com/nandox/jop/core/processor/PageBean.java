package com.nandox.jop.core.processor;

/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    PageBean.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class PageBean {
	/** */
	protected String beanId;

	/**
	 * Costruttore
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	
	public PageBean(String beanId) {
		this.beanId = beanId;
	}
	//
	//
	//
	private void checkSysntax() {
		int inx_st = this.beanId.indexOf("{");
		int inx_end = this.beanId.indexOf("}");
		if ( inx_st >=0 && inx_end > inx_st ) {
			
		}
	}
}
