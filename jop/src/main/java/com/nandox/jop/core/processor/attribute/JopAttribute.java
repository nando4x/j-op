package com.nandox.jop.core.processor.attribute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jsoup.nodes.Element;
import com.nandox.jop.core.processor.PageExpression;
import com.nandox.jop.core.context.WebAppContext;
import com.nandox.libraries.utils.Reflection;


/**
 * Attribute of page block, it create own bean based on class name specified in attribute list (ATTR_LIST).<br>
 * !!! REMBER...WHEN ADD NEW ATTRIBUTE ADD ITS ITEM IN ATTR_LIST !!! 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    BlockAttribute.java
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
	 * @param	  Dom element dom of the block
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public RETURN_ACTION preRender(Element Dom);
	
	public PageExpression getExpression();
	public static class Util {
		private static Map<String,Class<JopAttribute>> attrs;
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
		//
		//
		//
		private static void init(){
			if ( attrs == null ) {
				try {
					Iterator<Class<?>> l = Reflection.getClassesForPackage(JopAttribute.class.getPackage(), JopAttribute.class,true).iterator();
					attrs = new HashMap<String,Class<JopAttribute>>();
					while (l.hasNext()) {
						@SuppressWarnings("unchecked")
						Class<JopAttribute> c = (Class<JopAttribute>)l.next();
						if ( c.isAnnotationPresent(JopCoreAttribute.class) ) {
							try {
								attrs.put("jop_"+c.getAnnotation(JopCoreAttribute.class).name(), c);
							} catch(Exception e) {
								// TODO: manage error on create service manager
							}
						}
					}
				} catch (Exception e) {
					// TODO: manage error on scan package  
				}
			}
		}
	}
}
