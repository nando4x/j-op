package com.nandox.jop.core.processor;

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

	public BooleanPageExpression(WebAppContext Context, String Code) throws DomException {
		super(Context, Code,Boolean.class);
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.AbstractPageExpression#Execute(com.nandox.jop.core.context.WebAppContext)
	 */
	@Override
	public Boolean Execute(WebAppContext Context) {
		return (Boolean)this.Invoke(Context);
	}
}
