import XMLDomain.Tree;
import km4city.Application.XMLParsing;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		XMLParsing<Tree> xml = new XMLParsing<>(Tree.class);
		Tree tree = xml.DeserializeFromXML("sensor.xml");
	}

}
