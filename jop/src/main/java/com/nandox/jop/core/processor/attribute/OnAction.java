package com.nandox.jop.core.processor.attribute;

import java.util.Iterator;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.attribute.JopAttribute.RETURN_ACTION;
import com.nandox.jop.core.processor.expression.SimplePageExpression;

/**
 * Attribute jop_onaction implementation.<p>
 * Execute the page expression when action phase is performed  
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    OnAction.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@JopCoreAttribute(name="onaction")
public class OnAction extends AbstractJopAttribute<SimplePageExpression> {
	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String, java.lang.String>)
	 */
	public OnAction(WebAppContext Context, PageBlock Block, Element Node, String Name, String Value) throws Exception {
		super(Context, Block, Node, Name, Value);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#isActionAttribute()
	 */
	@Override
	public boolean isActionAttribute() {
		return true;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element, java.util.Map)
	 */
	@Override
	public Response preRender(WebAppContext Context, Element Dom, Map<String, Object> Vars) {
		this.getExpression().execute(Context, Vars);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	@Override
	public Response postRender(WebAppContext Context, Element Dom) {
		return null;
	}

}
