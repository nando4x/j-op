package com.nandox.jop.core.processor;

import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.context.WebAppContext;
/**
 * Attribute of page block
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
	/** Identification attribute: jop_id */
	public static final String JOP_RENDERED_ID = "jop_rendered";
	/** List of possible attributes */
	protected static final String[][] ATTR_LIST = { 
													{JOP_RENDERED_ID,BooleanPageBean.class.getCanonicalName()},
													{"class",SimplePageBean.class.getCanonicalName()}
												  };
	protected static final int ATTR_NAME = 0;
	protected static final int ATTR_CLASS = 1;
	/** */
	protected String name;
	/** */
	protected PageBean bean;
	/**
	 * Constructor: parse DOM element
	 * @param	  Context	Application context
	 * @param	  DomElement	HTML element of the block
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException if attribute name not found or syntax error
	 */	
	public BlockAttribute(WebAppContext Context, String Name, String BeanId) throws DomException {
		this.name = Name;
		for ( int ix=0; ix<ATTR_LIST.length; ix++ ) {
			if ( Name.toLowerCase().equals(ATTR_LIST[ix][ATTR_NAME].toLowerCase()) ) {
				this.parse(Context,BeanId,ix);
			}
		}
		throw new DomException(ErrorsDefine.JOP_ATTR_NOTFOUND);
	}
	//
	//
	//
	private void parse(WebAppContext context, String beanId, int listInx) throws DomException {
		try {
			this.bean = (PageBean)Class.forName(ATTR_LIST[listInx][ATTR_CLASS]).newInstance();
			return;
		} catch ( Exception e ) {
			throw new DomException(e.getMessage());
		}
	}
	
}
