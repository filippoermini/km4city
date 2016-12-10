package genericInstance;
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
import XMLDomain.Tree.Instance.Properties.Prop;

public class GenericInstance {
	
	private String instanceName;
	private boolean isRoot;
	private String type;
	private String baseUri;
	private ArrayList<GenericAttribute> attributeList;
	private boolean processed; // questo campo mi indica che sono stati inseriti tutti i valori
	
	public GenericInstance(Tree.Instance instance){
		
		this.attributeList = new ArrayList<>();
		this.instanceName = instance.getName();
		this.isRoot = instance.getIsRoot().contains("true");
		this.type = instance.getType();
		this.baseUri = instance.getBaseUri();
		this.processed = false;
		
		
		Tree.Instance.Properties property = instance.getProperties();
		List<Tree.Instance.Properties.Prop> propList = property.getProp();
		
		Iterator<Prop> it = propList.iterator();
		Tree.Instance.Properties.Prop prop;
		while(it.hasNext()){
			prop = it.next();
			GenericAttribute gAtt = new GenericAttribute(prop);
			attributeList.add(gAtt);
		}	
	}
	public GenericInstance(GenericInstance go) {
	
		this.instanceName = go.instanceName;
		this.isRoot = go.isRoot;
		this.type = go.type;
		this.baseUri = go.baseUri;
		this.processed = go.processed;
		this.attributeList = new ArrayList<GenericAttribute>();
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
		return "Instance name: "+getInstanceName()+"\n"
				+"Type name: "+getType()+"\n"
				+"Base Uri: "+getBaseUri()+"\n"
				+(isRoot?"root Instance\n":"")
				+("Attribute list: \n\n")
				+(attributeList.stream().map(Object::toString).collect(Collectors.joining("\n")));
	}
	
	public String getInstanceName() {
		return instanceName;
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
	
	public String getBaseUri(ArrayList<GenericInstance> tripleObject){
		
		if (baseUri.contains("$")){
			Pair<String,String> baseUriParam = getBaseUriParam(this.getBaseUri(),tripleObject);
			String pre = baseUriParam.getLeft();
			String suff = baseUriParam.getRight();
			return pre+suff;
		}else{
			return baseUri+this.getIdentifier().getAttribute().getAttributeValue().toString();
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
	
	private Pair<String,String> getBaseUriParam(String baseUri, ArrayList<GenericInstance> tripleObject){
		String suff = "";
		String pre  = baseUri;
		if(baseUri.contains("$")){
			Matcher m = Pattern.compile("\\{.*?\\}").matcher(baseUri);
			while(m.find()){
				String pattern = m.group();
				Matcher m1 = Pattern.compile("[$]+[a-zA-Z_]+").matcher(pattern);
				m1.find();
				String var = m1.group().replace("$", "");
				String value = getIdentifierFromInstanceName(var,tripleObject);
				pre = pre.replace(pattern, "");
				suff+=pattern.replace("$"+var, value).replace("{", "").replace("}", "").replaceAll(" ", "");
			}
		}
		return new Pair<String,String>(pre,suff);
	}
	
	private String getIdentifierFromInstanceName(String instanceName, ArrayList<GenericInstance> tripleObject){
		for(GenericInstance go:tripleObject){
			if(go.getInstanceName().toLowerCase().equals(instanceName.toLowerCase()))
				return go.getIdentifier().getAttribute().getAttributeValue().toString();
		}
		return "";
	}
	
	
}

