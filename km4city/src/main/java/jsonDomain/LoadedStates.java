package jsonDomain;

import java.util.ArrayList;

public class LoadedStates{

	
	public class LoadedStatesElement extends States{
		
		private String id;
		
		public LoadedStatesElement(){
			super();
			this.id = "";
		}
		public void setState(String id,ArrayList<State> state){
			this.id = id;
			this.setStateList(state);
		}
	}
	
	private ArrayList<LoadedStatesElement> stateList;
	
	public LoadedStates(){
		this.stateList = new ArrayList<>();
	}
	
	public void addElement(String id,ArrayList<State> state){
		LoadedStatesElement lse = new LoadedStatesElement();
		lse.setState(id, state);
		this.stateList.add(lse);
	}
	
	public ArrayList<LoadedStatesElement> getLoadedList(){
		return this.stateList;
	}
	
	public LoadedStatesElement getStateAtIndex(int index){
		return this.stateList.get(index);
	}
	
	
}
