/**
 * 
 */
package km4city.Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.thoughtworks.xstream.XStream;

/**
 * @author filippo.ermini
 *
 */
public class XMLParsing<T> {
	
	final Class<T> typeParameterClass;
	
	public XMLParsing(Class<T> typeParameterClass){
		this.typeParameterClass = typeParameterClass;
	}

	public T DeserializeFromXML(String xmlPath){
		T object=null;
		try {

			File file = new File(xmlPath);
			JAXBContext jaxbContext = JAXBContext.newInstance(typeParameterClass);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			object = (T) jaxbUnmarshaller.unmarshal(file);

		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
		return object;
		
		
	}
}
