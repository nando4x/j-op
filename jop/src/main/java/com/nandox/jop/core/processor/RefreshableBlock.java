package com.nandox.jop.core.processor;

/**
 * Represent a page block refreshable when related monitored bean change.<p> 
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    RefreshableBlock.java
 * 
 * @date      02 feb 2017 - 02 feb 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */

public class RefreshableBlock {
	private boolean toBeRefresh;	// flag to indicate that block is to refresh

	/**
	 * Set that block is to be refreshed
	 * @date      02 feb 2017 - 02 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void setToBeRefreshed() {
		this.toBeRefresh = true;
	}
	/**
	 * Reset that block is to be refreshed
	 * @date      02 feb 2017 - 02 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void resetToBeRefreshed() {
		this.toBeRefresh = false;
	}
	/**
	 * Check if block is to be refreshed
	 * @date      02 feb 2017 - 02 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public boolean getToBeRefresh() {
		return this.toBeRefresh;
	}
}
