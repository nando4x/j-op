package com.nandox.jop.core.processor.attribute;

import java.util.Iterator;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.RenderException;
import com.nandox.jop.core.processor.expression.SimplePageExpression;

/**
 * Attribute jop_include implementation.<p>
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
@JopCoreAttribute(name="include", priority=100)
public class Include extends AbstractJopAttribute<SimplePageExpression> implements JopAttributeRendering {
	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String>)
	 */
	public Include(WebAppContext Context, PageBlock Block, Element Node, String Value) throws Exception {
		super(Context, Block, Node, null);
		this.value = Value;
		if ( Value.trim().charAt(0) != '/') { // if relative path add part after context root
			String s = WebAppContext.getCurrentRequestContext().getHttpRequest().getRequestURI();
			if ( s.indexOf('/', 1) != s.lastIndexOf('/') ) {
				int ini = s.indexOf('/', 1);
				int end = s.lastIndexOf('/')+1;
				this.value = s.substring(ini, end) + this.value;
			}
		}
		Iterator<Element> elems = Node.getElementsByTag("param").iterator();
		while (elems.hasNext() ) {
			Element el = elems.next();
			if ( el.hasAttr("name") ) {
				String name = el.attr("name");
				String val = el.nextSibling().outerHtml();
				WebAppContext.getCurrentRequestContext().addParameter(name, val);
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttributeRendering#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element, java.util.Map)
	 */
	@Override
	public Response preRender(WebAppContext Context, Element Dom, Map<String, Object> Vars) throws RenderException {
		try {
			String file = Context.getResource(this.value);
			String ret = Context.getDispatcher().processPage((this.value.charAt(0)=='/'?this.value.substring(1):this.value), file);
			Dom.html(ret);
			return new JopAttribute.Response(RETURN_ACTION.CONTINUE);
		} catch (Exception e) {
			throw new RenderException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttributeRendering#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	@Override
	public Response postRender(WebAppContext Context, Element Dom) throws RenderException {
		return null;
	}

}
