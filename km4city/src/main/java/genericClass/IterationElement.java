package genericClass;

import java.util.ArrayList;

import jsonDomain.Attribute;
import jsonDomain.State;

public class IterationElement {

	
	private ArrayList<GenericAttribute> attributes;
	private ArrayList<GenericClass> classes;
	
	
	public IterationElement(){
		this.attributes = new ArrayList<>();
		this.classes = new ArrayList<>();
	}
	
	public ArrayList<GenericClass> getClasses(){
		return this.classes;
	}
	
	public ArrayList<GenericAttribute> getAttributes(){
		return this.attributes;
	}
	
	public GenericClass getObjectClassByName(String name){
		for(GenericClass g:classes){
			if(g.getClassName().contains(name)){
				return g;
			}
		}
		return null;
	}
	
	public GenericAttribute getGenericAttributeByName(String name){
		GenericAttribute value = null;
		for(GenericClass go:classes){
			if((value = go.getAttributeByName(name))!= null )
				return value;
		}
		for(GenericAttribute ga:attributes){
			if(name.equals(ga.getAttributeName()))
				return ga;
		}
		return null;
	}
	
	public String getValueByAttributeName(String name){
		GenericAttribute value = null;
		for(GenericClass go:classes){
			if((value = go.getAttributeByName(name))!= null )
				return value.getAttribute().getAttributeValue()!=null?value.getAttribute().getAttributeValue().toString():null;
		}
		for(GenericAttribute ga:attributes){
			if(name.equals(ga.getAttributeName()))
				return ga.getValue();
		}
		return null;
	}
	
	public String generateTriple(String type){
		String tripleRDF = "";
		State state = new State();
		for(GenericClass go:classes){
			//per ogni classe genero il type
			String baseUri = "<"+go.getBaseUri(classes)+"> ";
			tripleRDF += baseUri+"<"+type+"> "+"<"+go.getType().toString()+"> .\n";
			state.add(new Attribute("id",go.getIdentifier().getAttribute().getAttributeValue().toString()));
			for(GenericAttribute ga:go.getAttributeList()){
				//genero per ogni attributo delle classi che compongono l'oggetto la lista delle triple
				if(!ga.isHidden()){
					String attributeValue = !ga.isUri()?"\""+ga.getAttribute().getAttributeValue().toString()+"\"":"<"+ga.getAttribute().getAttributeValue().toString()+">";
					String object = ga.isExternalKey()?"<"+ga.getExternalClassObject().getBaseUri(classes)+">":attributeValue+(ga.getUri()!=null?"^^<"+ga.getUri(classes)+">":"");
					tripleRDF += baseUri+"<"+ga.getAttributeKey()+"> "+object+" .\n";
					
				}
			}
		}
		return tripleRDF;
	}
	
	public State generateStateArray(){
		State state = new State();
		for(GenericClass go:classes){
			for(GenericAttribute ga:go.getAttributeList()){
				if(!ga.isHidden()){
					state.add(new Attribute(ga.getAttributeName(),ga.getAttribute().getAttributeValue().toString()));
				}
			}
		}
		return state; 
	}
}
