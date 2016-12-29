package genericInstance;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.relation.RelationServiceNotRegisteredException;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.io.binary.Token.Attribute;

import Application.CommonValue;
import Application.EvalEngine;
import Application.RDFconnector.RepositoryManager;
import XMLDomain.Tree;
import XMLDomain.Tree.Instance;
import XMLDomain.Tree.Instance.Properties.Prop;
import XMLDomain.Tree.Instance;

public class IterationManager {

	
	private String mainBaseUri;
	private Tree.iterationElement origin;
	private IterationElement it;
	private EvalEngine javascriptEngine;
	private SimulationObject temporarylist;
	final static Logger logger  = Logger.getLogger(CommonValue.getInstance().getSimulationName());
	
	public IterationManager(Tree.iterationElement iterationElement, String baseUri, SimulationObject tempList){
		
		this.javascriptEngine = EvalEngine.getInstance();
		this.origin = iterationElement;
		this.it = new IterationElement();
		this.mainBaseUri = baseUri;
		this.temporarylist = tempList;
	}
	
	private void init(IterationElement it){
		Iterator<Instance> itc = this.origin.getInstance().iterator();
		Tree.Instance c;
		while(itc.hasNext()){
			c = itc.next();
			GenericInstance g = new GenericInstance(c,mainBaseUri);
			it.getInstances().add(g);
		}
		if(this.origin.getAttributes() != null){
			Iterator<Prop> itp = this.origin.getAttributes().getProp().iterator();
			Prop p;
			while(itp.hasNext()){
				p = itp.next();
				GenericAttribute g = new GenericAttribute(p);
				it.getAttributes().add(g);
			}
		}
	}
	public void setTemporayList(SimulationObject obj){
		this.temporarylist = obj;
	}
	public IterationElement generateIterationElement(String resId){
		this.it = new IterationElement();
		init(it);
		setID(resId);
		//genero prima gli attibuti condivisi
		for(GenericAttribute ga:it.getAttributes()){
			generateAttributeValue(ga);
		}
		//generazione delle classi
		for(GenericInstance g:it.getInstances()){ //estraggo le classi
			if(!g.isProcessed()){//la classe non è ancora stata processata
				_process(g);
			}
		}
		return it;
	}
	
	public void setID(String id){
		for(int i=0;i<it.getInstances().size();i++)
		{
			GenericInstance g = it.getInstances().get(i);
			if (g.isRoot()){
				g.setID(id);
			}	
		}
	}
	
	private void _process(GenericInstance g){
		for(GenericAttribute a:g.getAttributeList()){ //estraggo gli attributi per ogni classe
			if(a.getAttribute().getAttributeValue() == null){
				if(!(a.isPrimaryKey() && g.isRoot())){//non devo generare il valore per il campo id della clsse root, è già settato
					if(a.isValueExpression()){
						generateAttributeValue(a);
					}else{
						if(a.isExternalKey()){//l'attributo è un riferimento ad un'altra classe
							
							GenericInstance refInstances = it.getObjectInstanceByName(a.getType());
							if (!refInstances.isProcessed()){ //la classe non è ancora stata processata
								if(refInstances.getIdentifier().getAttribute().getAttributeValue()==null)
									_process(refInstances); //chiamata ricorsiva
							} // alla fine di questa procedura la classe refInstances contiene tutti i valori (escluso quelli che hanno delle dipendenze) compreso il valore dell'attributo identifier
							a.setExternalInstanceObject(refInstances); //l'attributo identifier della refInstances è già stato valorizzato e lo posso passare al mio attributo a
						}else{ // l'attributo è un campo semplice 
							generateAttributeValue(a);
						}
					}
				}
			}
		}
		
	}
	private void generateAttributeValue(GenericAttribute a){
		//calcolo del valore per la simulazione in base ai parametri  
		if(a.isValueExpression()){//uno o più parametri dell'attributo sono delle espressioni da calcolare
			//l'attributo contiene dei parametri con delle dipendenze verso altri attibuti che potrebbero non essere stati ancora generati
			//si avvia la procedura di risoluzione, e in caso di generazione, delle dipendenze
			//vado a cercare i valori necessari all'espressione e se non sono ancora determinati avvio la procedura di determinazione
			//richiamando ricorsivamente questa funzione sul nuovo attributo
			if(!a.isProcessed()){ //se l'attributo non è già stato processato
				Iterator<Entry<String, AttributeParam>> iterator = a.getAttributeList().entrySet().iterator();
			    a.setProcessed(true);
				while (iterator.hasNext()) { //ciclo su tutti gli attributi che contengono una espressione
					Map.Entry<String,AttributeParam> pair = (Map.Entry<String,AttributeParam>)iterator.next();
			    	if (pair.getValue().isValueEspression()){
			    		//il parametro contiene un'espressione da calcolare
			    		String regex = "[$]+[{]+[\\w-]*+[}]";
			    		String valueExpression = pair.getValue().getObject().toString();
			    		Pattern pattern = Pattern.compile(regex);
			    		Matcher matcher = pattern.matcher(valueExpression);
			    		while (matcher.find()){
			    			Object value;
			    			String var = matcher.group().replace(" ", "");
			    			GenericAttribute ga = it.getGenericAttributeByName(var.replace("$", "").replace("{", "").replace("}", "")); //attributo da cui dipende la variabile
			    			if(ga == null){
			    				logger.error("Variable "+var+" not present");
			    				logger.error("Process interrupt");
			    				System.exit(-1);
			    			}
			    			value = ((Object) ga.getAttribute().getAttributeValue());
			    			if(value != null){
			    				if(value.getClass().getName().contains("String")) 
			    					value = "\""+value+"\"";
			    				valueExpression = valueExpression.replace(var, value.toString());
			    				pair.getValue().setObject(valueExpression);
			    			}else{
			    				//il valore dell'attributo da cui dipende la variabile non è ancora stato generato
			    				generateAttributeValue(ga);
			    				value = ((Object) ga.getAttribute().getAttributeValue());
			    				if(value.getClass().getName().contains("String")) 
			    					value = "\""+value+"\"";
			    				valueExpression = valueExpression.replace(var, value.toString());
			    				pair.getValue().setObject(valueExpression);
			    			}
			    		}
			    		//se non è un value expression (cioè sono dentro un parametro) eseguo l'espressione
			    		if(!a.getType().equals("valueExpression") && !a.getType().equals("query")){
				    		try {
								pair.getValue().setObject(javascriptEngine.getEngine().eval(pair.getValue().getObject().toString()).toString());
							} catch (ScriptException e) {
								// TODO Auto-generated catch block
								logger.error("Evaluate expression error: "+ e.getMessage());
								logger.error("Process interrupt");
								System.exit(-1);
							}
			    		}
			    	}			
				}//ho determinato per tutti i valori dei parametri necessari a valorizzare 
			}else{
				if(a.getAttribute().getAttributeValue()==null){
					//caso di dipendenza circolare il percorso mi ha mandato da un attributo dal quale sono già passato
					logger.error("Circular dependency on bound attribute: "+a.getAttributeName());
					logger.error("Process interrupt");
					System.exit(-1);
				}	
			}    	
		}
		//controllo il caso in cui il valore � un riferimento ad un iterazione precedente
		if(a.isForegoingValue()){
			String regex = "@[\\[]+[\\w-']*+[\\]]+[{]+[\\w-]*+[}]";
			String regIndex = "[\\[]+[\\w-']*+[\\]]";
			String regVar = "[{]+[\\w-']*+[}]";
			String refValue = a.getParam("refValue").toString();
			Pattern pattern = Pattern.compile(regex);
    		Matcher matcher = pattern.matcher(refValue);
    		boolean find = false;
    		while (matcher.find()){
    			find = true;
    			Object value;
    			String var = matcher.group().trim();
    			Pattern patternIndex = Pattern.compile(regIndex);
        		matcher = patternIndex.matcher(var);
        		if(matcher.find()){
        			String index = matcher.group().trim().replace("[", "").replace("]", "");
        			//controllo se � un indice numerico o un id 
        			if(index.contains("'")){
        				//l'indice � un id
        				
        				index = index.replaceAll("'", "");
        				Pattern patternVar = Pattern.compile(regVar);
		        		matcher = patternVar.matcher(var);
		        		matcher.find();
		        		String varName = matcher.group().trim().replace("{", "").replace("}", "");
		        		String val;
		        		
		        		//controllo se � l'id attuale 
        				if(index.equals(this.it.getValueId())){
        					//il valore potrebbe ancora non essere stato generato
        					if((val = this.it.getValueByAttributeName(varName))!=null){
    		        			a.getAttribute().setValue(val);
    		        		}else{
    		        			for(GenericInstance g:it.getInstances()){ //estraggo le classi
    		        				if(!g.isProcessed()){//la classe non è ancora stata processata
    		        					_process(g);
    		        				}
    		        			}
    		        			//ho generato i valori che non avevo
    		        			a.getAttribute().setValue(this.it.getValueByAttributeName(varName));    
    		        		}
        				}else{
        					//vado a cercare l'id nella lista degli elementi gi� generati
        					IterationElement ie;
        					if((ie = this.temporarylist.getElementById(index))!=null){
        						val = ie.getValueByAttributeName(varName);
        		        		a.getAttribute().setValue(val);
        		        		
        					}else{
        						//l'elemento con quell'id non � presente nella lista
        						a.getAttribute().setValue(a.getParam("defaultValue").toString());
        					}
        				}
        			}else{
        				//l'indice � il numero del record
        				int indexInt = Integer.parseInt(index);
        				if(this.temporarylist.size()>indexInt){
        					IterationElement ie = this.temporarylist.getElementAtIndex(indexInt);
        					Pattern patternVar = Pattern.compile(regVar);
    		        		matcher = patternVar.matcher(var);
    		        		matcher.find();
    		        		String varName = matcher.group().trim().replace("{", "").replace("}", "");
    		        		a.getAttribute().setValue(ie.getValueByAttributeName(varName));
        				}else{
        					//il record a quell'indice ancora non esiste
        					a.getAttribute().setValue(a.getParam("defaultValue").toString());
        				}
        				
        				
        			}
        		}
    			
    			
    		}			

		}else{
			a.getAttribute().setValue(a.getType(), a.getAttributeList());
		}
		//tutti i parametri necessari a determinare il valore dell'attributo non sono espressioni (o non lo sono più)
		
		//System.out.println(a.toString());
	}
}
