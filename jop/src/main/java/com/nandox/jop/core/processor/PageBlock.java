package com.nandox.jop.core.processor;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.Comparator;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
//import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.context.BeanMonitoring;
import com.nandox.jop.core.processor.attribute.JopAttribute;
import com.nandox.jop.core.processor.expression.AbstractPageExpression;
import com.nandox.jop.core.processor.expression.PageExpression;
import com.nandox.jop.core.processor.expression.PageWriteExpression;
import com.nandox.jop.core.processor.expression.SimplePageExpression;
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
public class PageBlock {
//	public static final String JOP_BEAN_INI = "jop_bean={";
	public static final String JOP_EXPR_INI = "{";
	public static final String JOP_EXPR_END = "}";
	public static final String JOP_BEAN_TAG = "jbean";
	/** block DOM */
	protected Element domEl;
	/** block identifier */
	protected String id;
	/** child blocks */
	protected List<PageBlock> child;
	/** true if block is child of another block */
	protected boolean isChild;
	
	private static final String tmp_attr_id = "_jop_tmp_id";
	private static final String form_selector = "[value^=java{]";
	private String pageId;	// parent page identifier
	private int auto_id_index;	// auto incremental index of anonymous form input and for DOM attributes identifiers
	//private Element clone;
	
	private Map<String,PageExpression> exprs;	// list of all expressions [id, expression created]
	private Map<String,PageExpression> beans;	// list of all jbean	[bean id, expression reference in exprs list]
	private Map<String,PageWriteExpression> forms;	// list of all form input expression [input name, expression reference in exprs list]

	// class for DOM attribute expression 
	private class AttributeExpr {
		String name;		// DOM attribute name
		PageExpression expr;	// expression
		boolean isOwn;	// true if is an attribute of page block html element
	}
	
	private Map<String,AttributeExpr> html_attrs;	// list of DOM attribute with an expression [attribute id, attribute expression]
	private List<JopAttribute> attrs;	// list of Jop Attribute of block
	private Map<String,Class<?>> vars_definition;	// list of block variables [variable name, variable java class]
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
	public PageBlock(WebAppContext Context, String PageId, Element DomElement) throws DomException {
		this.pageId = PageId;
		this.domEl = DomElement;
		this.id = this.domEl.attr(JopAttribute.JOP_ATTR_ID);
		
		this.exprs = new HashMap<String,PageExpression>();
		this.beans = new HashMap<String,PageExpression>();
		this.forms = new HashMap<String,PageWriteExpression>();
		this.html_attrs = new HashMap<String,AttributeExpr>();
		this.attrs = new ArrayList<JopAttribute>();
		this.vars_definition = new HashMap<String,Class<?>>();
		
		this.parse(Context);
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
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
	public String render(WebAppContext Context) {
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
	protected Node renderAsNode(WebAppContext Context) {
		return this.renderAsNode(Context,0);
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
		// Search form tag with key (name) of the Data
		Iterator<Entry<String,PageWriteExpression>> i = this.forms.entrySet().iterator();
		while ( i.hasNext() ) {
			Entry<String,PageWriteExpression> pe = i.next();
			if ( Data != null && Data.containsKey(pe.getKey()) ) {
				// Invoke expression in write mode
				String val = Data.get(pe.getKey())[0]; // get string data
				pe.getValue().execute(Context, val, null); //TODO: what variables use?
			}
		}
		// check action attribute
		Iterator<JopAttribute> attr = this.attrs.iterator();
		while (attr.hasNext()) {
			JopAttribute ja = attr.next();
			if ( ja.isActionAttribute() ) {
				ja.preRender(Context,this.domEl,null); //TODO: what variables use?
			}
		}
		WebAppContext.getCurrentRequestContext().getRefreshableBlock(new JopId(this.pageId,this.getId())).setToBeRefreshed();
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
	//
	//
	//
	private Map<String,Object> instanceVariables() {
		Map<String,Object> vars = new HashMap<String,Object>();
		Iterator<String> v = this.vars_definition.keySet().iterator();
		while (v.hasNext()) {
			vars.put(v.next(), null);
		}
		return vars;
	}
	// Real render as node 
	//
	//
	private Node renderAsNode(WebAppContext Context, int index) {
		// Reset all value expression
		this.resetAllExprValue(Context);
		Element clone = this.domEl.clone();
		int num = 1;
		// check render attribute
		Iterator<JopAttribute> attr = this.attrs.iterator();
		while (attr.hasNext()) {
			JopAttribute ja = attr.next();
			if ( ja.isActionAttribute() ) // skip if an action attribute
				continue;
			JopAttribute.Response r = ja.preRender(Context,clone,null); //TODO: what variables use?
			switch (r.getAction()) {
				case NOTRENDER:
					return new TextNode(r.getResult(),"");
				default:
					num = r.getRepater_num();
					break;
			}
		}
		// real render
		this.renderer(Context,clone,num,index);
		return clone;
	}
	// Real final render call 
	//
	//
	private void renderer(WebAppContext Context, Element clone, int repeat, int index) {
		int num = 0;
		Map<String,Object> vars = this.instanceVariables();
		Element temp = clone.clone();
		clone.empty();
		while (num<repeat) {
			Element item = temp.clone();
			// ### Rendering all child in recursive mode
			Iterator<PageBlock> cl = this.child.iterator();
			while ( cl.hasNext() ) {
				PageBlock c = cl.next();
				Element e = item.getElementsByAttributeValue(JopAttribute.JOP_ATTR_ID, c.id).first();
				e.replaceWith(c.renderAsNode(Context,num));
			}
			// ### Start effective render
			// set variables
			Iterator<JopAttribute> attr = this.attrs.iterator();
			while (attr.hasNext()) {
				JopAttribute ja = attr.next();
				ja.setVariables(Context,vars,num);
			}
			// ### Fire every own bean and insert into html
			try {
				Iterator<Entry<String,PageExpression>> beans = this.beans.entrySet().iterator();
				while (beans.hasNext()) {
					Entry<String,PageExpression> e = beans.next();
					PageExpression b = e.getValue();
					if ( repeat > 1 )
						b.resetValue(Context);
					String v = (String)b.execute(Context, vars);
					Iterator<Element> elem = item.select(JOP_BEAN_TAG+"#"+e.getKey()).iterator();
					while (elem.hasNext()) {
						TextNode txt = new TextNode((v==null?"":v),"");
						elem.next().replaceWith(txt);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(ErrorsDefine.formatDOM(e.getMessage(),this.domEl));
			}

			// ### Compute all html attributes of child
			Iterator<Entry<String,AttributeExpr>> attrs = this.html_attrs.entrySet().iterator();
			while (attrs.hasNext()) {
				Entry<String,AttributeExpr> en = attrs.next();
				AttributeExpr b = en.getValue();
				if ( !b.isOwn ) {
					Element elem = item.getElementsByAttributeValue(tmp_attr_id, en.getKey()).first();
					try {
						if ( elem == null )
							continue;
						if ( repeat > 1 )
							b.expr.resetValue(Context);
						String a = elem.attr(b.name);
						elem.attr(b.name,a.replace("java"+b.expr.getCode(), (String)b.expr.execute(Context, vars)));
						elem.removeAttr(tmp_attr_id);
					} catch (Exception e) {
						throw new RuntimeException(ErrorsDefine.formatDOM(e.getMessage(),elem));
					}
				}
			}
			// ### Compute forms 
			Iterator<Entry<String,PageWriteExpression>> f = this.forms.entrySet().iterator();
			while ( f.hasNext() ) {
				Entry<String,PageWriteExpression> b = f.next();
				if ( repeat > 1 )
					b.getValue().resetValue(Context);
				String v = (String)b.getValue().execute(Context, vars);
				Element e = item.getElementsByAttributeValue("name", b.getKey()).first();
				if ( e == null )
					continue;
				String a = e.attr("value");
				e.attr("value",a.replace("java"+b.getValue().getCode(), (v!=null?v:"") ));
				// add page id to name attribute
				e.attr("name","["+this.pageId+"]."+e.attr("name"));
				// special case for select option: compute his content
				if ( e.tagName().equalsIgnoreCase("option") ) {
					if ( repeat > 1 )
						this.beans.get(e.text()).resetValue(Context);
					String c = (String)this.beans.get(e.text()).execute(Context, vars);
					e.text(c);
					e.removeAttr("name");
				}
			}
			
			clone.append(item.html());
			num++;
		}
		// ### Compute all own html attributes
		Iterator<Entry<String,AttributeExpr>> attrs = this.html_attrs.entrySet().iterator();
		while (attrs.hasNext()) {
			Entry<String,AttributeExpr> en = attrs.next();
			AttributeExpr b = en.getValue();
			if ( b.isOwn ) {
				String a = clone.attr(b.name);
				clone.attr(b.name,a.replace("java"+b.expr.getCode(), (String)b.expr.execute(Context, vars)));
				clone.removeAttr(tmp_attr_id);
			}
		}
		// delete jop_ attribute (exclude jop_id) from dom and then add page id into jop_id*/
		this.cleanDomFromAttribute(clone);
		// add page id into jop_id with index
		clone.attr(JopAttribute.JOP_ATTR_ID,"["+this.pageId+"]."+this.id+(index>0?"+"+index:""));
		// TODO: gestire eccezzione esecuzione espressioni
	}
	// Parsing Dom Element to search and build beans, attributes and form input
	//
	//
	private void parse(WebAppContext Context) throws DomException {
		BeanMonitoring mon = Context.getBeanMonitor(); // get bean monitor
		// Get and process attributes of this block
		this.parseAttributes(Context, this.domEl,mon,true);
		
		// Scan for element that is not inside another child block 
		Iterator<Element> elems = this.domEl.select(JOP_BEAN_TAG).iterator();
		elems = this.domEl.getAllElements().iterator();
		while (elems.hasNext() ) {
			Element el = elems.next();
			if ( this.checkIfParentBlockIsThis(el, false)) {
				// check if bean or attributes element
				if ( el.tag().getName().equalsIgnoreCase(JOP_BEAN_TAG) ) {
					// build expression and join the same
					PageExpression exp;
					String code = this.parseBean(el);
					if ( !this.exprs.containsKey(AbstractPageExpression.computeId(code)) ) {
						// for a new expression add it and register this block to those to refresh 
						exp = new SimplePageExpression(Context,code,this.vars_definition);
						this.exprs.put(exp.getId(), exp);
						mon.registerRefreshable(exp.getBeansList(), new JopId(this.pageId,this.id));
					} else
						exp = this.exprs.get(AbstractPageExpression.computeId(code));
					this.beans.put(exp.getId(),exp);
					el.attr("id",exp.getId());
				} else {
					// Get and process attributes of children block
					this.parseAttributes(Context, el,mon,false);
				}
			}
		}
		
		// Get and process value attribute of form tags (es. input)
		Elements grp = this.domEl.select(form_selector);
		if ( !this.domEl.tag().isFormSubmittable() ) // add self to the list if is not already present 
			grp.add(this.domEl); 
		elems = grp.iterator();
		while ( elems.hasNext() ) {
			Element el = elems.next();
			if ( el.tag().isFormSubmittable() ) {
				if ( this.checkIfParentBlockIsThis(el, true)) {
					this.computeFormTag(Context, el, mon);
				}
				// special case for select tag: scan child option for value attribute expression and content expression
				else if ( el.tagName().equalsIgnoreCase("select") ) {
					Iterator<Element> opt = el.select("option").iterator();
					String base_name = el.attr("name");
					int inx = 0;
					while ( opt.hasNext() ) {
						el = opt.next();
						el.attr("name",base_name+"_"+inx);
						this.computeFormTag(Context, el, mon);
						PageExpression exp;
						String code = this.parseBean(el);
						if ( !this.exprs.containsKey(AbstractPageExpression.computeId(code)) ) {
							// for a new expression add it and register this block to those to refresh 
							exp = new SimplePageExpression(Context,code,this.vars_definition);
							this.exprs.put(exp.getId(), exp);
							mon.registerRefreshable(exp.getBeansList(), new JopId(this.pageId,this.id));
						} else
							exp = this.exprs.get(AbstractPageExpression.computeId(code));
						this.beans.put(exp.getId(),exp);
						el.text(exp.getId());
						inx++;
					}
				}
			}
		}
	}
	// Parse page bean of the block to verify delimiter { } and than create one set of bean text  
	//
	//
	private String parseBean(Element element) throws DomException {
		// parse as java expression
		return this.parseJavaExpression("java"+element.text().trim());
	}
	// Parse attributes element to verify delimiter { }
	//
	//
	@SuppressWarnings("rawtypes")
	private void parseAttributes(WebAppContext context, Element el, BeanMonitoring mon, boolean isOwn) throws DomException {
		// scan all element's attributes excluding value and jop_id
		List<Attribute> lst = new ArrayList<Attribute>(el.attributes().asList());
		Collections.sort(lst,new AttributeComparator());
		Iterator<Attribute> attrs = lst.iterator();
		while (attrs.hasNext() ) {
			Attribute attr =  attrs.next();
			if ( !attr.getKey().equalsIgnoreCase(JopAttribute.JOP_ATTR_ID) && !attr.getKey().equalsIgnoreCase("value") ) {
				String a = attr.getValue();
				String bid = this.parseJavaExpression(a); 
				if ( bid != null || ( attr.getKey().toLowerCase().startsWith("jop_") && JopAttribute.Factory.isKnown(attr.getKey()) ) ) {
					if ( attr.getKey().toLowerCase().startsWith("jop_") ) {
						if ( isOwn ) {
							// block jop attribute
							JopAttribute ja = JopAttribute.Factory.create(context,this,el,attr.getKey(),(bid!=null?bid:a));
							this.attrs.add(ja);
							if ( ((AbstractJopAttribute)ja).getExpression() != null )
								mon.registerRefreshable(((AbstractJopAttribute)ja).getExpression().getBeansList(), new JopId(this.pageId,this.id));
						}
					} else {
						// html attribute: parse expression and verify if exist
						PageExpression exp;
						String code = this.parseJavaExpression(a); 
						if ( !this.exprs.containsKey(AbstractPageExpression.computeId(code)) ) {
							// for a new expression add it and register this block to those to refresh 
							exp = new SimplePageExpression(context,code,this.vars_definition);
							this.exprs.put(exp.getId(), exp);
							mon.registerRefreshable(exp.getBeansList(), new JopId(this.pageId,this.id));
						} else
							exp = this.exprs.get(AbstractPageExpression.computeId(code));
						// add attribute to map and identify element with temp id
						AttributeExpr ae = new AttributeExpr();
						ae.expr = exp;
						ae.name = attr.getKey();
						ae.isOwn = isOwn;
						String id = ""+this.auto_id_index++;
						this.html_attrs.put(id, ae);
						el.attr(tmp_attr_id,id);
					}
				}
			}
		}
	}
	// Parse expression check syntax and extract string of expression
	//
	//
	private String parseJavaExpression (String code) throws DomException {
		if ( !code.isEmpty() ) {
			if ( code.trim().indexOf("java"+JOP_EXPR_INI) >= 0 ) {
				if ( code.lastIndexOf(JOP_EXPR_END) > 0 ) {
					String bid = code.substring(code.indexOf(JOP_EXPR_INI),code.trim().lastIndexOf(JOP_EXPR_END)+1);
					return bid;
				} else
					throw new DomException(ErrorsDefine.JOP_EXPR_SYNTAX);
			}
		}
		return null;
	}
	// Check if parent job_id is of this block
	//
	//
	private boolean checkIfParentBlockIsThis ( Element el, boolean mustBeForm ) {
		Element p = el.parent(); 
		while ( p != null ) {
			// check if tag is in another child block 
			if ( !p.attr(JopAttribute.JOP_ATTR_ID).isEmpty() && (!mustBeForm || p.tagName().equalsIgnoreCase("form") ) ) {
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
	// Erase all jop attribute from DOM
	//
	//
	private void cleanDomFromAttribute(Element elem) {
		String attrs[] = JopAttribute.Factory.getNameList();
		for ( int ix=0; ix<attrs.length; ix++ )
			elem.removeAttr(attrs[ix]);

	}
	// compute expression for form tag
	//
	//
	private void computeFormTag(WebAppContext Context, Element el, BeanMonitoring mon) throws DomException {
		String a = el.attr("value");
		String code = this.parseJavaExpression(a); 
		if ( code != null ) {
			// create new expression and register this block to those to refresh 
			PageWriteExpression exp;

			if ( !this.exprs.containsKey(AbstractPageExpression.computeId(code)) ) {
				// for a new expression add it and register this block to those to refresh 
				exp = new SimplePageExpression(Context,code,this.vars_definition);
				this.exprs.put(exp.getId(), exp);
				mon.registerRefreshable(exp.getBeansList(), new JopId(this.pageId,this.id));
			} else
				exp = (PageWriteExpression)this.exprs.get(AbstractPageExpression.computeId(code));
			// if name attributes don't exist add it with auto index
			if ( !el.hasAttr("name") || el.attr("name").isEmpty() ) {
				el.attr("name",""+this.auto_id_index);
				auto_id_index++;
			}
			this.forms.put(el.attr("name"), exp);
		}
	}
	// Comparator for jop attribute name
	//
	//
	static private class AttributeComparator implements Comparator<Attribute> {
		static private final String[] jop_attrs = JopAttribute.Factory.getNameList();
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
