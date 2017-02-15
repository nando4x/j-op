package com.nandox.jop.core.sevices;

import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Iterator;
import com.nandox.jop.core.dispatcher.Dispatcher;
import com.nandox.jop.core.processor.JopId;
import com.nandox.jop.core.processor.PageBlock;

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
			JopId id = this.getJopIdFromParams(Params);
			Dsp.processPageBlockFormAction(id,Params);
			Iterator<Entry<JopId,String>> lst = Dsp.renderPageBlockToBeRefresh(id.getPage()).entrySet().iterator();
			ServiceJSDataBlock d = new ServiceJSDataBlock();
			d.setBlock(new ArrayList<ServiceJSDataBlock.Block>());
			int num = 0;
			while ( lst.hasNext() ) {
				Entry<JopId,String> e = lst.next();
				ServiceJSDataBlock.Block b = new ServiceJSDataBlock.Block();
				b.setId(e.getKey().composite());
				b.setValue(e.getValue());
				d.getBlock().add(b);
				num++;		
			}
			d.setNum(num);
			d.setType("block");
			ServiceJSResponse r = new ServiceJSResponse(ServiceJSResponse.Format.XML,d);
			return r;
		}
		// TODO: manage error command unknown
		return null;
	}
}
