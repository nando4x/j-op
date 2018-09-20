package com.nandox.jop.core.processor.attribute;

import java.util.Collection;
import java.util.Map;
import java.util.Iterator;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.expression.SimplePageExpression;

/**
 * Attribute jop_var implementation.<p>
 * Register the variables usable into block  
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Variables.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@JopCoreAttribute(name="var")
public class Variables extends AbstractJopAttribute<SimplePageExpression> {
	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String>)
	 */
	public Variables(WebAppContext Context, PageBlock Block, Element Node, String Value) throws Exception {
		super(Context, Block, Node, Value);
		this.registerVariable(Context, Block, Node);
	}
	// Add variable to block context
	//
	//
	private void registerVariable(WebAppContext Context, PageBlock Block, Element Node) {
		Map<String,Class<?>> vars = Block.getParser().parseVariables(Node);
		Iterator<String> lst = vars.keySet().iterator();
		while ( lst.hasNext() ) {
			String var = lst.next();
			Block.registerVariable(var,vars.get(var));
		}
	}
}
