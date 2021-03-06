package com.nandox.jop.core.sevices;

import java.util.Map;
import com.nandox.jop.core.processor.JopId;

/**
 * Abstract general javascript service.<p>
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
	 * Extract JopId from parameters Jop.jopId
	 * @param	  Params parameter map received from url request query
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected JopId getJopIdFromParams (Map<String,String[]> Params) throws Exception {
		String p[] = Params.get(PARAMS_JOPID);
		if ( p!= null && p.length > 0 )
			return new JopId(p[0]);
		return null;
	}
}
