package com.nandox.jop.core.processor.attribute;

import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.SimplePageExpression;

/**
 * Attribute jop_include implementation.<br>
 * Execute the collection page expression and for every collection or array item instance result in var (defined by jop_var attribute)<br>
 * repeat the nested block executing nested expression  
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Include.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@JopCoreAttribute(name="include")
public class Include extends AbstractJopAttribute<SimplePageExpression> {
	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String, java.lang.String>)
	 */
	public Include(WebAppContext Context, PageBlock Block, Element Node, String Name, String Value) {
		super(Context, Block, Node, Name, Value);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element, java.util.Map)
	 */
	@Override
	public Response preRender(WebAppContext Context, Element Dom, Map<String, Object> Vars) {
		try {
			String file = Context.getResource(this.value);
			String ret = Context.getDispatcher().processPage((this.value.charAt(0)=='/'?this.value.substring(1):this.value), file);
			Dom.html(ret);
			return new JopAttribute.Response(RETURN_ACTION.CONTINUE);
		} catch (Exception e) {
			//TODO: manage exception
			e = null;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	@Override
	public Response postRender(WebAppContext Context, Element Dom) {
		// TODO Auto-generated method stub
		return null;
	}

}
