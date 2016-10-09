package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;
/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BlockAttribute.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class BlockAttribute {
	/** */
	protected String name;
	/** */
	protected PageBean bean;
	/**
	 * 
	 */
	public BlockAttribute(WebAppContext Context, String Name, String BeanId) throws DomException {
		this.name = Name;
		this.bean = new PageBean(Context,BeanId);
	}
	
	
}
