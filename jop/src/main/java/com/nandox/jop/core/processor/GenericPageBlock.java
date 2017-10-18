package com.nandox.jop.core.processor;

import org.jsoup.nodes.Element;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Generic common page block.<p>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    GenericPageBlock.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class GenericPageBlock extends PageBlock {

	/**
	 * Constructor: parse DOM element
	 * @param	  Context	Application context
	 * @param	  Page		parent page
	 * @param	  DomElement	HTML element of the block
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public GenericPageBlock(WebAppContext Context, JopElement Parent, Element DomElement) throws DomException {
		super(Context,Parent,DomElement);
		this.parse(Context,Parent);
	}
}
