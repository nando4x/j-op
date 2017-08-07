package com.nandox.jop.core.processor.attribute;

import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.BooleanPageExpression;
import com.nandox.jop.core.processor.PageBlock;

/**
 * Attribute jop_rendered implementation.<br>
 * CHeck the expression and if false non render block  
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
@JopCoreAttribute(name="rendered")
public class Rendered extends AbstractJopAttribute<BooleanPageExpression> {

	/**
	 * @param Context
	 * @param Block
	 * @param Name
	 * @param Value
	 */
	public Rendered(WebAppContext Context, PageBlock Block, String Name, String Value) {
		super(Context, Block, Name, Value);
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
		// TODO Auto-generated method stub
		return null;
	}
}
