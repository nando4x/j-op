package com.nandox.jop.core.processor.attribute;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.DomException;
import com.nandox.jop.core.processor.expression.AbstractPageExpression;
import com.nandox.jop.core.processor.expression.PageExpression;

/**
 * Jop Attribute abstract implementation.<p>
 * Used to implements an attribute and compile own expression value 
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
	/* Attribute DOM value */
	protected String value;
	private E expression;
	/**
	 * Constructor
	 * @param	  Context	Application context
	 * @param	  Block		Page block
	 * @param	  Node		DOM node
	 * @param	  Value		Attribute value or expression
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	
	public AbstractJopAttribute(WebAppContext Context, PageBlock Block, Element Node, String Value) throws DomException {
		this.value = Value;
		if ( Value != null && Block.getParser().parseJavaExpression("java"+Value) != null )
			this.computeExpression(Context, Value, Block.getVarsDefinition());
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#getExpression
	 */
	@Override
	public PageExpression getExpression() {
		return expression;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#setVariables(com.nandox.jop.core.context.WebAppContext, java.util.Map, int)
	 */
	@Override
	public void setVariables(WebAppContext Context, Map<String, Object> Vars, int Index) {}
	/**
	 * Create attribute expression 
	 * @param	  Context	Application context
	 * @param	  Code		Attribute expression source code
	 * @param	  Vars 		list of block variables definitions [variable name, class]
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	protected void computeExpression(WebAppContext Context, String Code, Map<String,Class<?>> Vars) throws DomException {
		try {
			@SuppressWarnings("unchecked")
			Class<E> cl = (Class<E>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			this.expression = cl.getDeclaredConstructor(WebAppContext.class, String.class, Map.class).newInstance(Context,Code,Vars);
		} catch (Exception e) {
			// TODO: manage expression instance error
			throw new DomException(e.getCause().getMessage());
		}
	}
}
