package com.nandox.jop.core.processor;

import java.util.Map;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Boolean returned Expression Page: used for boolean page block attributes
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BooleanPageExpression.java
 * 
 * @date      16 ott 2016 - 16 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class BooleanPageExpression extends AbstractPageExpression<Boolean> {

	public BooleanPageExpression(WebAppContext Context, String Code, Map<String,Class<?>> Vars) throws DomException {
		super(Context, Code, Vars);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.AbstractPageExpression#Execute(com.nandox.jop.core.context.WebAppContext,Map<String,Object>)
	 */
	@Override
	public Boolean execute(WebAppContext Context, Map<String,Object> Vars) {
		return (Boolean)this.invoke(Context, Vars);
	}
}
