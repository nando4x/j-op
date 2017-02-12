package com.nandox.jop.core.sevices;

import java.util.Map;
import javax.xml.bind.JAXBException;
import com.nandox.jop.core.processor.JopId;
import com.nandox.libraries.utils.xml.GenerateXmlWithCDATA;

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
			return new GenerateXmlWithCDATA().Generate(data);
			/*StringWriter sw = new StringWriter();
	        JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
	        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

	        // output pretty printed
	        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        jaxbMarshaller.setProperty(CharacterEscapeHandler.class.getName(),
	                new CharacterEscapeHandler() {
	                    @Override
	                    public void escape(char[] ac, int i, int j, boolean flag,
	                            Writer writer) throws IOException {
	                        writer.write(ac, i, j);
	                    }
	                });
	        jaxbMarshaller.marshal(data, sw);
	        return sw.toString();*/
	    } catch (JAXBException e) {
	        e.printStackTrace();
	    }
		return null;
	}
}
