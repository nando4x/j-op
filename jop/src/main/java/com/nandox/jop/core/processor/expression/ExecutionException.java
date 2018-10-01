package com.nandox.jop.core.processor.expression;

/**
 * Execution exception thrown in case of expression runtime error.<p>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    DomException.java
 * 
 * @date      30 set 2016 - 30 set 2016
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class ExecutionException extends RuntimeException {
	private static final long serialVersionUID = -1107252320598375341L;

	private String expressionCode;

	public ExecutionException(String msg) {
		super(msg);
	}
	/**
	 * @return the expressionCode
	 */
	public String getExpressionCode() {
		return expressionCode;
	}

	/**
	 * @param expressionCode the expressionCode to set
	 */
	public void setExpressionCode(String expressionCode) {
		this.expressionCode = expressionCode;
	}
}
;