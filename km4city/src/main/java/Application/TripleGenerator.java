package Application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Application.RDFconnector.RepositoryManager;
import XMLDomain.Tree;
import XMLDomain.Tree.Class;
import genericClass.AttributeParam;
import genericClass.GenericAttribute;
import genericClass.GenericClass;
import genericClass.IterationElement;
import genericClass.IterationManager;
import genericClass.SimulationObject;
import genericClass.GenericAttribute.Attribute;
import jsonDomain.State;
import jsonDomain.States;

public class TripleGenerator {

	private RepositoryManager rdfEngine;
	private SimulationObject simulationObject;
	private IterationManager itManager;
	private ArrayList<State> statesList;
	public static Logger logger; 
	
	
	//TODO da rifare completamente
	public TripleGenerator(Tree tree){
		
		this.rdfEngine = RDFconnector.getInstance(tree.getClassIterationQuery().getServer());
		this.simulationObject = new SimulationObject(tree);
		this.itManager = new IterationManager(tree.getIterationElement());
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
			//ArrayList;
			while(results.hasNext()){
				BindingSet bindingSet = results.next();
				Value value = bindingSet.getValue("id");
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
