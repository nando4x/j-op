package com.nandox.jop.core.processor;

import java.lang.reflect.InvocationTargetException;
import org.jsoup.nodes.Element;
import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.context.WebAppContext;
/**
 * Attribute of page block, it create own bean based on class name specified in attribute list (ATTR_LIST).<br>
 * !!! REMBER...WHEN ADD NEW ATTRIBUTE ADD ITS ITEM IN ATTR_LIST !!! 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BlockAttribute.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class BlockAttribute {
	/** Identification attribute: jop_id */
	public static final String JOP_RENDERED_ID = "jop_rendered";
	/** List of possible attributes */
	protected static final String[][] ATTR_LIST = { 
													{"*",SimplePageExpression.class.getCanonicalName()},
													{JOP_RENDERED_ID,BooleanPageExpression.class.getCanonicalName()}
												  };
	protected static final int ATTR_NAME = 0;
	protected static final int ATTR_CLASS = 1;
	/** */
	protected String name;
	/** */
	protected PageExpression expr;
	/**
	 * Constructor: parse DOM element
	 * @param	  Context	Application context
	 * @param	  Name		Attribute name
	 * @param	  Code		Attribute code
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException if attribute name not found or syntax error
	 */	
	public BlockAttribute(WebAppContext Context, String Name, String Code) throws DomException {
		this.name = Name;
		int ix;
		for ( ix=0; ix<ATTR_LIST.length; ix++ ) {
			if ( Name.toLowerCase().equals(ATTR_LIST[ix][ATTR_NAME].toLowerCase()) )
				break;
		}
		if ( ix >= ATTR_LIST.length )
			ix = 0;
		this.parse(Context,Code,ix);
	}
	public static void CleanDomFromAttribute(Element DomEl) {
		DomEl.removeAttr(JOP_RENDERED_ID);
	}
	//
	//
	//
	private void parse(WebAppContext context, String code, int listInx) throws DomException {
		try {
			this.expr = (PageExpression)Class.forName(ATTR_LIST[listInx][ATTR_CLASS]).getDeclaredConstructor(WebAppContext.class, String.class).newInstance(context,code);
			return;
		} catch ( InvocationTargetException e ) {
			throw new DomException(e.getTargetException().getMessage());
		} catch ( Exception e ) {
			throw new DomException(e.getMessage());
		}
	}
	
}
