package com.nandox.jop.core.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nandox.jop.core.ErrorsDefine;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.logging.Logger;
import com.nandox.jop.core.processor.attribute.JopAttribute;

/**
 * Class of Page application.<p>
 * One page application is composed by PageBlock, every block can contains PageExpression and others blocks  
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
public class PageApp implements JopElement {
	/** Widget TAG */
	protected static final String JOP_WIDGET_TAG = "jwdg";
	/** Alternative Widget TAG */
	//protected static final String JOP_WIDGET_TAG_ALT = "div[jop_wdg]";
	/** DOM JOP block selector */
	protected static final String DOMPARSER_JOP_SELECTOR = JOP_WIDGET_TAG+","/*+JOP_WIDGET_TAG_ALT+","*/+PageApp.getAttributeSelector();
	/** Logger */
	protected static final Logger LOG = Logger.Factory.getLogger(PageApp.class);
	
	private static final String DOMPARSER_HEAD_TAG = "script[jop_head=\"true\"]";
	protected String id;	// page identifier
	private int hash;	// page hash code
	private Document dom;	// html DOM document
	private Map<String,PageBlock> blocks;	// list of all page blocks
	private WebAppContext appCtx;	// application web context
	private int auto_id_index;	// auto incremental index of anonymous block
	
	/**
	 * Constructor: parse page content into DOM
	 * @param	  Context	Application context
	 * @param	  PageId	page identificator
	 * @param	  ContentPage	html content page
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException if some syntax error
	 */	
	public PageApp(WebAppContext Context, String PageId, String ContentPage) throws ParseException {
		this.id = PageId;
		this.appCtx = Context;
		this.dom = Jsoup.parse(ContentPage);
		Document.OutputSettings settings = this.dom.outputSettings();
		settings.prettyPrint(false);
		settings.escapeMode(Entities.EscapeMode.extended);
		settings.charset("ASCII");
		this.blocks = new HashMap<String,PageBlock>();
		this.parse();
		this.hash = ContentPage.hashCode();
	}
	/**
	 * @return the blocks
	 */
	public Map<String, PageBlock> getBlocks() {
		return blocks;
	}
	/**
	 * @return the hash
	 */
	public int getHash() {
		return hash;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.JopElement#getParent()
	 */
	@Override
	public JopElement getParent() {
		return null;
	}
	/**
	 * Rendering the page: read every bean value and insert it into dom.<br>
	 * This method render only first level block 
	 * @param	  Context	Application context
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @return	  HTML of page
	 */	
	public String render(WebAppContext Context) throws RenderException {
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("start rendering page: %s", this.id);
		Iterator<PageBlock> i = this.blocks.values().iterator();
		Document d = this.dom.clone();
		while ( i.hasNext() ) {
			PageBlock pb = i.next();
			if ( !pb.isChild ) {
				Element e = d.getElementsByAttributeValue(JopAttribute.JOP_ATTR_ID, pb.id).first();
				e.replaceWith(pb.renderAsNode(Context));
			}
		}
		return d.html();
	}
	/**
	 * Performe action submit: search from own form and if present invoke in write mode with data
	 * This method call only first level block 
	 * @param	  Context	Application context
	 * @param	  Data	Map with name of tag and data
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void action(WebAppContext Context, Map<String,String[]> Data) {
		// Scan every block
		Iterator<PageBlock> i = this.blocks.values().iterator();
		while (i.hasNext() ) {
			PageBlock b = i.next();
			if ( !b.isChild )
				b.action(Context, Data, null);
		}
	}
	/**
	 * Return the page block corrispond to id
	 * @param	  BlockId	page block identifier
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public PageBlock getPageBlock(String BlockId) {
		return this.blocks.get(BlockId);
	}
	/**
	 * Return a new id computed automatically
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  auto incremented value
	 */
	protected String assignAutoId(){
		auto_id_index++;
		return ""+auto_id_index;
	}
	/**
	 * Check if element is a block or not
	 * @param	  Elem	element to check
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  true if block otherwise false
	 */
	protected boolean checkIfIsBlock(Element Elem) {
		return Elem.select(DOMPARSER_JOP_SELECTOR).first() == Elem;
	}
	/**
	 * Create a block from dom element
	 * @param	  Context	Application context
	 * @param	  Parent	parent element
	 * @param	  Elem 		element that represent a block   
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  created block
	 */
	protected PageBlock createBlock(WebAppContext Context,JopElement Parent, Element Elem) throws DomException {
		PageBlock b;
		// test the type of block (if widget or general)
		if ( Elem.tagName().equalsIgnoreCase(JOP_WIDGET_TAG) /*|| (el.tagName().equalsIgnoreCase("div") && el.hasAttr("jop_wdg"))*/ ) {
			b = this.appCtx.factoryWidget(Context,Parent,Elem);
		} else
			b = new GenericPageBlock(Context,Parent,Elem);
		// check for double jop id
		if ( this.blocks.containsKey(b.id) ) {
			if (LOG != null && LOG.isErrorEnabled() ) LOG.error("double block %s on page %s",b.id, this.id);
			throw new DomException(ErrorsDefine.formatDOM(ErrorsDefine.JOP_ID_DOUBLE,Elem));
		}
		this.blocks.put(b.id, b);
		return b;
	}
	// Parsing page content to search and build every block 
	//
	//
	private void parse() throws ParseException {
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("parsing page: %s", this.id);
		// Search jop head and substitute with script file include
		Elements list = this.dom.select(DOMPARSER_HEAD_TAG);
		for ( int ix=0; ix<list.size(); ix++ ) {
			Element el = list.get(ix);
			this.buildHeadScript(el);
		}
		// Search every 1st level jop block into dom and create it
		list = this.dom.select(DOMPARSER_JOP_SELECTOR);
		for ( int ix=0; ix<list.size(); ix++ ) {
			Element el = list.get(ix);
			if ( !this.hasParentBlock(el) ) {
    			// create block and check syntax error
    			try {
    				this.createBlock(this.appCtx,this,el);
    			} catch (Exception e) {
    				throw new ParseException(ErrorsDefine.formatDOM(e.getMessage(),el));
    			}
			}
		}
	}
	// check if element has a parent that's a block 
	//
	//
	private boolean hasParentBlock (Element elem) {
		Element p[] = elem.parents().toArray(new Element[0]); // sort parent list
		for ( int ix=0; ix<p.length; ix++ ) {
			if ( p[ix].select(DOMPARSER_JOP_SELECTOR).first() == p[ix] )
				return true;
		}
		return false;
	}
	// Build head scripts inclusion
	// 
	//
	private void buildHeadScript (Element el) {
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("build head scripts for page: %s", this.id);
		String pth = WebAppContext.getCurrentRequestContext().getHttpRequest().getContextPath();
		el.before("<link type=\"text/css\" rel=\"stylesheet\" href=\""+pth+"/jopscript/base.css\"/>");
		el.before("<script type=\"text/javascript\" src=\""+pth+"/jopscript/baselibs.js\"/>");
		el.before("<script type=\"text/javascript\" src=\""+pth+"/jopscript/core/services.js\"/>");
		el.remove();
		List lst =  com.nandox.jop.core.sevices.ServiceJSServlet.getResourceList("js");
		lst =  com.nandox.jop.core.sevices.ServiceJSServlet.getResourceList("css");
	}
	// Generate css selector with all possible jop attributes
	//
	//
	static private String getAttributeSelector() {
		String sel = "["+JopAttribute.JOP_ATTR_ID+"]";
		String lst[] = JopAttribute.Factory.getNameList();
		for ( int ix=0; ix<lst.length; ix++ ) {
			sel += ",["+lst[ix]+"]";
		}
		return sel;
	}
}
