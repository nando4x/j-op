package com.nandox.jop.core.processor.attribute;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.SimplePageExpression;

/**
 * Attribute jop_repeater implementation.<br>
 * Execute the expression and if false non render block  
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
@JopCoreAttribute(name="repeater", priority=100)
public class Repeater extends AbstractJopAttribute<SimplePageExpression> implements JopAttribute {

	/**
	 * @param Context
	 * @param Block
	 * @param Name
	 * @param Value
	 */
	public Repeater(WebAppContext Context, PageBlock Block, String Name, String Value) {
		super(Context, Block, Name, Value);
		Class<?> cl = this.getExpression().execute(Context).getClass();
		String vname = Block.getAttributeDefinition("jop_var");
		Block.addVariable(vname,cl);
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
