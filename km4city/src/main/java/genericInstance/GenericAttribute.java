package genericInstance;




import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import XMLDomain.Tree.Instance.Properties.Prop;
import XMLDomain.Tree.Instance;



public class GenericAttribute {

	public class Attribute<T>{
		private final Class<T> typeParameterClass;
		private T attributeValue;
		
		
		
		public Attribute(Class<T> typeParameterClass){
			this.typeParameterClass = typeParameterClass;
		}
		
		public Attribute(Attribute<T> a){
			if(a!=null){
				typeParameterClass = a.typeParameterClass;
				attributeValue = a.attributeValue;
			}else{
				typeParameterClass = null;
			}
		}
		
		
		public T generateAttributeValue(){
			return null;
		}
		
		public void setAttributeValue(String value){
			Constructor<?> constructor;
			try {
				constructor = typeParameterClass.getConstructor(String.class);
				this.attributeValue = (T) constructor.newInstance(value);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				logger.error("set attribute value Error. Value: "+value+" - "+e.getMessage());
			}
			
		}
		
		
		public T getAttributeValue(){
			return this.attributeValue;
		}
		public void setValue(String value){
			this.attributeValue = (T) value;
		}
		public void setValue(String type,Object value){	
			this.attributeValue = (T) genericTypeMap.getValue(type, value);
		}
		public String toString(){
			return "value: "+this.attributeValue+"\n";
		}
		
	}

	final static GenericTypeMap genericTypeMap = GenericTypeMap.getInstance();
	final static Logger logger = Logger.getLogger(GenericAttribute.class);
	private Attribute<?> attribute;
	private String attributeKey;
	private String name;
	private GenericInstance externalInstanceObject;
	private HashMap<String,AttributeParam> paramList;
	private boolean isPrimaryKey;
	private boolean isExternalKey;
	private boolean isUri;
	private boolean isValueExpression;
	private boolean isForegoingValue;
	private boolean isProcessed;
	private boolean isHidden;
	
	
	
	
	public GenericAttribute(Prop prop){
		
		this.externalInstanceObject = null;
		this.name = prop.getName()!=null?prop.getName():(prop.getKey().split("/")[prop.getKey().split("/").length-1]).split("#")[(prop.getKey().split("/")[prop.getKey().split("/").length-1]).split("#").length-1];;
		this.attributeKey =  prop.getKey();
		this.isProcessed = false;
		this.isExternalKey = false;
		this.isUri = prop.isUri();
		String className = genericTypeMap.getType(prop.getType().toLowerCase());
		this.isPrimaryKey = prop.isPrimaryKey();
		this.isExternalKey = (className == null);
		this.isHidden = prop.isHidden();
		this.paramList = new HashMap<String,AttributeParam>();
		this.paramList = prop.getAttributeList();
		
		
		if(!isExternalKey){
			try {
				Class instance = java.lang.Class.forName(className);
				Attribute attribute = new Attribute<>(instance);
				this.attribute = attribute;
			} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
				// TODO Auto-generated catch block
				logger.error("Acquiring properties Error: "+e.getMessage());
			}
		}
		this.isValueExpression = this.attribute==null?false:this.paramContainExpression();
		this.isForegoingValue = this.paramContainForegoingValue();
		
	}
	
	
	
	public GenericAttribute(GenericAttribute ga) {
		
		
		this.attribute = new Attribute<>(ga.attribute);
		this.attributeKey = ga.attributeKey;
		this.isPrimaryKey = ga.isPrimaryKey;
		this.isExternalKey = ga.isExternalKey;
		this.name = ga.name;
		this.externalInstanceObject = ga.externalInstanceObject;
		this.isUri = ga.isUri();
		this.isProcessed = ga.isProcessed;
		this.isValueExpression = ga.isValueExpression;
		this.isHidden = ga.isHidden;
		this.paramList = ga.paramList;
	}
	
	
	public boolean paramContainForegoingValue(){
		Iterator<Entry<String, AttributeParam>> it = this.paramList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String,AttributeParam> pair = (Map.Entry<String,AttributeParam>)it.next();
	    	if (pair.getValue()!=null && pair.getValue().isForegoingValue())
	    		return true;
	    }
	    return false;
	}
	
	public boolean paramContainExpression(){
		Iterator<Entry<String, AttributeParam>> it = this.paramList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry<String,AttributeParam> pair = (Map.Entry<String,AttributeParam>)it.next();
	    	if (pair.getValue()!=null && pair.getValue().isValueEspression())
	    		return true;
	    }
	    return false;
	}
	
	public void setParamList(HashMap<String,AttributeParam> param){
		this.paramList = param;
	}
	
	public void putParam(String key, String param){
		this.paramList.put(key, new AttributeParam(param));
	}
	
	public Object getParam(String key){
		return this.paramList.get(key).getObject();
	}
	public boolean isHidden(){
		return isHidden;
	}
	public boolean isProcessed() {
		return isProcessed;
	}
	
	public boolean isForegoingValue(){
		return isForegoingValue;
	}
		
	public void setProcessed(boolean isProcessed) {
		this.isProcessed = isProcessed;
	}

	public boolean isValueExpression() {
		return isValueExpression;
	}
	private String getObjectList(){
		String str = "";
		Iterator it = this.paramList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	    	str += pair.getKey()+": "+pair.getValue()+"\n";
	    }
	    return str;
	}
	public String toString(){
		return  "Attribute name: "+getAttributeName()+"\n"
				+"key value:"+this.getAttributeKey()+"\n"
				+(isPrimaryKey()?"is primary\n":"")
				+(isExternalKey()?"is external\n":"")
				+"Attribute param" + getObjectList()
				+"Attribute value:\n"+(attribute != null?attribute.toString():"");
							
				
	}
	public String getAttributeName(){
		return name;
	}
	public String getAttributeKey() {
		return attributeKey;
	}
	
	public String getValue(){
		return attribute.toString();
	}

	public String getValueExpression() {
		return (String) this.getParam("valueExpression");
	}

	public String getDatatype() {
		return  (String) this.getParam("datatype");
	}
	
	public String getDatatype(ArrayList<GenericInstance> tripleObject){
		return getUriParam((String) this.getParam("datatype"), tripleObject);
	}

	public void setAttribute(Attribute<?> att){
		this.attribute = att;
	}
	public void setExternalInstanceObject(GenericInstance go){
		this.externalInstanceObject = go;
		this.attribute = go.getIdentifier().getAttribute();
	}
	
	public GenericInstance getExternalInstanceObject(){
		return this.externalInstanceObject;
	}
	public Attribute<?> getAttribute() {
		if(attribute == null) {
			Class instance = String.class;
			attribute = new Attribute<>(instance);
		}
		return attribute;
	}
	
	public boolean isUri(){
		return isUri;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public boolean isExternalKey() {
		return isExternalKey;
	}
	
	public HashMap<String,AttributeParam> getAttributeList(){
		return paramList;
	}

	public String getType() {
		return (String) this.getParam("type");
	}
	
	private String getUriParam(String uri,ArrayList<GenericInstance> tripleObject){
		String resUri = uri;
		if(resUri.contains("$")){
			Matcher m = Pattern.compile("\\{.*?\\}").matcher(resUri);
			while(m.find()){
				String pattern = m.group();
				Matcher m1 = Pattern.compile("[$]+[a-zA-Z_]+").matcher(pattern);
				m1.find();
				String var = m1.group().replace("$", "");
				String value = getIdentifierFromInstanceName(var,tripleObject);
				resUri = resUri.replace(pattern, pattern.replace(var, value).replace("{", "").replace("}", "").replaceAll(" ", ""));
			}
		}
		return resUri;
	}
	private String getIdentifierFromInstanceName(String className, ArrayList<GenericInstance> tripleObject){
		for(GenericInstance go:tripleObject){
			if(go.getInstanceName().toLowerCase().equals(className.toLowerCase()))
				return go.getIdentifier().getAttribute().getAttributeValue().toString();
		}
		return "";
	}
	
	
}
