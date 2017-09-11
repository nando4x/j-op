package com.nandox.jop.core.processor.attribute;

import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.expression.BooleanPageExpression;

/**
 * Attribute jop_rendered implementation.<p>
 * Check the expression and if false not render block  
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Rendered.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@JopCoreAttribute(name="rendered")
public class Rendered extends AbstractJopAttribute<BooleanPageExpression> {

	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String, java.lang.String>)
	 */
	public Rendered(WebAppContext Context, PageBlock Block, Element Node, String Name, String Value) throws Exception {
		super(Context, Block, Node, Name, Value);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element, Map<String,Object>)
	 */
	public JopAttribute.Response preRender(WebAppContext Context, Element Dom, Map<String,Object> Vars) {
		if ( !(Boolean)this.getExpression().execute(Context, Vars) )
			return new JopAttribute.Response(RETURN_ACTION.NOTRENDER);
		return new JopAttribute.Response(RETURN_ACTION.CONTINUE);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	public JopAttribute.Response postRender(WebAppContext Context, Element Dom) {
		return null;
	}
}
