import org.apache.log4j.Logger;

import XMLDomain.Tree;
import km4city.Application.TripleGenerator;
import km4city.Application.XMLParsing;

public class Main{
	
	final static Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args){
		
		logger.info("Start computation");
		XMLParsing<Tree> xml = new XMLParsing<>(Tree.class);
		Tree tree = xml.DeserializeFromXML("sensor.xml");
		TripleGenerator tripleGen = new TripleGenerator("", tree);
		tripleGen.generateValue();
		
	}
}
