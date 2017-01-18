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
	
	
	public DataSimulator(String file,String name){
		
		this.xmlFile = file;
		logger = Logger.getLogger(name);
		CommonValue.init(name);
		xml = new XMLParsing(Tree.class);
		tree = xml.DeserializeFromXML(xmlFile);
		logger.info("Initialization parameters completed");
		this.startPath = tree.getFileInfo().getStartDirectory();
	}
	
	public void run(){
		
		logger.info("Start computation");
		TripleGenerator tripleGen = new TripleGenerator(tree);
		String triple = tripleGen.tripleRDF(tree.isStatefull(),tree.getTypeId());
		String jsonState = tripleGen.toJson();
				
		//generazione file e cartelle secondo lo scema predefinito
		String directoryPath = this.startPath;
		String dirSeparator = File.separator;
		if((directoryPath.charAt(directoryPath.length()-1)) != dirSeparator.charAt(0))
			directoryPath += dirSeparator;
		String path = directoryPath+DateTimeFormatter.ofPattern("yyyy_MM/dd/HH/mmss").withZone(ZoneOffset.systemDefault()).format(Instant.now()).toString();
		if(mkdir(path)){
			PrintWriter out = null;
			PrintWriter end = null;
			PrintWriter json = null;
			try {
				out = new PrintWriter(path+dirSeparator+tree.getFileInfo().getFileName());
				out.print(triple);
				out.close();
				logger.info("File "+path+dirSeparator+tree.getFileInfo().getFileName()+" creato");
			} catch (FileNotFoundException e) {
				logger.error("Creating file .n3 error "+e.getMessage());
			}
			try {
				end = new PrintWriter(path+dirSeparator+"end.txt");
				end.print("end triplification");
				end.close();
				logger.info("File "+path+dirSeparator+"end.txt creato");
			} catch (FileNotFoundException e) {
				logger.error("Creating file end.txt error "+e.getMessage());
			}
			if(tree.isStatefull()){
				try {
					String jsonFileName = tree.getFileInfo().getFileName().substring(0, tree.getFileInfo().getFileName().length()-3)+".json";
					
					json = new PrintWriter(path+dirSeparator+jsonFileName);
					json.print(jsonState);
					json.close();
					logger.info("File "+path+dirSeparator+jsonFileName+" creato");
				} catch (FileNotFoundException e) {
					logger.error("Creating file .n3 error "+e.getMessage());
				}
			}
			logger.info("Computation completed");
			RDFconnector.closeAll();
		}
	}
	
	private static boolean mkdir(String dirPath){
		
		File theDir = new File(dirPath);
		theDir.setReadable(true, false);
		theDir.setExecutable(true, false);
		theDir.setWritable(true, false);
		if (!theDir.exists()) {
		    if (theDir.mkdirs()){
		    	logger.info("Directory created: "+dirPath);
		    }else{
		    	logger.error("Directory not created: "+dirPath);
		    }
		}
		return true;
	}
}
