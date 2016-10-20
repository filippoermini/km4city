package km4city.Application;

import java.util.ArrayList;
import java.util.Iterator;

import GenericClass.GenericAttribute;
import GenericClass.GenericObject;
import XMLDomain.Tree;
import XMLDomain.Tree.Class;

public class TripleGenerator {

	private String query;
	private ArrayList<GenericObject> tripleObject;
	private ArrayList<GenericAttribute> boundAttribute;
	
	public TripleGenerator(String query,Tree tree){
		this.query = query;
		this.tripleObject = new ArrayList<>();
		this.boundAttribute = new ArrayList<>();
		Iterator<Class> it = tree.getClazz().iterator();
		Tree.Class c;
		while(it.hasNext()){
			c = it.next();
			GenericObject g = new GenericObject(c);
			tripleObject.add(g);
		}
	}
	
	private void setID(String id){
		for(GenericObject g:tripleObject){
			if (g.isRoot()){
				g.setID(id);
			}
		}
	}
	
	private GenericObject getObjectClassByName(String name){
		for(GenericObject g:tripleObject){
			if(g.getClassName().contains(name)){
				return g;
			}
		}
		return null;
	}
	
	//questa classe si occupa di generare per ogni attributo un valore del tipo e nel range da esso specificato
	public void generateValue(){
		//per ogni record della query vado a generare un set di triple definito dal Tree e dai genericObject istanziati
		String[] res = new String[]{"12345","12346","12347","12348"}; //risultati della query
		for(String el: res){
			//setto il valore id della classe root con il risultato della query
			this.setID(el);
			//ciclo su tutti gli attributi e genero un valore
			for(GenericObject g:tripleObject){ //estraggo le classi
				if(!g.isProcessed()){//la classe non è ancora stata processata
					_process(g);
				}
			}
		}
	}
	
	public void processClass(String ClassName){
		GenericObject g = getObjectClassByName(ClassName);
		_process(g);
	}

	public void processClass(GenericObject g){
		_process(g);
	}
	
	private void _process(GenericObject g){
		for(GenericAttribute a:g.getAttributeList()){ //estraggo gli attributi per ogni classe
			if(!a.getType().contains("id") && !g.isRoot()){//non devo generare il valore per il campo id della clsse root, è già settato
				if(a.isConstrain()){
					this.boundAttribute.add(a); //l'attributo contiene delle dipendenze verso altri attibuti che potrebbero non essere stati ancora generati
				}else{
					if(a.isExternalKey()){//l'attributo è un riferimento ad un'altra classe
						
						GenericObject refClass = getObjectClassByName(a.getType());
						if (!refClass.isProcessed()){ //la classe non è ancora stata processata
							_process(refClass); //chiamata ricorsiva
						} // alla fine di questa procedura la classe refClass contiene tutti i valori (escluso quelli che hanno delle dipendenze) compreso il valore dell'attributo identifier
						a.setAttribute(refClass.getIdentifier().getAttribute()); //l'attributo identifier della refClass è già stato valorizzato e lo posso passare al mio attributo a
					}else{ // l'attributo è un campo semplice 
						
						//calcolo del valore per la simulazione in base ai parametri  
						a.getAttribute().setValue(a.getType(), a.getAttribute().getMax(), a.getAttribute().getMin());
					}
				}
				//procedura di smaltimento della lista boundAttribute
			}
		}
	}
}
