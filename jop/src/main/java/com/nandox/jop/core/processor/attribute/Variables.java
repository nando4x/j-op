package com.nandox.jop.core.processor.attribute;

import java.util.Collection;
import java.util.Map;
import java.util.Iterator;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.attribute.Repeater.Status;
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
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#setVariables(com.nandox.jop.core.context.WebAppContext, java.util.Map, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setVariables(WebAppContext Context, Map<String, Object> Vars, int Index) {
		if ( Index == 0 ) {
			String par = Context.getCurrentRequestContext().getParameterAsString("Jop.variables");
			com.google.gson.Gson json = new com.google.gson.Gson();
			Map<String,Object> lst = (Map<String,Object>)json.fromJson(par, Map.class);
			Iterator<String> i = lst.keySet().iterator();
			while ( i.hasNext() ) {
				String k = i.next();
				if ( Vars.containsKey("") ) {
					Vars.put(k, lst.get(k));
				}
			}
		}
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
