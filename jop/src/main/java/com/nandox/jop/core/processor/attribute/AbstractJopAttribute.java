package com.nandox.jop.core.processor.attribute;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.processor.PageExpression;
import com.nandox.jop.core.processor.attribute.JopAttribute.RETURN_ACTION;
import com.nandox.jop.core.processor.AbstractPageExpression;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;

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
public abstract class AbstractJopAttribute<E extends AbstractPageExpression<?>> implements JopAttribute {
	
	private E expression;
	protected String name;
	protected String value;
	/**
	 * Constructor
	 * @param	  Context	Application context
	 * @param	  Block		Page block
	 * @param	  Name		Attribute name
	 * @param	  Value		Attribute value or expression
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public AbstractJopAttribute(WebAppContext Context, PageBlock Block, String Name, String Value) {
		this.name = Name;
		this.value = Value;
		this.computeExpression(Context, Value);
	}
	/**
	 * Return expression 
	 * @param	  Dom element dom of the block
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public PageExpression getExpression() {
		return expression;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	@Override
	abstract public RETURN_ACTION preRender(WebAppContext Context, Element Dom);
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	@Override
	abstract public RETURN_ACTION postRender(WebAppContext Context, Element Dom);
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#setVariables(com.nandox.jop.core.context.WebAppContext, java.util.Map, int)
	 */
	@Override
	public void setVariables(WebAppContext Context, Map<String, Object> Vars, int Index) {
	}
	/**
	 * Create attribute expression 
	 * @param	  Context	Application context
	 * @param	  Code		Attribute expression source code
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	protected void computeExpression(WebAppContext Context, String Code) {
		try {
			@SuppressWarnings("unchecked")
			Class<E> cl = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			this.expression = cl.getDeclaredConstructor(WebAppContext.class, String.class).newInstance(Context,Code);
		} catch (Exception e) {
			// TODO: manage expression instantation error
		}
	}
}
