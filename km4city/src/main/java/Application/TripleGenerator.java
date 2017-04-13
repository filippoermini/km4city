package Application;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Application.RDFconnector.RepositoryManager;
import XMLDomain.Tree;
import genericInstance.IterationElement;
import genericInstance.IterationManager;
import genericInstance.IterationObject;
import jsonDomain.State;
import jsonDomain.States;

public class TripleGenerator {
	
	private RepositoryManager rdfEngine;
	
	private IterationManager itManager;
	private ArrayList<State> statesList;
	private IterationObject itObject;
	private int index;
	private String id;
	public static Logger logger; 
	
	
	
	
	
	public TripleGenerator(Tree tree){
		
		this.itObject = new IterationObject(this, tree);
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
