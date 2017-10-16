/**
 * 
 */
package com.nandox.jop.core.processor.expression;

import java.util.Map;

import com.nandox.jop.core.context.WebAppContext;

/**
 * Represent write page expression.<p>
 * An expression that can also manage a value to write in some bean
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    PageWriteExpression.java
 * 
 * @date      24 gen 2016 - 24 gen 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public interface PageWriteExpression extends PageExpression {

	/** 
	 * @param Value	submission value to write to 
	 * @param NativeValue	native html submission value to write to 
	 * @see com.nandox.jop.core.processor.PageExpression#Execute(com.nandox.jop.core.context.WebAppContext,Map<String,Object>)
	 */
	Object execute(WebAppContext Context, Object Value, String NativeValue, Map<String,Object> Vars);

}
