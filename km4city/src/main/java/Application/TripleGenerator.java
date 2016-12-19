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
import genericInstance.SimulationObject;
import jsonDomain.State;

public class TripleGenerator {

	private RepositoryManager rdfEngine;
	private SimulationObject simulationObject;
	private IterationManager itManager;
	private ArrayList<State> statesList;
	public static Logger logger; 
	
	public TripleGenerator(Tree tree){
		
		this.rdfEngine = RDFconnector.getInstance(tree.getInstanceIterationQuery().getServer());
		this.simulationObject = new SimulationObject(tree);
		this.itManager = new IterationManager(tree.getIterationElement(),tree.getBaseUri());
		statesList = new ArrayList<>();
		logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
	}
	
	
	
	private IterationElement generateValue(String resId){
		//setto il valore id della classe root con il risultato della query
		IterationElement itElement = itManager.generateIterationElement(resId);
		return itElement;
	}
	
	public String tripleRDF(boolean stateful, String type){
			
			String triple = "";
			//eseguo la query e ciclo sui risultati 
			TupleQueryResult results = rdfEngine.SPARQLExecute(simulationObject.getQuery());
			int index = 0;
			while(results.hasNext()){
				BindingSet bindingSet = results.next();
				Value value = bindingSet.getValue("id");
				logger.info("Processing record "+ ++index +" id extract: "+value.stringValue());
				IterationElement itElement = generateValue(value.stringValue()); //genero i valori per ogni record della query
				this.simulationObject.AddElement(itElement); //aggiungo l'oggetto con i valori appena generati alla lista delle triple
				triple += itElement.generateTriple(type)+"\n";
				if(stateful){
					statesList.add(itElement.generateStateArray());
				}
			}
			return triple;
		}
		
		public String toJson(){
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			return g.toJson(statesList);
		}
	
	
}
