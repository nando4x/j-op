package com.nandox.jop.core.processor;

import java.util.Map;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Simple page expression that return a string format
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    SimplePageExpression.java
 * 
 * @date      13 ott 2016 - 13 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class SimplePageExpression extends AbstractPageExpression<String> implements PageWriteExpression {

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#PageBean(WebAppContext,String,Map<String,Class<?>>)
	 */
	public SimplePageExpression(WebAppContext Context, String Code, Map<String,Class<?>> Vars) throws DomException {
		super(Context, Code, Vars);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#Execute(com.nandox.jop.core.context.WebAppContext)
	 */
	@Override
	public String execute(WebAppContext Context, Map<String,Object> Vars) {
		return (String)this.invoke(Context, Vars);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageWriteExpression#Execute(com.nandox.jop.core.context.WebAppContext, java.lang.String, Map<String,Object>)
	 */
	public String execute(WebAppContext Context, String NativeValue, Map<String,Object> Vars) {
		return (String)this.invoke(Context,NativeValue,NativeValue,Vars);
	}
}
