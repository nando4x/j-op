package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    CollectionPageExpression.java
 * 
 * @date      07 mar 2017 - 07 mar 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class CollectionPageExpression extends AbstractPageExpression<Object> {

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#PageBean(WebAppContext,String)
	 */
	public CollectionPageExpression(WebAppContext Context, String Code) throws DomException {
		super(Context, Code);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.AbstractPageExpression#execute(com.nandox.jop.core.context.WebAppContext)
	 */
	@Override
	public Object execute(WebAppContext Context) {
		java.util.List<String> l = new java.util.ArrayList<String>();
		return l;
	}

}
