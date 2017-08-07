package com.nandox.jop.core.processor;

import java.util.Map;

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
	 * @see com.nandox.jop.core.processor.PageExpression#PageBean(WebAppContext,String,Map<String,Class<?>>)
	 */
	public CollectionPageExpression(WebAppContext Context, String Code, Map<String,Class<?>> Vars) throws DomException {
		super(Context, Code, Vars);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.AbstractPageExpression#execute(com.nandox.jop.core.context.WebAppContext,Map<String,Object>)
	 */
	@Override
	public Object execute(WebAppContext Context, Map<String,Object> Vars) {
		java.util.List<String> l = new java.util.ArrayList<String>();
		l.add("1");
		l.add("2");
		return this.invoke(Context, Vars);
		//return l;
	}

}
