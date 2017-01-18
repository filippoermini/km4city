package jsonDomain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
	public State getStatesById(String id){
		Iterator<State> it = stateList.iterator();
		State state;
		while(it.hasNext()){
			state = it.next();
			if(state.getId().contentEquals(id)){
				return state;
			}
		}
		return null;
	}
	
	
	
}
