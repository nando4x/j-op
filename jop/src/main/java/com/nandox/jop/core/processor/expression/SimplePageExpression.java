package com.nandox.jop.core.processor.expression;

import java.util.Map;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.DomException;

/**
 * Simple page expression that return a string format.<p>
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

public class SimplePageExpression extends AbstractPageExpression<Object> implements PageWriteExpression {

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
	public Object execute(WebAppContext Context, Map<String,Object> Vars) {
		return (Object)this.invoke(Context, Vars);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageWriteExpression#Execute(com.nandox.jop.core.context.WebAppContext, java.lang.Object, java.lang.String, Map<String,Object>)
	 */
	public Object execute(WebAppContext Context, Object Value, String NativeValue, Map<String,Object> Vars) {
		return (Object)this.invoke(Context,Value,NativeValue,Vars);
	}
}
