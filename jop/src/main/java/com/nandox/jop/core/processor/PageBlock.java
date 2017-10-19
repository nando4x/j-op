package com.nandox.jop.core.processor;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.Comparator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attribute;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.logging.Logger;
import com.nandox.jop.core.context.BeanMonitoring;
import com.nandox.jop.core.processor.attribute.JopAttribute;
import com.nandox.jop.core.processor.attribute.JopAttributeRendering;
import com.nandox.jop.core.processor.attribute.JopAttributeAction;
import com.nandox.jop.core.processor.expression.PageExpression;
import com.nandox.jop.core.processor.expression.PageWriteExpression;
import com.nandox.jop.core.processor.expression.ExpressionConverter;
import com.nandox.jop.core.processor.attribute.AbstractJopAttribute;
import com.nandox.jop.core.ErrorsDefine;

/**
 * This is basic part of a page.<p>
 * The Page block is identified by a JopId and can contains some page expressions.<br>
 * A block before is parsed to create the expressions runtime compiled class and than is rendered to execute previous created expressions
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    PageBlock.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
/**
 * @author EE38938
 *
 */
public abstract class PageBlock implements JopElement {
	private static final String JOP_BEAN_TAG = "jbean";
	/** block DOM */
	protected Element domEl;
	/** block identifier */
	protected String id;
	/** child blocks */
	protected List<PageBlock> child;
	/** true if block is child of another block */
	protected boolean isChild;
	/** Logger */
	protected static final Logger LOG = Logger.Factory.getLogger(PageBlock.class);
	
	private static final String tmp_attr_id = "_jop_tmp_id";
	private static final String form_selector = "select, [value^=java{]:not(option)";
	private JopElement parent;	// parent element
	private String pageId;		// parent page identifier
	private int auto_id_index;	// auto incremental index of anonymous form input and for DOM attributes identifiers
	private Parser parser;		// own parser
	
	// internal class for renderable element: all DOM elements that have one or more expression 
	private class Renderable {
		Element elem;
		PageBlock block;
		PageExpression beans;		// jbean
		PageWriteExpression form;	// form input expression
		Map<String,PageExpression> html_attrs;	// list of DOM attribute with an expression [attribute id, attribute expression]
		Renderable() {
			this.html_attrs = new HashMap<String,PageExpression>();
		}
	}
	private List<Renderable> rends;	// list of renderable element
	private List<Renderable> forms;	// list of renderable element with form
	private Map<String,PageExpression> exprs;		// list of all expressions [id, expression created]
	private List<JopAttribute> attrs;				// list of Jop Attribute of block
	private Map<String,Class<?>> vars_definition;	// list of block variables [variable name, variable java class]
	
	/**
	 * Constructor: parse DOM element
	 * @param	  Context	Application context
	 * @param	  Parent	parent element
	 * @param	  DomElement	HTML element of the block
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */	
	public PageBlock(WebAppContext Context, JopElement Parent, Element DomElement) throws DomException {
		this.parent = Parent;
		this.pageId = this.getPage().id;
		this.domEl = DomElement;
		this.id = this.domEl.attr(JopAttribute.JOP_ATTR_ID);
		
		this.exprs = new HashMap<String,PageExpression>();
		this.rends = new ArrayList<Renderable>();
		this.forms = new ArrayList<Renderable>();
		this.attrs = new ArrayList<JopAttribute>();
		this.vars_definition = new HashMap<String,Class<?>>();
		// get parent variables
		if ( Parent instanceof PageBlock )
			this.vars_definition.putAll(((PageBlock)Parent).getVarsDefinition());
		this.parser = new Parser(this.exprs,this.vars_definition);
		this.child = new ArrayList<PageBlock>();
		// Assign jop_id if not just defined
		String id = this.domEl.attr(JopAttribute.JOP_ATTR_ID);
		if ( id.isEmpty() ) {
			id = this.getPage().assignAutoId();
			this.domEl.attr(JopAttribute.JOP_ATTR_ID,id);
		}
		this.id = this.domEl.attr(JopAttribute.JOP_ATTR_ID);
		
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * @return the parser
	 */
	public Parser getParser() {
		return this.parser;
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.JopElement#getParent()
	 */
	@Override
	public JopElement getParent() {
		return this.parent;
	}
	/**
	 * Parse block.<br>
	 * @param	  Context	Application context
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException for syntax error
	 * @return	  
	 */	
	protected void parse(WebAppContext Context, JopElement Parent) throws DomException {
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("parsing block id: (%s) %s",this.pageId,this.id);
		BeanMonitoring mon = Context.getBeanMonitor(); // get bean monitor
		
		// Get and process own attributes
		Renderable own = new Renderable();
		own.elem = this.domEl;
		this.parseAttributes(Context, own,mon,true, false);
		this.rends.add(own);
		
		// Scan all element
		Iterator<Element> elems = this.domEl.getAllElements().iterator();
		while (elems.hasNext() ) {
			Element el = elems.next();
			Renderable rend = new Renderable(); // create renderable element
			rend.elem = el;
			if ( this.checkIfParentBlockIsThis(el, false)) {
				// check if bean or child block or form tag
				if ( el.tagName().equalsIgnoreCase(JOP_BEAN_TAG) || el.tagName().equalsIgnoreCase("option") ) { // is bean (special case for option because
																												// don't accept tag inside
					// build expression and join the same
					PageExpression exp = this.parser.expressionParser(Context, "java"+el.text().trim(), new JopId(this.pageId,this.id));
					el.attr(tmp_attr_id,exp.getId());
					rend.beans = exp;
				} else if ( this.getPage().checkIfIsBlock(el) ) { // is child
					PageBlock b = this.getPage().createBlock(Context, this, el);
					b.isChild = true;
					this.child.add(b);
					rend.block = b;
				} else if ( el.select(form_selector).first() == el && el.tag().isFormSubmittable() ) { // is form element
					this.computeFormTag(Context, rend, mon);
				} 
				// compute attributes of child
				this.parseAttributes(Context, rend,mon,false,el.tagName().equalsIgnoreCase("option"));
				this.rends.add(rend);
			} else if ( el == this.domEl && el.select(form_selector).first() == el && el.tag().isFormSubmittable() ) { // this block is form tag
				this.computeFormTag(Context, own, mon);
			}
		}
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("parsed block id: (%s) %s",this.pageId,this.id);
	}
	/**
	 * Rendering block and return string of its contents.<br>
	 * @param	  Context	Application context
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  html in string format
	 */	
	public String render(WebAppContext Context) throws RenderException {
		return this.renderAsNode(Context).outerHtml();
	}
	/**
	 * Rendering block.<br>
	 * First rendering child in depth and when child is finish or not present for self invoke the ???  
	 * @param	  Context	Application context
	 * @date      30 set 2016 - 30 set 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 * @return	  
	 */	
	protected Node renderAsNode(WebAppContext Context) throws RenderException {
		return this.renderAsNode(Context,0,null); //TODO: which parent variables get if renderer only child block?
	}
	/**
	 * Perform action form submit
	 * @param	  Context	Application context
	 * @param	  Data	Form data Map [name of input,data]
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void action(WebAppContext Context, Map<String,String[]> Data) {
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("submitting block id: (%s) %s",this.pageId,this.id);
		this.realAction(Context, Data);
		WebAppContext.getCurrentRequestContext().getRefreshableBlock(new JopId(this.pageId,this.getId())).setToBeRefreshed();
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("submitted block id: (%s) %s",this.pageId,this.id);
	}
	/* (non-Javadoc)
	 * @see com.nandox.jop.core.processor.RefreshableBlock#ResetToBeRefreshed(boolean)
	 */
	public void resetToBeRefreshed(boolean ResetChild) {
		WebAppContext.getCurrentRequestContext().getRefreshableBlock(new JopId(this.pageId,this.getId())).resetToBeRefreshed();
		if ( ResetChild ) {
			Iterator<PageBlock> cl = this.child.iterator();
			while ( cl.hasNext() ) {
				cl.next().resetToBeRefreshed(ResetChild);
			}
		}
	}
	/**
	 * Register variable with name and type class on this block  
	 * @param	  Name	Variable name
	 * @param	  Type	Variable name
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  
	 */
	public void registerVariable(String Name, Class<?> Type) {
		this.vars_definition.put(Name, Type);
		// TODO: manage duplicate 
	}
	/**
	 * @return the vars_definition
	 */
	public Map<String, Class<?>> getVarsDefinition() {
		return vars_definition;
	}
	// Scan parents until PageApp
	//
	//
	private PageApp getPage() {
		JopElement p = this.parent;
		while ( !(p instanceof PageApp) ) {
			p = p.getParent();
		}
		return (PageApp)p;
	}
	// instance all variables to null or parent value
	//
	//
	private Map<String,Object> instanceVariables(Map<String,Object> parentVars) {
		Map<String,Object> vars = new HashMap<String,Object>();
		Iterator<String> v = this.vars_definition.keySet().iterator();
		while (v.hasNext()) {
			String key = v.next(); 
			vars.put(key, (parentVars!=null?parentVars.get(key):null));
		}
		return vars;
	}
	// Real render as node 
	//
	//
	private Node renderAsNode(WebAppContext Context, int index, Map<String,Object> parentVars ) throws RenderException {
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("rendering block id: (%s) %s",this.pageId,this.id);
		// Reset all value expression
		this.resetAllExprValue(Context);
		Element clone = this.cloneElement(domEl);
		int num = 1;
		ExpressionConverter conv = null;
		// check render attribute
		Iterator<JopAttribute> attr = this.attrs.iterator();
		while (attr.hasNext()) {
			JopAttribute ja = attr.next();
			if ( !(ja instanceof JopAttributeRendering) ) // skip if an action attribute
				continue;
			JopAttribute.Response r = ((JopAttributeRendering)ja).preRender(Context,clone,parentVars);
			switch (r.getAction()) {
				case NOTRENDER:
					if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("NOT rendered block id: (%s) %s",this.pageId,this.id);
					return new TextNode((String)r.getResult(),"");
				case CONVERTER:
					conv = (ExpressionConverter)r.getResult();
					break;
				default:
					num = r.getRepater_num();
					break;
			}
		}
		// real render
		this.renderer(Context,clone,num,index,conv,parentVars);
		if (LOG != null && LOG.isDebugEnabled() ) LOG.debug("rendered block id: (%s) %s",this.pageId,this.id);
		return clone;
	}
	// Real final render call 
	//
	//
	private void renderer(WebAppContext Context, Element clone, int repeat, int index, ExpressionConverter converter, Map<String,Object> parentVars) throws RenderException {
		int num = 0;
		Map<String,Object> vars = this.instanceVariables(parentVars);
		Element temp = this.cloneElement(clone);
		clone.empty();
		while (num<repeat) {
			Element item = this.cloneElement(temp);
			// Set variables
			Iterator<JopAttribute> attr = this.attrs.iterator();
			while (attr.hasNext()) {
				JopAttribute ja = attr.next();
				ja.setVariables(Context,vars,num);
			}
			// Reset expression value for each repetition
			Iterator<PageExpression> ie = this.exprs.values().iterator();
			while (ie.hasNext() ) {
				ie.next().resetValue(Context);
			}
			// Scan renderable element
			Iterator<Renderable> rends = this.rends.iterator();
			while ( rends.hasNext() ) {
				Renderable rend = rends.next();
				// check if a block child or dom element
				if ( rend.block != null ) { // rendering child block
					Element e = item.getElementsByAttributeValue(JopAttribute.JOP_ATTR_ID, rend.block.id).first();
					e.replaceWith(rend.block.renderAsNode(Context,num,vars));
				} else {
					try {
						// fire own bean and insert into html searching by tmp jop id
						if ( rend.beans != null ) {
							String v = (String)rend.beans.execute(Context, vars);
							Iterator<Element> elem = item.select(rend.elem.tagName()+"["+tmp_attr_id+"="+rend.elem.attr(tmp_attr_id)+"]").iterator(); 
							while (elem.hasNext()) {
								if ( rend.elem.tagName().equalsIgnoreCase("option") ) {
									elem.next().html((v==null?"":v));
								} else {
									TextNode txt = new TextNode((v==null?"":v),"");
									elem.next().replaceWith(txt);
								}
							}
						}
						// compute all html attributes
						Iterator<Entry<String,PageExpression>> attrs = rend.html_attrs.entrySet().iterator();
						if ( !rend.elem.attr(tmp_attr_id).isEmpty() && attrs.hasNext() ) {
							Element ea = item.getElementsByAttributeValue(tmp_attr_id, rend.elem.attr(tmp_attr_id)).first();
							while (attrs.hasNext()) {
								Entry<String,PageExpression> en = attrs.next();
								PageExpression b = en.getValue();
								String a = ea.attr(en.getKey());
								ea.attr(en.getKey(),a.replace("java"+b.getCode(), (String)b.execute(Context, vars)));
							}
							ea.removeAttr(tmp_attr_id);
						}
						
						// compute form
						if ( rend.form != null ) {
							Object o = rend.form.execute(Context, vars);
							String v = (converter!=null?converter.callOutputConverter(Context, o):(String)o);	
							Element e = item.getElementsByAttributeValue("name", rend.elem.attr("name")).first();
							if ( e != null ) {
								// setting value attribute
								String a = e.attr("value");
								e.attr("value",a.replace("java"+rend.form.getCode(), (v!=null?v:"") ));
								// add page id to name attribute
								e.attr("name","["+this.pageId+"]."+e.attr("name"));
							}
						}
					} catch (Exception e) {
						throw new RuntimeException(ErrorsDefine.formatDOM(e.getMessage(),rend.elem));
					}
				}
			}
			clone.append(item.html());
			// substitute all block attribute
			Iterator<Attribute> attrs = item.attributes().iterator();
			while (attrs.hasNext() ) {
				Attribute a = attrs.next();
				clone.attr(a.getKey(),item.attr(a.getKey()));
			}
			num++;
		}
		// delete jop_ attribute (exclude jop_id) from dom and then add page id into jop_id
		this.cleanDomFromAttribute(clone);
		// add page id into jop_id with index
		clone.attr(JopAttribute.JOP_ATTR_ID,"["+this.pageId+"]."+this.id+(index>0?"+"+index:""));
		// TODO: gestire eccezzione esecuzione espressioni
	}
	// Parse attributes element to verify delimiter { }
	//
	//
	@SuppressWarnings("rawtypes")
	private void parseAttributes(WebAppContext context, Renderable rend, BeanMonitoring mon, boolean isOwn, boolean includeValue) throws DomException {
		// scan all element's attributes excluding value and jop_id
		List<Attribute> lst = new ArrayList<Attribute>(rend.elem.attributes().asList());
		Collections.sort(lst,new AttributeComparator());
		Iterator<Attribute> attrs = lst.iterator();
		while (attrs.hasNext() ) {
			Attribute attr =  attrs.next();
			if ( !attr.getKey().equalsIgnoreCase(JopAttribute.JOP_ATTR_ID) && (includeValue || !(attr.getKey().equalsIgnoreCase("value") && rend.elem.tag().isFormSubmittable())) ) {
				String a = attr.getValue();
				String bid = this.parser.parseJavaExpression(a); 
				if ( bid != null || ( attr.getKey().toLowerCase().startsWith("jop_") && JopAttribute.Factory.isKnown(attr.getKey()) ) ) {
					if ( attr.getKey().toLowerCase().startsWith("jop_") ) {
						if ( isOwn && JopAttribute.Factory.isKnown(attr.getKey()) ) {
							// block jop attribute
							JopAttribute ja = JopAttribute.Factory.create(context,this,rend.elem,attr.getKey(),(bid!=null?bid:a));
							this.attrs.add(ja);
							if ( ((AbstractJopAttribute)ja).getExpression() != null )
								mon.registerRefreshable(((AbstractJopAttribute)ja).getExpression().getBeansList(), new JopId(this.pageId,this.id));
						}
					} else {
						// html attribute: parse expression and verify if exist
						PageExpression exp = this.parser.expressionParser(context, a, new JopId(this.pageId,this.id));
						// add attribute to map and identify element with temp id
						String id = ""+this.auto_id_index++;
						rend.elem.attr(tmp_attr_id,id);
						rend.html_attrs.put(attr.getKey(), exp);
					}
				}
			}
		}
	}
	// compute expression for form tag
	//
	//
	private void computeFormTag(WebAppContext Context, Renderable rend, BeanMonitoring mon) throws DomException {
		String a = rend.elem.attr("value");
		String code = this.parser.parseJavaExpression(a); 
		if ( code != null ) {
			// create new expression and register this block to those to refresh 
			PageWriteExpression exp = (PageWriteExpression)this.parser.expressionParser(Context, a, new JopId(this.pageId,this.id));
			// if name attributes don't exist add it with auto index
			if ( !rend.elem.hasAttr("name") || rend.elem.attr("name").isEmpty() ) {
				rend.elem.attr("name",""+this.auto_id_index);
				auto_id_index++;
			}
			rend.form = exp;
			this.forms.add(rend);
		}
	}
	// real action submission
	//
	//
	private void realAction(WebAppContext Context, Map<String,String[]> Data) {
		Iterator<JopAttribute> attr = this.attrs.iterator();
		// check pre action attribute
		ExpressionConverter conv = null;
		while (attr.hasNext()) {
			JopAttribute ja = attr.next();
			if ( ja instanceof JopAttributeAction ) {
				JopAttribute.Response r = ((JopAttributeAction)ja).preAction(Context,this.domEl,null); //TODO: what variables use?
				if ( r != null ) {
					switch (r.getAction()) {
						case CONVERTER:
							conv = (ExpressionConverter)r.getResult();
							break;
						default:
							break;
					}
				}
			}
		}

		// Search each form tag with key (name) of the Data
		Iterator<Renderable> i = this.forms.iterator();
		while ( i.hasNext() ) {
			Renderable rend = i.next();
			PageWriteExpression pe = rend.form;
			if ( Data != null && Data.containsKey(rend.elem.attr("name")) ) {
				// Invoke expression in write mode
				String val = Data.get(rend.elem.attr("name"))[0]; // get string data
				pe.execute(Context, (conv!=null?conv.callInputConverter(Context, val):val), val, null); //TODO: what variables use?
			}
		}
		Iterator<PageBlock> c = this.child.iterator();
		while ( c.hasNext() ) {
			c.next().realAction(Context, Data);
		}
		// check post action attribute
		attr = this.attrs.iterator();
		while (attr.hasNext()) {
			JopAttribute ja = attr.next();
			if ( ja instanceof JopAttributeAction ) {
				((JopAttributeAction)ja).postAction(Context,this.domEl,null); //TODO: what variables use?
			}
		}
	}
	// Check if parent job_id is of this block
	//
	//
	private boolean checkIfParentBlockIsThis ( Element el, boolean mustBeForm ) {
		Element p = el.parent(); 
		while ( p != null ) {
			// check if tag is in another child block 
			if ( p.hasAttr(JopAttribute.JOP_ATTR_ID) /* && (!mustBeForm || p.tagName().equalsIgnoreCase("form") )*/ ) {
				if ( p.attr(JopAttribute.JOP_ATTR_ID).equals(this.id) )
					return true;
				else
					return false;
			}
			p = p.parent();
		}
		return false;
	}
	// Reset all expression value
	//
	//
	private void resetAllExprValue(WebAppContext context) {
		Iterator<PageExpression> exp = this.exprs.values().iterator();
		while ( exp.hasNext() ) {
			exp.next().resetValue(context);
		}
	}
	// Erase all jop attribute and tmp attribute from DOM
	//
	//
	private void cleanDomFromAttribute(Element elem) {
		String attrs[] = JopAttribute.Factory.getNameList();
		for ( int ix=0; ix<attrs.length; ix++ )
			elem.removeAttr(attrs[ix]);
		elem.removeAttr(tmp_attr_id);
	}
	// clone element: use this instead jsoup clone to force the escaping html special chars
	//
	//
	private Element cloneElement(Element elem) {
		Element e = elem.clone();
		Document d = new Document("");
		Document.OutputSettings settings = d.outputSettings();
		settings.prettyPrint(false);
		settings.escapeMode(Entities.EscapeMode.extended);
		settings.charset("ASCII");
		d.appendChild(e);
		return e;
	}
	// Comparator for jop attribute name
	//
	//
	private class AttributeComparator implements Comparator<Attribute> {
		private final String[] jop_attrs = JopAttribute.Factory.getNameList();
		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compare(Attribute a1, Attribute a2) {
			int i1 = -1000, i2 = -1000;
			for (int ix=0; ix<jop_attrs.length; ix++) {
				if ( jop_attrs[ix].equalsIgnoreCase(a1.getKey()))
					i1 = ix;
				if ( jop_attrs[ix].equalsIgnoreCase(a2.getKey()))
					i2 = ix;
			}
			return i1>i2?-1:(i1==i2?0:1);
		}

		
	}
}
