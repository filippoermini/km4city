
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

import Application.RDFconnector;
import Application.TripleGenerator;
import Application.XMLParsing;
import XMLDomain.Tree; 



public class Main{
	
	final static Logger logger = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args){
		
		
		logger.info("Start computation");
		XMLParsing<Tree> xml = new XMLParsing(Tree.class);
		Tree tree = xml.DeserializeFromXML(args[0]);
		RDFconnector rdf = new RDFconnector(tree.getQueryInfo().getServer());
		
		TripleGenerator tripleGen = new TripleGenerator(tree,rdf);
		String triple = tripleGen.tripleRDF(tree.isStatefull());
		String jsonState = tripleGen.toJson();
				
		//generazione file e cartelle secondo lo scema predefinito
		String directoryPath = args[1];
		String path = directoryPath+DateTimeFormatter.ofPattern("yyyy_MM/dd/HH/mmss").withZone(ZoneOffset.systemDefault()).format(Instant.now()).toString();
		if(mkdir(path)){
			PrintWriter out = null;
			PrintWriter end = null;
			PrintWriter json = null;
			try {
				out = new PrintWriter(path+"/"+tree.getFileName());
				out.print(triple);
				out.close();
				logger.info("File "+path+"/"+tree.getFileName()+" creato");
			} catch (FileNotFoundException e) {
				logger.error("Creating file .n3 error "+e.getMessage());
			}
			try {
				end = new PrintWriter(path+"/"+"end.txt");
				end.print("end triplification");
				end.close();
				logger.info("File "+path+"/end.txt creato");
			} catch (FileNotFoundException e) {
				logger.error("Creating file end.txt error "+e.getMessage());
			}
			try {
				String jsonFileName = tree.getFileName().substring(0, tree.getFileName().length()-4)+".json";
				json = new PrintWriter(path+"/"+jsonFileName);
				json.print(jsonState);
				json.close();
				logger.info("File "+path+"/"+jsonFileName+" creato");
			} catch (FileNotFoundException e) {
				logger.error("Creating file .n3 error "+e.getMessage());
			}
		}
		
		//--------------------------------
		
		
	}
	
	private static boolean mkdir(String dirPath){
		
		File theDir = new File("./"+dirPath);
		if (!theDir.exists()) {
		    boolean result = false;
		    try{
		        theDir.mkdirs();
		        result = true;
		    } 
		    catch(SecurityException se){
		    	logger.error("Directory not created: "+dirPath);
		    }        
		    if(result) {    
		        logger.info("Directory created: "+dirPath);
		    }
		}
		return true;
	}
}
