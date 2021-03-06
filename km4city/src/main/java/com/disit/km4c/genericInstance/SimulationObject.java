package com.disit.km4c.genericInstance;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.disit.km4c.application.CommonValue;
import com.disit.km4c.xmlDomain.Tree;
import com.disit.km4c.xmlDomain.Tree.Instance;
import com.disit.km4c.xmlDomain.Tree.iterationElement;
import com.disit.km4c.xmlDomain.Tree.Instance.Properties.Prop;

public class SimulationObject {
	
	final static Logger logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());

	
	private String typeId;
	private Tree.QueryInfo query;
	private Tree.FileInfo fileInfo;
	private ArrayList<IterationElement> iterationElements;
	
	public SimulationObject(Tree tree){
		this.typeId = tree.getTypeId();
		this.fileInfo = tree.getFileInfo();
		this.query = tree.getInstanceIterationQuery();
		this.iterationElements = new ArrayList<>();
	}
	
	public String getQuery(){
		return this.query.getQuery();
	}
	
	public void AddElement(IterationElement it){
		this.iterationElements.add(it);
	}
	
	public int size(){
		return iterationElements.size();
	}
	
	public IterationElement getElementAtIndex(int index){
		return this.iterationElements.get(index);
	}
	
	public IterationElement getElementById(String id){
		Iterator<IterationElement> it = iterationElements.iterator();
		IterationElement element;
		while(it.hasNext()){
			element = it.next();
			if(element.getValueId().equals(id)){
				return element;
			}
		}
		return null;
	}
}
