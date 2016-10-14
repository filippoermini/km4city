/**
 * 
 */
package km4city.Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

/**
 * @author filippo.ermini
 *
 */
public class XMLParsing<T> {
	
	final Class<T> typeParameterClass;
	final static Logger logger = Logger.getLogger(XMLParsing.class);
	
	public XMLParsing(Class<T> typeParameterClass){
		this.typeParameterClass = typeParameterClass;
	}
	public boolean SerializeToXML(String xmlPath, T object){
		boolean results = false;
		try {

				File file = new File(xmlPath);
				JAXBContext jaxbContext = JAXBContext.newInstance(typeParameterClass);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	
				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(object, file);
				logger.info("Serialize "+typeParameterClass+" object into file "+xmlPath);
				results = true;

		    } catch (JAXBException e) {
		    	logger.error("Serialize Error - JAXB exception: "+e.getMessage());
		    }
		return results;

	}
	public T DeserializeFromXML(String xmlPath){
		T object=null;
		try {

			File file = new File(xmlPath);
			JAXBContext jaxbContext = JAXBContext.newInstance(typeParameterClass);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			object = (T) jaxbUnmarshaller.unmarshal(file);
			logger.info("Deserialize file "+xmlPath+" into "+typeParameterClass+" object");

		  } catch (JAXBException e) {
			logger.error("Deserialize error - JAXB excepition: "+e.getMessage());
		  }
		return object;
		
		
	}
}
