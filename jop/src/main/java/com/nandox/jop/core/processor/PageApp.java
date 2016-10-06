package com.nandox.jop.core.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.nandox.jop.core.ErrorsDefine;
/**
 * Descrizione classe
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    PageApp.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class PageApp {
	protected static final String DOMPARSER_JOP_SELECTOR = "["+PageBlock.JOP_ATTR_ID+"]";
	private Document dom;
	private Map<String,PageBlock> blocks;
	
	/**
	 * Constructor: parse page content into DOM
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public PageApp(String ContentPage) throws DomException {
		this.dom = Jsoup.parse(ContentPage);
		this.blocks = new HashMap<String,PageBlock>();
		this.parse();
	}
	//
	//
	//
	private void parse() throws DomException {
		// Search every jop block into dom and create it
        Iterator<Element> elems = this.dom.select(DOMPARSER_JOP_SELECTOR).iterator();
    	while ( elems.hasNext() ) {
    		Element el = elems.next();
    		String id = el.attr(PageBlock.JOP_ATTR_ID);
			// check for double jop id
    		if ( this.blocks.containsKey(id) ) {
    			throw new DomException(ErrorsDefine.FormatDOM(ErrorsDefine.JOP_ID_DOUBLE,el));
    		} else {
    			// create block and check syntax error
    			this.blocks.put(id, new PageBlock(el));
    		}
    	}
		// Scan blocks for own child and attach them
    	PageBlock b[] = this.blocks.values().toArray(new PageBlock[0]);
    	for ( int ix=0; ix<b.length; ix++ ) {
    		ArrayList<PageBlock> child = new ArrayList<PageBlock>();
    		elems = b[ix].domEl.select(DOMPARSER_JOP_SELECTOR).iterator();
    		while ( elems.hasNext() ) {
        		Element el = elems.next();
        		Element p = el.parent(); 
    			while ( p != null ) {
    				if ( !p.attr(PageBlock.JOP_ATTR_ID).isEmpty() ) {
    					if ( p.attr(PageBlock.JOP_ATTR_ID).equals(b[ix].id) )
    						child.add(this.blocks.get(el.attr(PageBlock.JOP_ATTR_ID)));
    					break;
    				}
    				p = p.parent();
    			}
    		}
    		b[ix].child = child;
    	}
	}
	
}
