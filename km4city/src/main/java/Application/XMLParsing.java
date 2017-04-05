package Application;

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
	public static Logger logger;  
	
	public XMLParsing(Class<T> typeParameterClass){
		this.typeParameterClass = typeParameterClass;
		logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
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
		    	logger.error("Serialize Error - JAXB exception: "+e.getMessage()+e.getCause() );
		    	logger.error("Process interrupt");
				System.exit(-1);
		    }
		return results;

	}
	public T DeserializeFromXML(String xmlPath) {
		T object=null;
		try {

			File file = new File(xmlPath);
			JAXBContext jaxbContext = JAXBContext.newInstance(typeParameterClass);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			object = (T) jaxbUnmarshaller.unmarshal(file);
			logger.info("Deserialize file "+xmlPath+" into "+typeParameterClass+" object");
			
		  } catch (JAXBException e) {
			String msg = e.getLocalizedMessage()==null?e.getLinkedException().getLocalizedMessage():e.getLocalizedMessage();
			logger.error("Deserialize error - JAXB excepition: "+msg);
			logger.error("Process interrupt");
			System.exit(-1);
		  } 
		return object;
		
		
	}
}
