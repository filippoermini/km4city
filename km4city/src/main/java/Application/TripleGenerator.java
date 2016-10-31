package Application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
import genericClass.GenericAttribute;
import genericClass.GenericObject;
import jsonDomain.States;

public class TripleGenerator {

	private String query;
	private RepositoryManager rdfEngine;
	private TripleContainer tripleContainerOrigin;
	private TripleList tripleList;
	private ArrayList<GenericAttribute> boundAttribute;
	private States statesList;
	public static Logger logger; 
	
	
	public TripleGenerator(Tree tree,RepositoryManager rdf){
		
		this.rdfEngine = rdf;
		this.query = tree.getQueryInfo().getQuery();
		this.tripleContainerOrigin = new TripleContainer(tree.getTypeId());
		this.boundAttribute = new ArrayList<>();
		tripleList = new TripleList();
		statesList = new States();
		Iterator<Class> it = tree.getClazz().iterator();
		Tree.Class c;
		while(it.hasNext()){
			c = it.next();
			GenericObject g = new GenericObject(c);
			tripleContainerOrigin.add(g);
		}
		logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
	}
	
	
	
	
	//questa classe si occupa di generare per ogni attributo un valore del tipo e nel range da esso specificato
	private TripleContainer generateValue(String resId){
		//setto il valore id della classe root con il risultato della query
		TripleContainer clone = new TripleContainer(this.tripleContainerOrigin);
		this.boundAttribute = new ArrayList<>();
		clone.setID(resId);
		//ciclo su tutti gli attributi e genero un valore
		for(GenericObject g:clone.getTripleObject()){ //estraggo le classi
			if(!g.isProcessed()){//la classe non è ancora stata processata
				_process(g,clone);
			}
		}
		return clone;
	}
	
	private void _process(GenericObject g,TripleContainer clone){
		for(GenericAttribute a:g.getAttributeList()){ //estraggo gli attributi per ogni classe
			if(!(a.isPrimaryKey() && g.isRoot())){//non devo generare il valore per il campo id della clsse root, è già settato
				if(a.isConstrain()){
					this.boundAttribute.add(a); //l'attributo contiene delle dipendenze verso altri attibuti che potrebbero non essere stati ancora generati
				}else{
					if(a.isExternalKey()){//l'attributo è un riferimento ad un'altra classe
						
						GenericObject refClass = clone.getObjectClassByName(a.getType());
						if (!refClass.isProcessed()){ //la classe non è ancora stata processata
							if(refClass.getIdentifier().getAttribute().getAttributeValue()==null)
								_process(refClass,clone); //chiamata ricorsiva
						} // alla fine di questa procedura la classe refClass contiene tutti i valori (escluso quelli che hanno delle dipendenze) compreso il valore dell'attributo identifier
						a.setExternalClassObject(refClass); //l'attributo identifier della refClass è già stato valorizzato e lo posso passare al mio attributo a
					}else{ // l'attributo è un campo semplice 
						
						//calcolo del valore per la simulazione in base ai parametri  
						a.getAttribute().setValue(a.getType(), a.getAttributeList());
						//System.out.println(a.toString());
					}
				}
				
			}
		}
		//procedura di smaltimento della lista boundAttribute
		g.setProcessed();
		int index = 0;
		GenericAttribute ga = null;;
		int size = boundAttribute.size();
		try{
			while(!boundAttribute.isEmpty()){
				ga = boundAttribute.get(index);
				if(execute(ga,clone)){
					boundAttribute.remove(ga);
					index = index==0?0:index - 1;
					size--;
				}else{
					index++;
				}
				if(index>=size && index!=0){
					throw new Exception("Circular dependency on bound attribute");
				}
			}
		} catch (Exception e) {
			logger.fatal("Evaluate expression error: "+ e.getMessage());
			System.exit(0);
		}

	}
	
	private boolean execute(GenericAttribute ga,TripleContainer tc) throws ScriptException{
		String regex = "[$]+[a-zA-Z0-9]*\\s";
		String valueExpression = ga.getValueExpression();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(valueExpression);

		while (matcher.find()){
			String var = matcher.group().replace(" ", "");
			String value = tc.getValueByAttributeName(var.replace("$", ""));
			if(value != null ){
				valueExpression = valueExpression.replace(var, value);
			}
			else{
				return false;
			}
			
		}
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		String result = engine.eval(valueExpression).toString();
		ga.getAttribute().setAttributeValue(result);
		return true;
	}
	
	public String tripleRDF(boolean stateful){
		
		String triple = "";
		//eseguo la query e ciclo sui risultati 
		TupleQueryResult results = rdfEngine.SPARQLExecute(query);
		//ArrayList;
		while(results.hasNext()){
			BindingSet bindingSet = results.next();
			Value value = bindingSet.getValue("id");
			TripleContainer clone = generateValue(value.stringValue()); //genero i valori per ogni record della query
			this.tripleList.add(clone); //aggiungo l'oggetto con i valori appena generati alla lista delle triple
			clone.generateTriple();
			triple += clone.getRDFTriple()+"\n";
			if(stateful){
				statesList.add(clone.getState());
			}
		}
		return triple;
	}
	
	public String toJson(){
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		return g.toJson(statesList);
	}
}
