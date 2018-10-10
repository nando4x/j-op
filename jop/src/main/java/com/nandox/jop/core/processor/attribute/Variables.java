package com.nandox.jop.core.processor.attribute;

import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.JopId;
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
	private ArrayList<String> vars_name;
	String jopId;
	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String>)
	 */
	public Variables(WebAppContext Context, PageBlock Block, Element Node, String Value) throws Exception {
		super(Context, Block, Node, Value);
		this.jopId = Block.getId();
		this.registerVariable(Context, Block, Node);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#setVariables(com.nandox.jop.core.context.WebAppContext, java.util.Map, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setVariables(WebAppContext Context, Map<String, Object> Vars, int Index) {
		if ( Index == 0 ) {
			String par = WebAppContext.getCurrentRequestContext().getParameterAsString("Jop.variables");
			com.google.gson.Gson json = new com.google.gson.Gson();
			Map<String,Object> lst = (Map<String,Object>)json.fromJson(par, Map.class);
			if ( lst != null ) {
				Iterator<String> i = lst.keySet().iterator();
				while ( i.hasNext() ) {
					String k = i.next();
					if ( Vars.containsKey(k) && this.vars_name.contains(k) ) {
						Vars.put(k, lst.get(k));
					}
				}
			}
			try {
				JopId jopId = new JopId(WebAppContext.getCurrentRequestContext().getParameterAsString("Jop.jopId"));
				if ( !this.jopId.equals(jopId.getId()) ) {
					for ( String key : this.vars_name )
						Vars.put(key, null);
				}
			} catch ( Exception e ) {
				// TODO: gestire eccezzione JopId
			}
		}
	}
	// Add variable to block context
	//
	//
	private void registerVariable(WebAppContext Context, PageBlock Block, Element Node) {
		Map<String,Class<?>> vars = Block.getParser().parseVariables(Node);
		this.vars_name = new ArrayList<String>(vars.keySet());
		Iterator<String> lst = vars.keySet().iterator();
		while ( lst.hasNext() ) {
			String var = lst.next();
			Block.registerVariable(var,vars.get(var));
		}
	}
}
