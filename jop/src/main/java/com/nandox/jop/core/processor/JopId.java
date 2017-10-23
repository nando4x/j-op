package com.nandox.jop.core.processor;

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
	private String index;
	/**
	 * Constructor
	 * @param	  CompositId identifier composit ([page].id#index)
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception ParseException if malformed (must be [page path].idblock#index)
	 * @return
	 */
	public JopId(String CompositId) throws ParseException {
		int ini = CompositId.indexOf("[");
		int end = CompositId.indexOf("].");
		if ( ini >= 0 && end > ini ) {
			this.page = CompositId.substring(ini+1, end);
			this.Id = CompositId.substring(end+2);
			if ( this.Id.indexOf("#") > 0 ) {
				try {
					Integer.valueOf(this.Id.substring(this.Id.indexOf("#")+1));
					this.index = this.Id.substring(this.Id.indexOf("#")+1);
					this.Id = this.Id.substring(0, this.Id.indexOf("#"));
				} catch (Exception e) {}
			}
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
	 * Constructor
	 * @param	  PageId page identifier
	 * @param	  Id block identifier
	 * @param	  Index optional block index
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public JopId(String PageId, String Id, String Index) {
		this.page = PageId;
		this.Id = Id;
		this.index = Index;
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
	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * Compute and return the composition identifier
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public String composite() {
		return "["+this.page+"]."+this.Id +(this.index!=null&&!this.index.isEmpty()?"#"+this.index:"");
	}
}
