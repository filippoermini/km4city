package Application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import XMLDomain.Tree;
import XMLDomain.Tree.Class;
import genericClass.GenericAttribute;
import genericClass.GenericObject;
import jsonDomain.States;

public class TripleGenerator {

	private String query;
	private TripleContainer tripleContainer;
	private TripleList tripleList;
	private ArrayList<GenericAttribute> boundAttribute;
	private States statesList;
	final static Logger logger = Logger.getLogger(TripleGenerator.class);
	
	
	public TripleGenerator(String query,Tree tree){
		
		this.query = query;
		this.tripleContainer = new TripleContainer(tree.getTypeId());
		this.boundAttribute = new ArrayList<>();
		tripleList = new TripleList();
		statesList = new States();
		Iterator<Class> it = tree.getClazz().iterator();
		Tree.Class c;
		while(it.hasNext()){
			c = it.next();
			GenericObject g = new GenericObject(c);
			tripleContainer.add(g);
		}
	}
	
	private void setID(String id){
		for(int i=0;i<tripleContainer.getTripleObject().size();i++)
		{
			GenericObject g = tripleContainer.getTripleObject().get(i);
			if (g.isRoot()){
				g.setID(id);
			}	
		}
	}
	
	private GenericObject getObjectClassByName(String name){
		for(GenericObject g:tripleContainer.getTripleObject()){
			if(g.getClassName().contains(name)){
				return g;
			}
		}
		return null;
	}
	
	//questa classe si occupa di generare per ogni attributo un valore del tipo e nel range da esso specificato
	private void generateValue(String resId){
		//setto il valore id della classe root con il risultato della query
		this.setID(resId);
		//ciclo su tutti gli attributi e genero un valore
		for(GenericObject g:tripleContainer.getTripleObject()){ //estraggo le classi
			if(!g.isProcessed()){//la classe non è ancora stata processata
				_process(g);
			}
		}
	}
	
	private void processClass(String ClassName){
		GenericObject g = getObjectClassByName(ClassName);
		_process(g);
	}

	private void processClass(GenericObject g){
		_process(g);
	}
	
	private void _process(GenericObject g){
		for(GenericAttribute a:g.getAttributeList()){ //estraggo gli attributi per ogni classe
			if(!(a.isPrimaryKey() && g.isRoot())){//non devo generare il valore per il campo id della clsse root, è già settato
				if(a.isConstrain()){
					this.boundAttribute.add(a); //l'attributo contiene delle dipendenze verso altri attibuti che potrebbero non essere stati ancora generati
				}else{
					if(a.isExternalKey()){//l'attributo è un riferimento ad un'altra classe
						
						GenericObject refClass = getObjectClassByName(a.getType());
						if (!refClass.isProcessed()){ //la classe non è ancora stata processata
							if(refClass.getIdentifier().getAttribute().getAttributeValue()==null)
								_process(refClass); //chiamata ricorsiva
						} // alla fine di questa procedura la classe refClass contiene tutti i valori (escluso quelli che hanno delle dipendenze) compreso il valore dell'attributo identifier
						a.setExternalClassObject(refClass); //l'attributo identifier della refClass è già stato valorizzato e lo posso passare al mio attributo a
					}else{ // l'attributo è un campo semplice 
						
						//calcolo del valore per la simulazione in base ai parametri  
						a.getAttribute().setValue(a.getType(), a.getAttribute().getMax(), a.getAttribute().getMin());
						//System.out.println(a.toString());
					}
				}
				
			}
		}
		//procedura di smaltimento della lista boundAttribute
		g.setProcessed();
		int index = 0;
		GenericAttribute ga;
		while(!boundAttribute.isEmpty()){
			ga = boundAttribute.get(index);
			try {
				if(execute(ga)){
					boundAttribute.remove(ga);
					index = index==0?0:index - 1;
				}
			} catch (ScriptException e) {
				logger.error("Evaluate expression "+ga.getValueExpression()+" error");
			}
			index++;
		}
		
	}
	
	private boolean execute(GenericAttribute ga) throws ScriptException{
		String regex = "[$]+[a-zA-Z0-9]*\\s";
		String valueExpression = ga.getValueExpression();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(valueExpression);

		while (matcher.find()){
			String var = matcher.group().replace(" ", "");
			String value = this.tripleContainer.getValueByAttributeName(var.replace("$", ""));
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
		String[] queryRes = new String[]{"12345"};
		//ArrayList;
		for(String el: queryRes){
			generateValue(el); //genero i valori per ogni record della query
			this.tripleList.add(this.tripleContainer); //aggiungo l'oggetto con i valori appena generati alla lista delle triple
			this.tripleContainer.generateTriple();
			triple += this.tripleContainer.getRDFTriple()+"\n";
			if(stateful){
				statesList.add(this.tripleContainer.getState());
			}
		}
		return triple;
	}
	
	public String toJson(){
		Gson g = new GsonBuilder().setPrettyPrinting().create();;
		return g.toJson(statesList);
	}
}
