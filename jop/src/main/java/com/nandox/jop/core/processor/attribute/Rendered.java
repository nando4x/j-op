package com.nandox.jop.core.processor.attribute;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.BooleanPageExpression;

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
public class Rendered extends AbstractJopAttribute<BooleanPageExpression> implements JopAttribute {

	/**
	 * @param Context
	 * @param Name
	 * @param Value
	 */
	public Rendered(WebAppContext Context, String Name, String Value) {
		super(Context, Name, Value);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	public RETURN_ACTION preRender(WebAppContext Context, Element Dom) {
		if ( !(Boolean)this.getExpression().execute(Context) )
			return RETURN_ACTION.NOTRENDER;
		return RETURN_ACTION.CONTINUE;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	public RETURN_ACTION postRender(WebAppContext Context, Element Dom) {
		// TODO Auto-generated method stub
		return null;
	}
}
