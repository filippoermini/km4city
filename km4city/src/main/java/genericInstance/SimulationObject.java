package genericInstance;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import Application.CommonValue;
import XMLDomain.Tree;
import XMLDomain.Tree.Instance;
import XMLDomain.Tree.Instance.Properties.Prop;

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
}
