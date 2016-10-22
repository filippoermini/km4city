package km4city.Application;

import java.util.ArrayList;
import java.util.Formatter;

import GenericClass.GenericAttribute;
import GenericClass.GenericObject;


public class TripleContainer{
	
	private String type; 
	private ArrayList<GenericObject> tripleObject;
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
		return triple;
	}
	
	public String getValueByAttributeName(String name){
		GenericAttribute value = null;
		for(GenericObject go:tripleObject){
			if((value = go.getAttributeByName(name))!= null )
				return value.getAttribute().gettAttributeValue();
		}
		return null;
	}
}