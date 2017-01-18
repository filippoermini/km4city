package Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Application.RDFconnector.RepositoryManager;
import XMLDomain.Tree;
import genericInstance.IterationElement;
import genericInstance.IterationManager;
import genericInstance.KeyWordCommand;
import genericInstance.SimulationObject;
import jsonDomain.LoadedStates;
import jsonDomain.State;
import jsonDomain.States;

public class TripleGenerator {
	

	public class IterationObject{
		private HashMap<String, KeyWordCommand> keyWord;
		private LoadedStates loadedStates;
		private SimulationObject simulationObject;
		
		public IterationObject(Tree tree){
			keyWord = new HashMap<>();
			loadedStates = new LoadedStates();
			initKeyWord();
			loadStates(tree);
			
			this.simulationObject = new SimulationObject(tree);
		}

		public HashMap<String, KeyWordCommand> getKeyWord() {
			return keyWord;
		}

		public LoadedStates getStates() {
			return loadedStates;
		}

		public SimulationObject getSimulationObject() {
			return simulationObject;
		}
		
		private void initKeyWord(){
			this.keyWord.put("index", new KeyWordCommand() {
	            public Object runCommand() { return getIteration(); };
	        });
			this.keyWord.put("id", new KeyWordCommand() {
	            public Object runCommand() { return getId(); };
	        });
		}
		
		private void deepSearch(String startDir, String path,String filename) throws FileNotFoundException{
			ArrayList<String> dirList = new ArrayList<>();
			File file = new File(startDir);
			String[] directories = file.list(new FilenameFilter() {
			  @Override
			  public boolean accept(File current, String name) {
			    return new File(current, name).isDirectory();
			  }
			});
			if(directories.length>0){
				for(String el:directories){
					deepSearch(startDir+"/"+el,path+el,filename);			
				}
			}else{
				//sono nell'ultima cartella
				String filenameJson = filename.substring(0, filename.length()-3)+".json";
				File jsonFile = new File(startDir+"/"+filenameJson);
				if (jsonFile.exists()){
					//importo il file json
					Gson g = new GsonBuilder().setPrettyPrinting().create();
					FileReader jsonReader = new FileReader(jsonFile);
					ArrayList<State> previusState =  g.fromJson(jsonReader,new TypeToken<List<State>>(){}.getType());
					this.loadedStates.addElement(path, previusState);
				}
			}
			
		}
		
		private void loadStates(Tree tree){
			String startDir = tree.getFileInfo().getStartDirectory();
			String filename = tree.getFileInfo().getFileName();
			
			//acquisico la lista delle sottocartelle
			try {
				deepSearch(startDir,"",filename);
			} catch (FileNotFoundException e) {
				logger.error("File Json error "+e);
				logger.error("Process interrupt");
				System.exit(-1);
			}
			
		}
		
	}
	private RepositoryManager rdfEngine;
	
	private IterationManager itManager;
	private ArrayList<State> statesList;
	private IterationObject itObject;
	private int index;
	private String id;
	public static Logger logger; 
	
	
	
	
	
	public TripleGenerator(Tree tree){
		
		this.itObject = new IterationObject(tree);
		this.rdfEngine = RDFconnector.getInstance(tree.getInstanceIterationQuery().getServer());
		this.itManager = new IterationManager(tree.getIterationElement(),tree.getBaseUri(),this.itObject);
		statesList = new ArrayList<>();
		logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
		index = 0;
		
	}
	
	
	
	private IterationElement generateValue(String resId){
		//setto il valore id della classe root con il risultato della query
		IterationElement itElement = itManager.generateIterationElement(resId);
		return itElement;
	}
	
	public String tripleRDF(boolean stateful, String type){
		
		String triple = "";
		//eseguo la query e ciclo sui risultati 
		TupleQueryResult results = rdfEngine.SPARQLExecute(this.itObject.getSimulationObject().getQuery());
		while(results.hasNext()){
			BindingSet bindingSet = results.next();
			Value value = bindingSet.getValue("id");
			this.id = value.stringValue();
			logger.info("Processing record "+ ++index +" id extract: "+this.id);
			IterationElement itElement = generateValue(value.stringValue()); //genero i valori per ogni record della query
			this.itObject.getSimulationObject().AddElement(itElement); //aggiungo l'oggetto con i valori appena generati alla lista delle triple
			triple += itElement.generateTriple(type)+"\n";
			if(stateful){
				statesList.add(itElement.generateStateArray(id));
			}
		}
		return triple;
	}
		
	public String toJson(){
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		return g.toJson(statesList);
	}
	public int getIteration(){
		return index;
	}
	
	public String getId(){
		return id;
	}
	
	
	
}
