package com.nandox.jop.core.processor.attribute;

import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.RenderException;
import com.nandox.jop.core.processor.expression.PageExpression;
import com.nandox.jop.core.processor.expression.CollectionPageExpression;
import com.nandox.jop.core.processor.expression.ExpressionConverter;

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
@JopCoreAttribute(name="converter", priority=100, nested="support_data")
public class Converter extends AbstractJopAttribute<CollectionPageExpression> implements JopAttributeAction, JopAttributeRendering {
	private com.nandox.jop.core.Converter converter;
	private PageExpression dataExpr;
	private String data; 
	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String>)
	 */
	public Converter(WebAppContext Context, PageBlock Block, Element Node, String Value) throws Exception {
		super(Context, Block, Node, Value);
		Class<?> clazz = Class.forName(this.value);
		this.converter = (com.nandox.jop.core.Converter)clazz.newInstance();
		this.data = Node.attr("jop_support_data").trim();
		if ( this.data != null  && !this.data.isEmpty() ) {
			if ( Block.getParser().parseJavaExpression(this.data) != null ) {
				this.dataExpr = Block.getParser().expressionParser(Context, data, null, CollectionPageExpression.class);
			}
			
		}
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttributeAction#preAction(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element, java.util.Map)
	*/
	@Override
	public Response preAction(WebAppContext Context, Element Dom, Map<String, Object> Vars) {
		Object dt = this.data;
		if ( this.dataExpr != null )
			dt = this.dataExpr.execute(Context, Vars);
		JopAttribute.Response r = new JopAttribute.Response(RETURN_ACTION.CONVERTER,new ExpressionConverter(this.converter,dt));
		return r;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttributeAction#postAction(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element, Map<String,Object>)
	 */
	public JopAttribute.Response postAction(WebAppContext Context, Element Dom, Map<String,Object> Vars) {
		return null;
	}
	@Override
	public Response preRender(WebAppContext Context, Element Dom, Map<String, Object> Vars) throws RenderException {
		return this.preAction(Context, Dom, Vars);
	}
	@Override
	public Response postRender(WebAppContext Context, Element Dom) throws RenderException {
		return null;
	}
}
