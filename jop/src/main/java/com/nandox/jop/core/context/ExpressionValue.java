package com.nandox.jop.core.context;

/**
 * Expression context runtime value
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ExpressionValue.java
 * 
 * @date      11 set 2017 - 11 set 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class ExpressionValue<E extends Object> {
	/** expression identifier */
	protected String Id;
	private E value;	// cached value returned from expression invoker
	
	/**
	 * Costruttore
	 * @param	  Id expression identifier 
	 * @date      11 set 2017 - 11 set 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public ExpressionValue(String Id) {
		this.Id = Id;
	}

	/**
	 * @return the value
	 */
	public E getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(E value) {
		this.value = value;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return Id;
	}
}
