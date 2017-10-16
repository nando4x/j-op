package com.nandox.jop.core;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Converter interface.<p>
 * When use jop_converter attribute you have to define a converter class that must implements this interface.<br>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Converter.java
 * 
 * @date      15 ott 2017 - 15 ott 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public interface Converter {

	/**
	 * Convert native submitted value and return value as Object
	 * @param	  Context	Application context
	 * @param	  Data		result of jop_converter_data attribute
	 * @param	  Value		native html submitted value
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  converted value
	 */
	public Object asObject(WebAppContext Context, Object Data, String Value);
}
