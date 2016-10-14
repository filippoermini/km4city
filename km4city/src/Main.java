import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import XMLDomain.Tree;
import km4city.Application.XMLParsing;

public class Main {

	final static Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		logger.info("Start computation");
		XMLParsing<Tree> xml = new XMLParsing<>(Tree.class);
		Tree tree = xml.DeserializeFromXML("sensor.xml");
		
	}

}
