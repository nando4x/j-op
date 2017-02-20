package com.nandox.jop.core.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.nandox.jop.core.sevices.ServiceJSManager;
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
	/** Rendered flag attribute: jop_rendered */
	public static final String JOP_ATTR_RENDERED = "jop_rendered";

	/**
	 * Return name of attribute
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception DomException if attribute name not found or syntax error
	 */
	public static String preRender();
	
	public static class Util {
		private Map<String,Class<JopAttribute>> attrs;
		public void init(){
			try {
				Iterator<Class<?>> l = Reflection.getClassesForPackage(this.getClass().getPackage(), JopAttribute.class,true).iterator();
				attrs = new HashMap<String,Class<JopAttribute>>();
				while (l.hasNext()) {
					try {
						attrs.put(s.getIdentifier(), l.next());
					} catch(Exception e) {
						// TODO: manage error on create service manager
					}
				}
			} catch (Exception e) {
				// TODO: manage error on scan package  
			}
		}
	}
}
