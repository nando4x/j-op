package com.nandox.jop.core.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.context.WebAppContext;
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
	private WebAppContext appCtx;
	/**
	 * Constructor: parse page content into DOM
	 * @param	  Context	Application context
	 * @param	  ContentPage	html content page
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public PageApp(WebAppContext Context, String ContentPage) throws DomException {
		this.appCtx = Context;
		this.dom = Jsoup.parse(ContentPage);
		this.blocks = new HashMap<String,PageBlock>();
		this.parse();
	}
	public String Render(WebAppContext Context) {
		Iterator<PageBlock> i = this.blocks.values().iterator();
		Document d = this.dom.clone();
		while ( i.hasNext() ) {
			PageBlock pb = i.next();
			if ( !pb.isChild ) {
				Element w = d.getElementsByAttributeValue(PageBlock.JOP_ATTR_ID, pb.id).first().wrap("<div>");
				w.html(pb.Render(Context));
				w.unwrap();
			}
		}
		return d.html();
	}
	// Parsing page content to search and build every block 
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
    			this.blocks.put(id, new PageBlock(this.appCtx,el));
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
    					if ( p.attr(PageBlock.JOP_ATTR_ID).equals(b[ix].id) ) {
    						child.add(this.blocks.get(el.attr(PageBlock.JOP_ATTR_ID)));
    						this.blocks.get(el.attr(PageBlock.JOP_ATTR_ID)).isChild = true;
    					}
    					break;
    				}
    				p = p.parent();
    			}
    		}
    		b[ix].child = child;
    	}
	}
	
}
