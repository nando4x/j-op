package com.nandox.jop.core.processor;

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
	 * @see com.nandox.jop.core.processor.PageExpression#PageBean(WebAppContext,String)
	 */
	public SimplePageExpression(WebAppContext Context, String Code) throws DomException {
		super(Context, Code,String.class);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageExpression#Execute(com.nandox.jop.core.context.WebAppContext)
	 */
	@Override
	public String Execute(WebAppContext Context) {
		return (String)this.Invoke(Context);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.PageWriteExpression#Execute(com.nandox.jop.core.context.WebAppContext, java.lang.String)
	 */
	public String Execute(WebAppContext Context, String NativeValue) {
		// TODO Auto-generated method stub
		return (String)this.Invoke(Context,NativeValue,NativeValue);
	}
}
