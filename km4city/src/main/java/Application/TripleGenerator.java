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
import genericClass.GenericObject;
import genericClass.GenericAttribute.Attribute;
import jsonDomain.States;

public class TripleGenerator {

	private String query;
	private RepositoryManager rdfEngine;
	private TripleContainer tripleContainerOrigin;
	private TripleList tripleList;
	private States statesList;
	private EvalEngine javascriptEngine;
	public static Logger logger; 
	
	
	public TripleGenerator(Tree tree,RepositoryManager rdf){
		
		this.rdfEngine = rdf;
		this.query = tree.getClassIterationQuery().getQuery();
		this.tripleContainerOrigin = new TripleContainer(tree.getTypeId());
		this.tripleList = new TripleList();
		this.statesList = new States();
		this.javascriptEngine = EvalEngine.getInstance();
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
			if(a.getAttribute().getAttributeValue() == null){
				if(!(a.isPrimaryKey() && g.isRoot())){//non devo generare il valore per il campo id della clsse root, è già settato
					if(a.isValueExpression()){
						generateAttributeValue(a,clone);
					}else{
						if(a.isExternalKey()){//l'attributo è un riferimento ad un'altra classe
							
							GenericObject refClass = clone.getObjectClassByName(a.getType());
							if (!refClass.isProcessed()){ //la classe non è ancora stata processata
								if(refClass.getIdentifier().getAttribute().getAttributeValue()==null)
									_process(refClass,clone); //chiamata ricorsiva
							} // alla fine di questa procedura la classe refClass contiene tutti i valori (escluso quelli che hanno delle dipendenze) compreso il valore dell'attributo identifier
							a.setExternalClassObject(refClass); //l'attributo identifier della refClass è già stato valorizzato e lo posso passare al mio attributo a
						}else{ // l'attributo è un campo semplice 
							generateAttributeValue(a,clone);
						}
					}
				}
			}
		}
		
	}
	private void generateAttributeValue(GenericAttribute a, TripleContainer tc){
		//calcolo del valore per la simulazione in base ai parametri  
		if(a.isValueExpression()){//uno o più parametri dell'attributo sono delle espressioni da calcolare
			//l'attributo contiene dei parametri con delle dipendenze verso altri attibuti che potrebbero non essere stati ancora generati
			//si avvia la procedura di risoluzione, e in caso di generazione, delle dipendenze
			//vado a cercare i valori necessari all'espressione e se non sono ancora determinati avvio la procedura di determinazione
			//richiamando ricorsivamente questa funzione sul nuovo attributo
			if(!a.isProcessed()){ //se l'attributo non è già stato processato
				Iterator<Entry<String, AttributeParam>> it = a.getAttributeList().entrySet().iterator();
			    a.setProcessed(true);
				while (it.hasNext()) { //ciclo su tutti gli attributi che contengono una espressione
					Map.Entry<String,AttributeParam> pair = (Map.Entry<String,AttributeParam>)it.next();
			    	if (pair.getValue().isValueEspression()){
			    		//il parametro contiene un'espressione da calcolare
			    		String regex = "[$]+[a-zA-Z0-9_]*\\s";
			    		String valueExpression = (String) pair.getValue().getObject();
			    		Pattern pattern = Pattern.compile(regex);
			    		Matcher matcher = pattern.matcher(valueExpression);
			    		while (matcher.find()){
			    			Object value;
			    			String var = matcher.group().replace(" ", "");
			    			GenericAttribute ga = tc.getGenericAttributeByName(var.replace("$", "")); //attributo da cui dipende la variabile
			    			value = ((Object) ga.getAttribute().getAttributeValue());
			    			if(value != null){
			    				if(value.getClass().getName().contains("String")) 
			    					value = "\""+value+"\"";
			    				valueExpression = valueExpression.replace(var, value.toString());
			    				pair.getValue().setObject(valueExpression);
			    			}else{
			    				//il valore dell'attributo da cui dipende la variabile non è ancora stato generato
			    				generateAttributeValue(ga, tc);
			    			}
			    		}
			    		//se non è un value expression (cioè sono dentro un parametro) eseguo l'espressione
			    		if(!a.getType().equals("valueExpression")){
				    		try {
								pair.getValue().setObject(javascriptEngine.getEngine().eval(pair.getValue().getObject().toString()).toString());
							} catch (ScriptException e) {
								// TODO Auto-generated catch block
								logger.error("Evaluate expression error: "+ e.getMessage());
							}
			    		}
			    	}			
				}//ho determinato per tutti i valori dei parametri necessari a valorizzare 
			}else{
				if(a.getAttribute().getAttributeValue()==null){
					//caso di dipendenza circolare il percorso mi ha mandato da un attributo dal quale sono già passato
					logger.fatal("Circular dependency on bound attribute: "+a.getAttributeName());
					System.exit(0);
				}	
			}    	
		}
		//tutti i parametri necessari a determinare il valore dell'attributo non sono espressioni (o non lo sono più)
		a.getAttribute().setValue(a.getType(), a.getAttributeList());
		//System.out.println(a.toString());
	}
	private boolean execute(GenericAttribute ga,TripleContainer tc) throws ScriptException{
		String regex = "[$]+[a-zA-Z0-9_]*\\s";
		String valueExpression = ga.getValueExpression();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(valueExpression);

		while (matcher.find()){
			String var = matcher.group().replace(" ", "");
			String value = tc.getValueByAttributeName(var.replace("$", ""));
			Pattern p = Pattern.compile("[^a-zA-Z]"); //controllo se il valore restutiito è una stringa in caso la metto tra apici
			value = p.matcher(value).find()?"\""+value+"\"":value;
			if(value != null ){
				valueExpression = valueExpression.replace(var, value);
			}
			else{
				return false;
			}
			
		}
		ScriptEngine engine = javascriptEngine.getEngine();
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
