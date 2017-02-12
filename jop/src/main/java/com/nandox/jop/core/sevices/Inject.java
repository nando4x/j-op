package com.nandox.jop.core.sevices;

import java.util.Map;
import java.util.ArrayList;
import com.nandox.jop.core.dispatcher.Dispatcher;
import com.nandox.jop.core.processor.JopId;

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
			JopId id = this.GetJopIdFromParams(Params);
			Dsp.processPageBlockFormAction(id,Params);
			ServiceJSDataBlock d = new ServiceJSDataBlock();
			d.setType("block");
			d.setNum(1);
			ServiceJSDataBlock.Block b = new ServiceJSDataBlock.Block();
			b.id = "1";
			b.value = "<div>assasasas <span>ddsdd</span></div>";
			d.setBlock(new ArrayList<ServiceJSDataBlock.Block>());
			d.getBlock().add(b);
			ServiceJSResponse r = new ServiceJSResponse(ServiceJSResponse.Format.XML,d);
			return r;
		}
		// TODO: manage error command unknown
		return null;
	}
}
