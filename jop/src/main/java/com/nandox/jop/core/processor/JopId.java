package com.nandox.jop.core.processor;

import com.nandox.jop.core.ErrorsDefine;

/**
 * Class that represents an Jop Identifier.<p> 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    JopId.java
 * 
 * @date      24 gen 2017 - 24 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class JopId {
	private String page;
	private String Id;
	/**
	 * Constructor
	 * @param	  CompositId identifier composit ([page].id)
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception ParseException if malformed (must be [page path].idblock)
	 * @return
	 */
	public JopId(String CompositId) throws ParseException {
		int ini = CompositId.indexOf("[");
		int end = CompositId.indexOf("].");
		if ( ini >= 0 && end > ini ) {
			this.page = CompositId.substring(ini+1, end);
			this.Id = CompositId.substring(end+2);
		}// else
			//throw new ParseException(ErrorsDefine.JOP_ID_MALFORMED);
	}
	/**
	 * Constructor
	 * @param	  PageId page identifier
	 * @param	  Id block identifier
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public JopId(String PageId, String Id) {
		this.page = PageId;
		this.Id = Id;
	}
	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return Id;
	}
	public String composite() {
		return "["+this.page+"]."+this.Id;
	}
}
