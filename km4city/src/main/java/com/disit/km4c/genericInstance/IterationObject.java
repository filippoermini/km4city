package com.disit.km4c.genericInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.disit.km4c.application.TripleGenerator;
import com.disit.km4c.jsonDomain.LoadedStates;
import com.disit.km4c.jsonDomain.State;
import com.disit.km4c.xmlDomain.Tree;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class IterationObject{
	/**
	 * 
	 */
	private final TripleGenerator tripleGenerator;
	private HashMap<String, KeyWordCommand> keyWord;
	private LoadedStates loadedStates;
	private SimulationObject simulationObject;
	
	public IterationObject(TripleGenerator tripleGenerator, Tree tree){
		this.tripleGenerator = tripleGenerator;
		keyWord = new HashMap<>();
		loadedStates = new LoadedStates();
		initKeyWord();
		if(tree.isStatefull()) loadStates(tree);
		
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
            public Object runCommand() { return IterationObject.this.tripleGenerator.getIteration(); };
        });
		this.keyWord.put("id", new KeyWordCommand() {
            public Object runCommand() { return IterationObject.this.tripleGenerator.getId(); };
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
			TripleGenerator.logger.error("File Json error "+e);
			TripleGenerator.logger.error("Process interrupt");
			System.exit(-1);
		}
		
	}
}