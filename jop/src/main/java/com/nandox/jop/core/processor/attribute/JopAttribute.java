package com.nandox.jop.core.processor.attribute;

import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;
import org.jsoup.nodes.Element;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.processor.PageBlock;
import com.nandox.jop.core.processor.expression.PageExpression;
import com.nandox.jop.core.processor.DomException;
import com.nandox.libraries.utils.Reflection;


/**
 * Attribute of page block.<p>
 * Use this interface to implement every jop attribute, on default is implemented from AbstractJopAttribute.<br>
 * There is also a subclass Factory to instance an attribute.<br>
 * <br>
 * To implement an attribute have to create a Class that implements JopAttributeRendering or JopAttributeAction and extend AbstractJopAttribute.<br>
 * <b>!!IMPORTANT!! on the class define annotation JopCoreAttribute with attribute name without prefix (jop_)</b> 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    JopAttribute.java
 * 
 * @date      04 ott 2016 - 04 ott 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public interface JopAttribute {
	/** Identification attribute: jop_id */
	public static final String JOP_ATTR_ID = "jop_id";
	/** Return action on block render phase */
	public enum RETURN_ACTION {
		/** continue rendering */
		CONTINUE,
		/** stop rendering: use result as rendering contents */
		NOTRENDER,
		/** action value converted */
		CONVERTER
	}
	/**
	 * Return expression 
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public PageExpression getExpression();
	/**
	 * Setting variables used into page expression and defined with attribute
	 * @param	  Context	Application context
	 * @param	  Vars		variables map
	 * @param	  Index		iterator index
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public void setVariables(WebAppContext Context, Map<String,Object> Vars, int Index);
	/**
	 * Response class of an attribute
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public static class Response {
		private RETURN_ACTION action;
		private int repater_num;
		private Object result;
		
		public Response(RETURN_ACTION Action) {
			this.action = Action;
			this.repater_num = 1;
			this.result = "";
		}
		public Response(RETURN_ACTION Action, int Num) {
			this(Action);
			this.repater_num = Num;
			this.result = "";
		}
		public Response(RETURN_ACTION Action, Object Result) {
			this(Action);
			this.repater_num = 1;
			this.result = Result;
		}
		/**
		 * @return the action
		 */
		public RETURN_ACTION getAction() {
			return action;
		}
		/**
		 * @return the repater_num
		 */
		public int getRepater_num() {
			return repater_num;
		}
		/**
		 * @return the result
		 */
		public Object getResult() {
			return result;
		}
	}
	/**
	 * Factory class to instance attribute
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 */
	public static class Factory {
		private static Map<String,Class<JopAttribute>> attrs; // map of all attributes
		/**
		 * Instance an attribute
		 * @param	  Context	Application context
		 * @param	  Block		Page block
		 * @param	  Node		DOM node
		 * @param	  Name		Attribute name
		 * @param	  Value		Attribute value or expression
		 * @date      04 ott 2016 - 04 ott 2016
		 * @author    Fernando Costantino
		 * @revisor   Fernando Costantino
		 * @exception 
		 */
		public static JopAttribute create(WebAppContext Context ,PageBlock Block, Element Node, String Name, String Value) throws DomException {
			init();
			Class<?> c = attrs.get(Name);
			try {
				return (JopAttribute)c.getDeclaredConstructor(WebAppContext.class,PageBlock.class,Element.class,String.class).newInstance(Context,Block,Node,Value);
			} catch (Exception e) {
				// TODO: manage instantiate error
				//return null;
				throw new DomException(e.getCause().getMessage());
			}
		}
		/**
		 * Return list with all attributes name
		 * @date      04 ott 2016 - 04 ott 2016
		 * @author    Fernando Costantino
		 * @revisor   Fernando Costantino
		 * @exception
		 * @return    attributes name array
		 */
		public static String[] getNameList() {
			init();
			return attrs.keySet().toArray(new String[0]);
		}
		/**
		 * Check if an known and managed attribute (nested is excluded)
		 * @param	  Name Atttribute name
		 * @date      04 ott 2016 - 04 ott 2016
		 * @author    Fernando Costantino
		 * @revisor   Fernando Costantino
		 * @exception
		 * @return    attributes name array
		 */
		public static boolean isKnown(String Name) {
			init();
			return (attrs.containsKey(Name) && attrs.get(Name)!=null);
		}
		// initialization of map: scan package to find all classes that implement JopAttribute and extend AbstractJopAttribute and have name with 
		// JopCoreAttribute annotation
		//
		private static void init(){
			if ( attrs == null ) {
				try {
					Iterator<Class<?>> l = Reflection.getClassesForPackage(JopAttribute.class.getPackage(), JopAttribute.class,true).iterator();
					Map<String,Class<JopAttribute>> map = new HashMap<String,Class<JopAttribute>>();
					while (l.hasNext()) {
						@SuppressWarnings("unchecked")
						Class<JopAttribute> c = (Class<JopAttribute>)l.next();
						if ( c.isAnnotationPresent(JopCoreAttribute.class) && AbstractJopAttribute.class.isAssignableFrom(c) ) {
							try {
								map.put("jop_"+c.getAnnotation(JopCoreAttribute.class).name(), c);
								StringTokenizer sk = new StringTokenizer(c.getAnnotation(JopCoreAttribute.class).nested(),",");
								while ( sk.hasMoreTokens()) {
									map.put("jop_"+sk.nextToken(), null);
								}
							} catch(Exception e) {
								// TODO: manage error on create service manager
							}
						}
					}
					// Sort on priority
					ArrayList<Entry<String,Class<JopAttribute>>> lst = new ArrayList<Entry<String,Class<JopAttribute>>>(map.entrySet());
					Collections.sort(lst, new Comparator<Entry<String,Class<JopAttribute>>>() {
						public int compare(Entry<String,Class<JopAttribute>> e1, Entry<String,Class<JopAttribute>> e2) {
							int i1 = this.getItem(e1);
							int i2 = this.getItem(e2);
							return (i1>i2?1:(i1==i2)?0:-1);
						}
						public int getItem(Entry<String,Class<JopAttribute>> item) {
							try {
								return item.getValue().getAnnotation(JopCoreAttribute.class).priority();
							} catch(Exception e) { return -1; }
						}
					});
					attrs = new HashMap<String,Class<JopAttribute>>();
					Iterator<Entry<String,Class<JopAttribute>>> i = lst.iterator();
					while(i.hasNext()) {
						Entry<String,Class<JopAttribute>> e = i.next();
						attrs.put(e.getKey(), e.getValue());
					}
				} catch (Exception e) {
					// TODO: manage error on scan package  
				}
			}
		}
	}
}
