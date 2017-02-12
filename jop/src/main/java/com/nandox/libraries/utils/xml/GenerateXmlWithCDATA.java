package com.nandox.libraries.utils.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;

/**
 * Class to generate XML from a JAXB object with CDATA
 * 
 * @project   Jop (Java One Page)
 * 
 * @module    XmlFromClass.java
 * 
 * @date      10 feb 2017 - 10 feb 2017
 * 
 * @author    Fernando Costantino
 * 
 * @revisor   Fernando Costantino
 */
@SuppressWarnings("restriction")
public class GenerateXmlWithCDATA {

	/**
	 * Generate XML String from object that have to use JAXB annotation
	 * @date      10 feb 2017 - 10 feb 2017
	 * @author    Fernando Costantino
	 * @revisor   Fernando Costantino
	 * @exception JAXBException
	 * @return
	 */
	public String Generate(Object Data) throws JAXBException {
		try {
			StringWriter sw = new StringWriter();
	        JAXBContext jc = JAXBContext.newInstance(Data.getClass());
	        Marshaller m = jc.createMarshaller();

	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        m.setProperty(CharacterEscapeHandler.class.getName(),
                new CharacterEscapeHandler() {
                    @Override
                    public void escape(char[] ac, int i, int j, boolean flag,
                            Writer writer) throws IOException {
                        writer.write(ac, i, j);
                    }
                }
	        );
	        m.marshal(Data, sw);
	        return sw.toString();
		} catch (JAXBException e) {
			throw e;
		}
	}
	/**
	 * XmlAdapter for CDATA, use with @XmlJavaTypeAdapter:<br> 
	 * <br>
	 * @XmlJavaTypeAdapter(com.nandox.libraries.utils.xml.GenerateXmlWithCDATA.AdapterCDATA.class)
	 * 
	 * @project   Jop (Java One Page)
	 * 
	 * @module    AdapterCDATA.java
	 * 
	 * @date      10 feb 2017 - 10 feb 2017
	 * 
	 * @author    Fernando Costantino
	 * 
	 * @revisor   Fernando Costantino
	 */
    public static final class AdapterCDATA extends XmlAdapter<String, String> {
		@Override
        public String marshal(String arg0) throws Exception {
            return "<![CDATA[" + arg0 + "]]>";
        }
        @Override
        public String unmarshal(String arg0) throws Exception {
            return arg0;
        }

    }
}
