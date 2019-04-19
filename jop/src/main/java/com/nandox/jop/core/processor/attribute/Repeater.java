package com.nandox.jop.core.processor.attribute;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.expression.CollectionPageExpression;

/**
 * Attribute jop_repeater implementation.<p>
 * Execute the collection page expression and for every collection or array item instance result in var (defined by jop_var attribute)<br>
 * repeat the nested block executing nested expression  
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Repeater.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@JopCoreAttribute(name="repeater", priority=100, nested="status")
public class Repeater extends AbstractJopAttribute<CollectionPageExpression> implements JopAttributeRendering {
	private String coll_name;
	private String vname;
	private String vstatus;
	private String is_array;

	/**
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute(com.nandox.jop.core.context.WebAppContext, com.nandox.jop.core.processor.PageBlock, org.jsoup.nodes.Element, java.lang.String>)
	 */
	public Repeater(WebAppContext Context, PageBlock Block, Element Node, String Value) throws Exception {
		super(Context, Block, Node, Value);
		this.registerVariable(Context, Block, Node);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttributeRendering#preRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element, Map<String,Object>)
	 */
	public JopAttribute.Response preRender(WebAppContext Context, Element Dom, Map<String,Object> Vars) {
		int size;
		Object o = this.getExpression().execute(Context,Vars);
		if ( this.is_array == null )
			this.setArrayFlag(Context, o);
		if ( "Y".equals(this.is_array) )
			size = ((Object[])o).length;
		else 
			size = ((Collection<?>)o).size();
		return new JopAttribute.Response(RETURN_ACTION.CONTINUE,size);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.JopAttributeRendering#postRender(com.nandox.jop.core.context.WebAppContext, org.jsoup.nodes.Element)
	 */
	public JopAttribute.Response postRender(WebAppContext Context, Element Dom) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.attribute.AbstractJopAttribute#setVariables(com.nandox.jop.core.context.WebAppContext, java.util.Map, int)
	 */
	@Override
	public void setVariables(WebAppContext Context, Map<String, Object> Vars, int Index) {
		if ( Vars.get(this.vstatus) == null )
			Vars.put(vstatus, new Status());
		((Status)Vars.get(vstatus)).index = Index;
		if ( Vars.get(this.coll_name) == null )
			Vars.put(this.coll_name, this.getExpression().execute(Context, Vars));
		if ( "Y".equals(this.is_array) ) {
			Object v = ((Object[])Vars.get(this.coll_name))[Index];
			Vars.put(this.vname,v);
			((Status)Vars.get(vstatus)).size = ((Object[])Vars.get(this.coll_name)).length;
		} else {
			Collection<?> c = (Collection<?>)Vars.get(this.coll_name);
			Object v = c.toArray(new Object[0])[Index];
			Vars.put(this.vname,v);
			((Status)Vars.get(vstatus)).size = c.size();
		}
	}
	// Add variable to block context
	//
	//
	private void registerVariable(WebAppContext Context, PageBlock Block, Element Node) {
		Map<String,Class<?>> vars = Block.getParser().parseVariables(Node);
		vname = vars.keySet().iterator().next();
		Class<?> cl = vars.get(vname);
		if ( cl != null )  {
			this.setArrayFlag(Context, null);
		} else {
			Object o = this.getExpression().execute(Context, null);
			if ( o != null ) {
				cl = o.getClass();
				if ( cl.isArray() )
					cl = cl.getComponentType();
				else if ( Collection.class.isAssignableFrom(cl) )
					cl = ((Collection<?>)o).iterator().next().getClass();
				this.setArrayFlag(Context, o);
			}
		}
/*		vname = Node.attr("jop_var").trim();//Block.getAttributeDefinition("jop_var").trim();
		if ( vname.trim().startsWith("(") && vname.indexOf(")") > 0 ) {
			try {
				cl = this.getClass().getClassLoader().loadClass(vname.substring(vname.indexOf("(")+1, vname.indexOf(")")).trim());
				vname = vname.substring(vname.indexOf(")")+1).trim();
			} catch (Exception e) {
				throw new RuntimeException(e.toString() + ": " + e.getMessage());
			}
			this.setArrayFlag(Context, null);
		} else {
			Object o = this.getExpression().execute(Context, null);
			if ( o != null ) {
				cl = o.getClass();
				if ( cl.isArray() )
					cl = cl.getComponentType();
				else if ( Collection.class.isAssignableFrom(cl) )
					cl = ((Collection<?>)o).iterator().next().getClass();
				this.setArrayFlag(Context, o);
			}
		}*/
		Block.registerVariable(vname,cl);
		this.coll_name = "_iterator_"+vname;
		Block.registerVariable(this.coll_name,null);
		
		vstatus = Node.attr("jop_status").trim();
		Block.registerVariable((vstatus!=null&&!vstatus.isEmpty()?vstatus:"status"),Status.class);
	}
	// Check if is an array or collection
	//
	//
	private void setArrayFlag(WebAppContext Context, Object expResult) {
		Class<?> cl = null;
		Object o = (expResult==null?this.getExpression().execute(Context, null):expResult);
		if ( o != null ) {
			cl = o.getClass();
			if ( cl.isArray() ) {
				this.is_array = "Y";
			} else
				this.is_array = "N";
		}
	}
	static public class Status implements Serializable {
		private static final long serialVersionUID = 7337697015921709101L;
		protected int index;
		protected int size;
		public int getIndex() { return this.index; }
		public int getSize() { return this.size; }
	}
}
