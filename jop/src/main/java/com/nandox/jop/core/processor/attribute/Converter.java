package com.nandox.jop.core.processor.attribute;

import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.expression.CollectionPageExpression;

/**
 * Attribute jop_converter implementation.<p>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Converter.java
 * 
 * @date      15 ott 2017 - 15 ott 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@JopCoreAttribute(name="converter", priority=100, nested="converter_data")
public class Converter extends AbstractJopAttribute<CollectionPageExpression> implements JopAttribute {
	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String, java.lang.String>)
	 */
	public Converter(WebAppContext Context, PageBlock Block, Element Node, String Name, String Value) throws Exception {
		super(Context, Block, Node, Name, Value);
		
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element, Map<String,Object>)
	 */
	public JopAttribute.Response preRender(WebAppContext Context, Element Dom, Map<String,Object> Vars) {
		return new JopAttribute.Response(RETURN_ACTION.CONTINUE);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	public JopAttribute.Response postRender(WebAppContext Context, Element Dom) {
		return null;
	}
}
