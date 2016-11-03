package run;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.util.RDFCollections;

import Application.CommonValue;
import Application.RDFconnector;
import Application.RDFconnector.RepositoryManager;
import Application.TripleGenerator;
import Application.XMLParsing;
import XMLDomain.Tree;

public class DataSimulator {

	public static Logger logger; 
	private String xmlFile;
	private String startPath;
	private XMLParsing<Tree> xml;
	private Tree tree;
	private RepositoryManager rdf;
	
	
	public DataSimulator(String file, String start,String name){
		
		this.xmlFile = file;
		CommonValue.init(name);
		logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
		xml = new XMLParsing(Tree.class);
		tree = xml.DeserializeFromXML(xmlFile);
		rdf = RDFconnector.getInstance(tree.getClassIterationQuery().getServer());
		logger.info("Initialization parameters completed");
		this.startPath = tree.getFileInfo().getStartDirectory();
	}
	
	public void run(){
		
		logger.info("Start computation");
		TripleGenerator tripleGen = new TripleGenerator(tree,rdf);
		String triple = tripleGen.tripleRDF(tree.isStatefull());
		String jsonState = tripleGen.toJson();
				
		//generazione file e cartelle secondo lo scema predefinito
		String directoryPath = this.startPath;
		String path = directoryPath+DateTimeFormatter.ofPattern("yyyy_MM/dd/HH/mmss").withZone(ZoneOffset.systemDefault()).format(Instant.now()).toString();
		if(mkdir(path)){
			PrintWriter out = null;
			PrintWriter end = null;
			PrintWriter json = null;
			try {
				out = new PrintWriter(path+"/"+tree.getFileInfo().getFileName());
				out.print(triple);
				out.close();
				logger.info("File "+path+"/"+tree.getFileInfo().getFileName()+" creato");
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
			if(tree.isStatefull()){
				try {
					String jsonFileName = tree.getFileInfo().getFileName().substring(0, tree.getFileInfo().getFileName().length()-4)+".json";
					json = new PrintWriter(path+"/"+jsonFileName);
					json.print(jsonState);
					json.close();
					logger.info("File "+path+"/"+jsonFileName+" creato");
				} catch (FileNotFoundException e) {
					logger.error("Creating file .n3 error "+e.getMessage());
				}
			}
			logger.info("Computation completed");
			RDFconnector.closeAll();
		}
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
