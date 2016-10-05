
/**
 * 
 */
package com.nandox.jop.core.processor;

/**
 * @author ee38938
 *
 */
public class BlockAttribute {
	/** */
	protected String name;
	/** */
	protected PageBean bean;
	/**
	 * 
	 */
	public BlockAttribute(String Name, String BeanId) {
		this.name = Name;
		this.bean = new PageBean(BeanId);
	}
	
	
}
