package com.nandox.jop.core.processor.attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;
import org.jsoup.nodes.Element;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.libraries.utils.Reflection;


/**
 * Attribute of page block.<br>
 * Use this interface to implement every jop attribute with preRender and postRender action on rendering of page block,<br>
 * every action can return an RETURN_ACTION to pilot the rest of rendering.<br>
 * There is also a subclass Factory to instance an attribute.<br>
 * <br>
 * To implement an attribute have to create a Class that implements JopAttribute and extend AbstractJopAttribute.<br>
 * !!IMPORTANT!! on the class define annotation JopCoreAttribute with attribute name without prefix (jop_) 
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
		CONTINUE,
		NOTRENDER
	}

	/**
	 * Pre rendering action
	 * @param	  Context	Application context
	 * @param	  Dom element dom of the block
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public RETURN_ACTION preRender(WebAppContext Context, Element Dom);
	/**
	 * Post rendering action
		 * @param	  Context	Application context
	 * @param	  Dom element dom of the block
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public RETURN_ACTION postRender(WebAppContext Context, Element Dom);
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
		 * @param	  Name attribute name
		 * @date      04 ott 2016 - 04 ott 2016
		 * @author    Fernando Costantino
		 * @revisor   Fernando Costantino
		 * @exception 
		 */
		public static JopAttribute create(WebAppContext Context ,String Name, String Value) {
			init();
			Class<?> c = attrs.get(Name);
			try {
				return (JopAttribute)c.getDeclaredConstructor(WebAppContext.class,String.class,String.class).newInstance(Context,Name,Value);
			} catch (Exception e) {
				// TODO: manage instantiate error
				return null;
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
							} catch(Exception e) {
								// TODO: manage error on create service manager
							}
						}
					}
					// Sort on priority
					ArrayList<Entry<String,Class<JopAttribute>>> lst = new ArrayList<Entry<String,Class<JopAttribute>>>(map.entrySet());
					Collections.sort(lst, new Comparator<Entry<String,Class<JopAttribute>>>() {
						public int compare(Entry<String,Class<JopAttribute>> e1, Entry<String,Class<JopAttribute>> e2) {
							int i1 = e1.getValue().getAnnotation(JopCoreAttribute.class).priority();
							int i2 = e2.getValue().getAnnotation(JopCoreAttribute.class).priority();
							return (i1>i2?1:(i1==i2)?0:-1);
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
