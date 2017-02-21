package com.nandox.jop.core.processor.attribute;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
		this.computeExpression(Context, Value);
	}
	
	public PageExpression getExpression() {
		return expression;
	}

	protected void computeExpression(WebAppContext Context, String Code) {
		try {
			Class<E> cl = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			this.expression = cl.getDeclaredConstructor(WebAppContext.class, String.class).newInstance(Context,Code);
		} catch (Exception e) {
			// TODO: manage expression instantation error
		}
	}
}
