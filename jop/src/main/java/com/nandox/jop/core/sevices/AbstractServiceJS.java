package com.nandox.jop.core.sevices;

import java.util.Map;

/**
 * Abstract javascript service manager.<br>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    AbstractServiceJS.java
 * 
 * @date      24 gen 2017 - 24 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public abstract class AbstractServiceJS {
	/** */
	protected static final String PARAMS_JOPID = "Jop.jopId";
	/**
	 * Extract page id from parameters Jop.jopId
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected String GetPageIdFromParams (Map<String,String[]> Params) {
		String p[] = Params.get(PARAMS_JOPID);
		if ( p!= null && p.length > 0 )
			return p[0];
		return null;
	}
	/**
	 * Extract page id from parameters Jop.jopId
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected String GetBlockIdFromParams (Map<String,String[]> Params) {
		String p[] = Params.get(PARAMS_JOPID);
		if ( p!= null && p.length > 0 )
			return p[0];
		return null;
	}
}
