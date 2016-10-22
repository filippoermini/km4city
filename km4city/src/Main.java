import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import XMLDomain.Tree;
import km4city.Application.TripleGenerator;
import km4city.Application.XMLParsing;

public class Main{
	
	final static Logger logger = Logger.getLogger(Main.class);
	public static void main(String[] args){
		
		logger.info("Start computation");
		XMLParsing<Tree> xml = new XMLParsing<>(Tree.class);
		Tree tree = xml.DeserializeFromXML(args[0]);
		
		TripleGenerator tripleGen = new TripleGenerator("", tree);
		String triple = tripleGen.tripleRDF();
		
		
		//generazione file e cartelle secondo lo scema predefinito
		String directoryPath = args[1];
		String path = directoryPath+DateTimeFormatter.ofPattern("yyyy_MM/dd/HH/mmss").withZone(ZoneOffset.systemDefault()).format(Instant.now()).toString();
		if(mkdir(path)){
			PrintWriter out = null;
			try {
				out = new PrintWriter(path+"/"+tree.getFileName());
				out.print(triple);
				out.close();
				logger.info("File "+path+"/"+tree.getFileName()+" creato");
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
