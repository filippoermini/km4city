package Application;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import genericClass.GenericAttribute;
import genericClass.GenericObject;
import jsonDomain.Attribute;
import jsonDomain.State;



public class TripleContainer {
	
	private String type; 
	private ArrayList<GenericObject> tripleObject;
	private String tripleRDF;
	private State state;
	
	public TripleContainer(String type){
		this.tripleObject = new ArrayList<>();
		this.type = type;
	}
	
	public TripleContainer(TripleContainer t){
		this.type = t.type;
		this.tripleRDF = t.tripleRDF;
		this.state = t.state;
		this.tripleObject = new ArrayList<>();
		Iterator<GenericObject> it = t.tripleObject.iterator();
		while(it.hasNext()){
			this.tripleObject.add(new GenericObject(it.next()));
		}
	}
	
	public GenericObject getObjectClassByName(String name){
		for(GenericObject g:getTripleObject()){
			if(g.getClassName().contains(name)){
				return g;
			}
		}
		return null;
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
			String baseUri = "<"+go.getBaseUri(tripleObject)+"> ";
			tripleRDF += baseUri+"<"+type+"> "+"<"+go.getType().toString()+"> .\n";
			state.add(new Attribute("id",go.getIdentifier().getAttribute().getAttributeValue().toString()));
			for(GenericAttribute ga:go.getAttributeList()){
				//genero per ogni attributo delle classi che compongono l'oggetto la lista delle triple
				if(!ga.isHidden()){
					String attributeValue = !ga.isUri()?"\""+ga.getAttribute().getAttributeValue().toString()+"\"":"<"+ga.getAttribute().getAttributeValue().toString()+">";
					String object = ga.isExternalKey()?"<"+ga.getExternalClassObject().getBaseUri(tripleObject)+">":attributeValue+(ga.getUri()!=null?"^^<"+ga.getUri(tripleObject)+">":"");
					tripleRDF += baseUri+"<"+ga.getAttributeKey()+"> "+object+" .\n";
					state.add(new Attribute(ga.getAttributeName(),ga.getAttribute().getAttributeValue().toString()));
				}
			}
		}		
	}
	
	public String getRDFTriple(){
		return tripleRDF;
	}
	public State getState(){
		return state;
	}
	
	public void setID(String id){
		for(int i=0;i<getTripleObject().size();i++)
		{
			GenericObject g = getTripleObject().get(i);
			if (g.isRoot()){
				g.setID(id);
			}	
		}
	}
	public GenericAttribute getGenericAttributeByName(String name){
		GenericAttribute value = null;
		for(GenericObject go:tripleObject){
			if((value = go.getAttributeByName(name))!= null )
				return value;
		}
		return null;
	}
	public String getValueByAttributeName(String name){
		GenericAttribute value = null;
		for(GenericObject go:tripleObject){
			if((value = go.getAttributeByName(name))!= null )
				return value.getAttribute().getAttributeValue()!=null?value.getAttribute().getAttributeValue().toString():null;
		}
		return null;
	}
	
}