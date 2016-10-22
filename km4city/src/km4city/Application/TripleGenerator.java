package km4city.Application;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.stream.Collectors;

import GenericClass.GenericAttribute;
import GenericClass.GenericObject;
import XMLDomain.Tree;
import XMLDomain.Tree.Class;

public class TripleGenerator {

	public class TripleObject{
		
		private String type; 
		private ArrayList<GenericObject> tripleObject;
		public TripleObject(String type){
			this.tripleObject = new ArrayList<>();
			this.type = type;
		}
		public TripleObject clone(){
			return new TripleObject(type);
		}
		public void add(GenericObject obj){
			this.tripleObject.add(obj);
		}
		public ArrayList<GenericObject> getTripleObject(){
			return this.tripleObject;
		}
		public String ToRDF(){
			String triple = "";
			Formatter formatter = new Formatter();
			for(GenericObject go:tripleObject){
				//per ogni classe genero il type
				triple += "<"+go.getBaseUri()+"/"+go.getIdentifier().getAttribute().gettAttributeValue()+"> "+"<"+type+"> "+"<"+go.getType().toString()+"> .\n";
				for(GenericAttribute ga:go.getAttributeList()){
					//genero per ogni attributo delle classi che compongono l'oggetto la lista delle triple
					String object = ga.isExternalKey()?"<"+ga.getExternalClassObject().getBaseUri()+"/"+ga.getAttribute().gettAttributeValue()+">":ga.getAttribute().gettAttributeValue()+(ga.getUri()!=null?"^^<"+ga.getUri()+">":"");
					triple += "<"+go.getBaseUri()+"/"+go.getIdentifier().getAttribute().gettAttributeValue()+"> "+"<"+ga.getAttributeKey()+"> "+object+" .\n";
				}
			}
			formatter.close();
			return triple;
		}
	}
	public class TripleList{
		
		private ArrayList<TripleObject> tripleList;
		
		public TripleList(){
			this.tripleList = new ArrayList<>();
		}
		public void add(TripleObject obj){
			TripleObject objClone = obj.clone();
			this.tripleList.add(objClone);
		}
		
		public Iterator<TripleObject> getIterator(){
			return this.getIterator();
		}
	}
	private String query;
	private TripleObject tripleObject;
	private ArrayList<GenericAttribute> boundAttribute;
	private TripleList tripleList;
	
	
	public TripleGenerator(String query,Tree tree){
		this.query = query;
		this.tripleObject = new TripleObject(tree.getTypeId());
		this.boundAttribute = new ArrayList<>();
		tripleList = new TripleList();
		Iterator<Class> it = tree.getClazz().iterator();
		Tree.Class c;
		while(it.hasNext()){
			c = it.next();
			GenericObject g = new GenericObject(c);
			tripleObject.add(g);
		}
	}
	
	private void setID(String id){
		for(int i=0;i<tripleObject.getTripleObject().size();i++)
		{
			GenericObject g = tripleObject.getTripleObject().get(i);
			if (g.isRoot()){
				g.setID(id);
			}	
		}
	}
	
	private GenericObject getObjectClassByName(String name){
		for(GenericObject g:tripleObject.getTripleObject()){
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
		for(GenericObject g:tripleObject.getTripleObject()){ //estraggo le classi
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
				//procedura di smaltimento della lista boundAttribute
			}
		}
		g.setProcessed();
	}
	
	public String tripleRDF(){
		String triple = "";
		//eseguo la query e ciclo sui risultati 
		String[] queryRes = new String[]{"12345"};
		//ArrayList;
		for(String el: queryRes){
			generateValue(el); //genero i valori per ogni record della query
			this.tripleList.add(this.tripleObject); //aggiungo l'oggetto con i valori appena generati alla lista delle triple
			triple += this.tripleObject.ToRDF()+"\n";
		}
		return triple;
	}
}
