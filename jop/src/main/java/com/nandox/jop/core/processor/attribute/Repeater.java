package com.nandox.jop.core.processor.attribute;

import java.util.Collection;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.CollectionPageExpression;
import com.nandox.jop.core.processor.attribute.JopAttribute.Response;

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
public class Repeater extends AbstractJopAttribute<CollectionPageExpression> implements JopAttribute {
	private String coll_name;
	private String vname;
	private boolean is_array;
	/**
	 * @param Context
	 * @param Block
	 * @param Name
	 * @param Value
	 */
	public Repeater(WebAppContext Context, PageBlock Block, String Name, String Value) {
		super(Context, Block, Name, Value);
		this.registerVariable(Context, Block);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	public JopAttribute.Response preRender(WebAppContext Context, Element Dom) {
		int size;
		if ( this.is_array )
			size = ((Object[])this.getExpression().execute(Context)).length;
		else 
			size = ((Collection<?>)this.getExpression().execute(Context)).size();
		return new JopAttribute.Response(RETURN_ACTION.CONTINUE,size);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttribute#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	public JopAttribute.Response postRender(WebAppContext Context, Element Dom) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#setVariables(com.nandox.jop.core.context.WebAppContext, java.util.Map, int)
	 */
	@Override
	public void setVariables(WebAppContext Context, Map<String, Object> Vars, int Index) {
		if ( Vars.get(this.coll_name) == null )
			Vars.put(this.coll_name, this.getExpression().execute(Context));
		if ( this.is_array ) {
			Object v = ((Object[])Vars.get(this.coll_name))[Index];
			Vars.put(this.vname,v);
		} else {
			Collection<?> c = (Collection<?>)Vars.get(this.coll_name);
			Object v = c.toArray(new Object[0])[Index];
			Vars.put(this.vname,v);
		}
	}
	// Add variable to block context
	//
	//
	private void registerVariable(WebAppContext Context, PageBlock Block) {
		Class<?> cl = null;
		vname = Block.getAttributeDefinition("jop_var").trim();
		if ( vname.trim().startsWith("(") && vname.indexOf(")") > 0 ) {
			try {
				cl = this.getClass().getClassLoader().loadClass(vname.substring(vname.indexOf("(")+1, vname.indexOf(")")).trim());
				vname = vname.substring(vname.indexOf(")")+1).trim();
			} catch (Exception e) {
				// TODO: manage error in class detect
			}
		} else {
			Object o = this.getExpression().execute(Context);
			cl = o.getClass();
			if ( cl.isArray() ) {
				cl = cl.getComponentType();
				this.is_array = true;
			}
			else if ( Collection.class.isAssignableFrom(cl) )
				cl = ((Collection<?>)o).iterator().next().getClass();
		}
		Block.registerVariable(vname,cl);
		this.coll_name = "_iterator_"+vname;
		Block.registerVariable(this.coll_name,null);
	}
}
