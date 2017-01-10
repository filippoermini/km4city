package jsonDomain;

import java.util.ArrayList;
import java.util.HashMap;

public class States {

	private ArrayList<State> stateList;
	public States(){
		this.stateList = new ArrayList<>();
	}
	public void add(State s){
		this.stateList.add(s);
	}
	protected void setStateList(ArrayList<State> s){
		this.stateList = s;
	}
	public ArrayList<State> getStateList(){
		return this.stateList;
	}
	
	
	
}
