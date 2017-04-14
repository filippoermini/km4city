package com.disit.km4c.jsonDomain;

import java.util.ArrayList;
import java.util.Iterator;

public class State{
	
	private ArrayList<Attribute> attribute;
	private String id;
	
	public State(){
		this.attribute = new ArrayList<>();
		this.id = "";
	}
	public void add(Attribute a){
		this.attribute.add(a);
	}
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getValue(String key){
		Iterator<Attribute> it = attribute.iterator();
		Attribute att;
		while(it.hasNext()){
			att = it.next();
			if(att.getKey().contentEquals(key))
				return att.getValue();
		}
		return null;
	}
}