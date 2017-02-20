package com.nandox.jop.core.processor.attribute;

import com.nandox.jop.core.processor.PageExpression;
import com.nandox.jop.core.processor.AbstractPageExpression;
import com.nandox.jop.core.context.WebAppContext;

/**
 * Jop Attribute abstract implementation 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    AbstractJopAttribute.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public abstract class AbstractJopAttribute<E extends AbstractPageExpression> {

	private E expression;
	protected String name;
	protected String value;
	
	public AbstractJopAttribute(WebAppContext Context ,String Name, String Value) {
		this.name = Name;
		this.value = Value;
		this.expression = this.computeExpression(Context,Value);
	}
	
	public PageExpression getExpression() {
		return expression;
	}

	protected abstract E computeExpression(WebAppContext Context, String Code);
}
