package Application;

import java.util.ArrayList;
import java.util.Formatter;

import genericClass.GenericAttribute;
import genericClass.GenericObject;
import jsonDomain.Attribute;
import jsonDomain.State;



public class TripleContainer{
	
	private String type; 
	private ArrayList<GenericObject> tripleObject;
	private String tripleRDF;
	private State state;
	public TripleContainer(String type){
		this.tripleObject = new ArrayList<>();
		this.type = type;
	}
	public TripleContainer clone(){
		return new TripleContainer(type);
	}
	public void add(GenericObject obj){
		this.tripleObject.add(obj);
	}
	public ArrayList<GenericObject> getTripleObject(){
		return this.tripleObject;
	}
	public void generateTriple(){
		tripleRDF = "";
		state = new State();
		for(GenericObject go:tripleObject){
			//per ogni classe genero il type
			tripleRDF += "<"+go.getBaseUri()+"/"+go.getIdentifier().getAttribute().gettAttributeValue()+"> "+"<"+type+"> "+"<"+go.getType().toString()+"> .\n";
			state.add(new Attribute("id",go.getIdentifier().getAttribute().gettAttributeValue()));
			for(GenericAttribute ga:go.getAttributeList()){
				//genero per ogni attributo delle classi che compongono l'oggetto la lista delle triple
				String object = ga.isExternalKey()?"<"+ga.getExternalClassObject().getBaseUri()+"/"+ga.getAttribute().gettAttributeValue()+">\"":ga.getAttribute().gettAttributeValue()+"\""+(ga.getUri()!=null?"^^<"+ga.getUri()+">":"");
				tripleRDF += "<"+go.getBaseUri()+"/"+go.getIdentifier().getAttribute().gettAttributeValue()+"> "+"<"+ga.getAttributeKey()+"> "+object+" .\n";
				state.add(new Attribute(ga.getAttributeName(),ga.getAttribute().gettAttributeValue()));
			}
		}		
	}
	
	public String getRDFTriple(){
		return tripleRDF;
	}
	public State getState(){
		return state;
	}
	public String getValueByAttributeName(String name){
		GenericAttribute value = null;
		for(GenericObject go:tripleObject){
			if((value = go.getAttributeByName(name))!= null )
				return value.getAttribute().gettAttributeValue()!=null?value.getAttribute().gettAttributeValue():null;
		}
		return null;
	}
}