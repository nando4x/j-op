package com.nandox.jop.core.processor;

/**
 * Descrizione classe
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

public interface RefreshableBlock {

	/**
	 * Set that block is to be refreshed
	 * @date      02 feb 2017 - 02 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public void SetToBeRefreshed();
	/**
	 * Check if block is to be refreshed
	 * @date      02 feb 2017 - 02 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	public boolean GetToBeRefresh();
}
