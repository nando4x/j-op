package com.nandox.jop.core.sevices;

/**
 * Javascript Service response.<p>
 * The response can be different format: xml, json.<br> 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    ServiceJSResponse.java
 * 
 * @date      10 feb 2017 - 10 feb 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class ServiceJSResponse {
	/** Response format */
	public enum Format {
		XML
	}
	private Format format;
	private Object data;
	/**
	 * Costruttore
	 * @date      10 feb 2017 - 10 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception
	 */
	public ServiceJSResponse(Format Frm, Object Data) {
		this.format = Frm;
		this.data = Data;
	}
	/**
	 * @return the format
	 */
	public Format getFormat() {
		return format;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
}
