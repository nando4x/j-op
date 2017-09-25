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
	 * @param	  PageId	page identificator
	 * @param	  DomElement	HTML element of the block
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
public GenericPageBlock(WebAppContext Context, String PageId, Element DomElement) throws DomException {
		super(Context, PageId, DomElement);
		this.parse(Context);
	}

}
