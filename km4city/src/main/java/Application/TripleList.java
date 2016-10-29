package Application;

import java.util.ArrayList;
import java.util.Iterator;

public class TripleList{
	
	private ArrayList<TripleContainer> tripleList;
	
	public TripleList(){
		this.tripleList = new ArrayList<>();
	}
	
	public Iterator<TripleContainer> getIterator(){
		return this.getIterator();
	}
	public void add(TripleContainer tripleContainer) {
		this.tripleList.add(tripleContainer);
	}
}