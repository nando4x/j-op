package com.nandox.jop.core.sevices;

import java.util.Map;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import com.nandox.jop.core.processor.JopId;

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
	 * Extract JopId from parameters Jop.jopId
	 * @date      24 gen 2017 - 24 gen 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception 
	 * @return
	 */
	protected JopId GetJopIdFromParams (Map<String,String[]> Params) {
		String p[] = Params.get(PARAMS_JOPID);
		if ( p!= null && p.length > 0 )
			return new JopId(p[0]);
		return null;
	}
	
	protected String transformToXML(ServiceJSDataBlock data) {
		try {
			StringWriter sw = new StringWriter();
	        JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
	        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

	        // output pretty printed
	        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        jaxbMarshaller.marshal(data, sw);
	        return sw.toString();
	      } catch (JAXBException e) {
	        e.printStackTrace();
	      }
		return null;
	}
}
