package genericClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import Application.Pair;
import XMLDomain.Tree;
import XMLDomain.Tree.Class.Properties.Prop;

public class GenericObject {
	
	private String className;
	private boolean isRoot;
	private String type;
	private String baseUri;
	private ArrayList<GenericAttribute> attributeList;
	private boolean processed; // questo campo mi indica che sono stati inseriti tutti i valori
	
	public GenericObject(Tree.Class Class){
		
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
	
	

	public GenericObject(GenericObject go) {
	
		this.className = go.className;
		this.isRoot = go.isRoot;
		this.type = go.type;
		this.baseUri = go.baseUri;
		this.processed = go.processed;
		this.attributeList = new ArrayList();
		Iterator<GenericAttribute> it = go.attributeList.iterator();
		while(it.hasNext()){
			this.attributeList.add(new GenericAttribute(it.next()));
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
				+(isRoot?"root class\n":"")
				+("Attribute list: \n\n")
				+(attributeList.stream().map(Object::toString).collect(Collectors.joining("\n")));
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
	
	public String getBaseUri(ArrayList<GenericObject> tripleObject){
		
		if (baseUri.contains("$")){
			Pair<String,String> baseUriParam = getBaseUriParam(this.getBaseUri(),tripleObject);
			String pre = baseUriParam.getLeft();
			String suff = baseUriParam.getRight();
			return pre+suff;
		}else{
			return baseUri+"/"+this.getIdentifier().getAttribute().getAttributeValue().toString();
		}
			
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
	
	private Pair<String,String> getBaseUriParam(String baseUri, ArrayList<GenericObject> tripleObject){
		String suff = "";
		String pre  = baseUri;
		if(baseUri.contains("$")){
			Matcher m = Pattern.compile("\\{.*?\\}").matcher(baseUri);
			while(m.find()){
				String pattern = m.group();
				Matcher m1 = Pattern.compile("[$]+[a-zA-Z_]+").matcher(pattern);
				m1.find();
				String var = m1.group().replace("$", "");
				String value = getIdentifierFromClassName(var,tripleObject);
				pre = pre.replace(pattern, "");
				suff+=pattern.replace("$"+var, value).replace("{", "").replace("}", "").replaceAll(" ", "");
			}
		}
		return new Pair<String,String>(pre,suff);
	}
	
	private String getIdentifierFromClassName(String className, ArrayList<GenericObject> tripleObject){
		for(GenericObject go:tripleObject){
			if(go.getClassName().toLowerCase().equals(className.toLowerCase()))
				return go.getIdentifier().getAttribute().getAttributeValue().toString();
		}
		return "";
	}
	
	
	
}

