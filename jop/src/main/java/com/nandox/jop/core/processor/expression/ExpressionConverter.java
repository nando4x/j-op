package com.nandox.jop.core.processor.expression;

import com.nandox.jop.core.Converter;
import com.nandox.jop.core.context.WebAppContext;

/**
 * Internal expression converter.<p>
 * Is called before expression execution if action phase or post execution if rendering phase.<br>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ExpressionConverter.java
 * 
 * @date      15 ott 2017 - 15 ott 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class ExpressionConverter {
	private Converter converter;	// specific converter
	private Object supportData;		// support data

	/**
	 * Convert string native submitted value and return value as Object
	 * @param	  Converter		specific converter of user implementation (specified by jop_converter attribute)
	 * @param	  SupportData	support data (specified by jop_support_data attribute)
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 */
	public ExpressionConverter(Converter Converter, Object SupportData) {
		this.converter = Converter;
		this.supportData = SupportData;
	}
	/**
	 * Conversion before expression execution
	 * @param	  Context	Application context
	 * @param	  Value		native html submitted value
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  converted value
	 */
	public Object callInputConverter(WebAppContext Context, String Value) {
		return this.converter.asObject(Context, this.supportData, Value);
	}
	/**
	 * Conversion after expression execution
	 * @param	  Context	Application context
	 * @param	  Value		expression result value
	 * @date      04 ott 2016 - 04 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  converted value
	 */
	public String callOutputConverter(WebAppContext Context, Object Value) {
		return this.converter.asString(Context, this.supportData, Value);
	}
}
