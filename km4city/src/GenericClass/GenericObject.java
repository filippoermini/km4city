package GenericClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import XMLDomain.Tree;
import XMLDomain.Tree.Class.Properties.Prop;

public class GenericObject {
	
	private String className;
	private boolean isRoot;
	private String type;
	private String baseUri;
	private ArrayList<GenericAttribute> attributeList;
	private String query;
	private String server;
	private boolean processed; // questo campo mi indica che sono stati inseriti tutti i valori
	
	public GenericObject(Tree.Class Class){
		
		this.query = Class.getQueryInfo().getQuery();
		this.server = Class.getQueryInfo().getServer();
		this.attributeList = new ArrayList<>();
		this.className = Class.getName();
		this.isRoot = Class.getIsRoot().contains("true");
		this.type = Class.getType();
		this.baseUri = Class.getBaseUri();
		this.processed = false;
		
		
		Tree.Class.Properties property = Class.getProperties();
		List<Tree.Class.Properties.Prop> propList = property.getProp();
		
		Iterator<Prop> it = propList.iterator();
		Tree.Class.Properties.Prop prop;
		while(it.hasNext()){
			prop = it.next();
			GenericAttribute gAtt = new GenericAttribute(prop);
			attributeList.add(gAtt);
		}
		
	}

	public void setID(String id){
		for(GenericAttribute g:attributeList){
			if (g.isPrimaryKey()){
				g.getAttribute().setAttributeValue(id);
			}
		}
	}
	@Override
	public String toString(){
		;
		return "Class name: "+getClassName()+"\n"
				+"Type name: "+getType()+"\n"
				+"Base Uri: "+getBaseUri()+"\n"
				+(isRoot?"root class\n"+"Query: "+getQuery()+"\nServer: "+getServer():"")
				+("Attribute list: \n\n")
				+(attributeList.stream().map(Object::toString).collect(Collectors.joining("\n")));
	}
	
	public String getServer(){
		return server;
	}
	public String getQuery(){
		return query;
	}
	public String getClassName() {
		return className;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public String getType() {
		return type;
	}

	public String getBaseUri() {
		return baseUri;
	}

	public boolean isProcessed(){
		return processed;
	}
	public ArrayList<GenericAttribute> getAttributeList() {
		return attributeList;
	}
	
	public void setProcessed(){
		this.processed = true;
	}
	
	public GenericAttribute getIdentifier(){
		for(GenericAttribute a: attributeList){
			if(a.getAttributeKey().contains("identifier")){
				return a;
			}
		}
		return null;
	}
	
	public GenericAttribute getAttributeByName(String name){
		for(GenericAttribute a: attributeList){
			if(a.getAttributeName().contains(name.replaceAll("\\s", "")))
				return a;
		}
		return null;
	}
	
	
	
	
}

