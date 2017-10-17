package com.nandox.jop.core.processor.attribute;

import java.util.Map;
import org.jsoup.nodes.Element;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.RenderException;
import com.nandox.jop.core.processor.attribute.JopAttribute.Response; 

/**
 * Rendering Attribute of page block.<p>
 * Use this interface to implement every jop attribute with preRender and postRender method on rendering of page block,<br>
 * every action can return an RETURN_ACTION to pilot the rest of rendering.<br>
 * To implement an attribute have to create a Class that implements JopAttributeRendering or JopAttributeAction.<br>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    JopAttributeRendering.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public interface JopAttributeRendering {
	/**
	 * Pre rendering action executed before rendering block operation
	 * @param	  Context	Application context
	 * @param	  Dom element dom of the block
	 * @param	  Vars 	list of block variables instance [variable name, variable value instanced]
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public Response preRender(WebAppContext Context, Element Dom, Map<String,Object> Vars) throws RenderException;
	/**
	 * Post rendering action executed before rendering block operation
	 * @param	  Context	Application context
	 * @param	  Dom element dom of the block
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public Response postRender(WebAppContext Context, Element Dom) throws RenderException;
}
