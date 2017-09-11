package com.nandox.jop.core.processor;

import java.util.Map;

import com.nandox.jop.core.context.WebAppContext;
import com.nandox.jop.core.context.ExpressionValue;

/**
 * Represent generic page expression.<br>
 * An expression is java (or other) language expression used in page into html attribute or jbean tag
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    PageExpression.java
 * 
 * @date      17 set 2016 - 17 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public interface PageExpression {

	/**
	 * @return the expression identifier
	 */
	String getId();
	/**
	 * @return the expression code
	 */
	String getCode();

	/**
	 * Execute expression on specific context.<br>
	 * The context is used to get beans instance if present in expression
	 * @param	  Context	Application context
	 * @param	  Vars 	list of block variables instance [variable name, variable value instanced]
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  result of method in specific type  
	 */
	Object execute(WebAppContext Context, Map<String,Object> Vars);

	/**
	 * Reset expression return value to null.<br>
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	    
	 */
	void resetValue();
	
	/**
	 * Return the list of bean referenced into expression
	 * @date      01 feb 2017 - 01 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  array of bean name
	 */
	String[] getBeansList();

	/**
	 * Instance context value
	 * @param	  Id expression identifier
	 * @date      01 feb 2017 - 01 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  ExpressionValue instance
	 */
	ExpressionValue<?> instanceValue(String Id);
}