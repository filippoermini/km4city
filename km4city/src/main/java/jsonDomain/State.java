package jsonDomain;

import java.util.ArrayList;

public class State{
	
	private ArrayList<Attribute> attribute;
	
	public State(){
		this.attribute = new ArrayList<>();
	}
	public void add(Attribute a){
		this.attribute.add(a);
	}
}