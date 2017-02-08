package com.nandox.jop.core.sevices;

import java.util.Map;

import com.nandox.jop.core.dispatcher.Dispatcher;

/**
 * Implementation of inject service manager.<br>
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    Inject.java
 * 
 * @date      24 gen 2017 - 24 gen 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
public class Inject extends AbstractServiceJS implements ServiceJSManager {
	/** */
	protected String CMD_POSTBLOCK = "postblock";

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.sevices.ServiceJSManager#getIdentifier()
	 */
	public String getIdentifier() {
		return "inject";
	}

	/* (non-Javadoc)
	 * @see com.nandox.jop.core.sevices.ServiceJSManager#execute(java.lang.String, java.util.Map)
	 */
	public ServiceJSResponse execute(Dispatcher Dsp, String Cmd, Map<String, String[]> Params) {
		if ( Cmd.equals(CMD_POSTBLOCK) ) {
			String pageId = this.GetPageIdFromParams(Params);
			String blockId = this.GetBlockIdFromParams(Params);
		}
		// TODO: manage error command unknow
		return null;
	}

}
