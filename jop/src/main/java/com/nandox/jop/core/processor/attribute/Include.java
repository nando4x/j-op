package com.nandox.jop.core.processor.attribute;

import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageApp;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.SimplePageExpression;
/**
 * @author ee38938
 *
 */
public class Include extends AbstractJopAttribute<SimplePageExpression> {
	private PageApp page;	// page to include
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
		// TODO Auto-generated method stub
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
