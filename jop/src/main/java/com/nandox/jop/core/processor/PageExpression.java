package com.nandox.jop.core.processor;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Represent generic page expression.<br>
 * An expression is java (or other) language expression used in page into attribute or jbean tag
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
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	  result of method in specific type  
	 */
	Object Execute(WebAppContext Context);

	/**
	 * Reset bean value to null.<br>
	 * @date      07 ott 2016 - 07 ott 2016
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return	    
	 */
	void ResetValue();

}