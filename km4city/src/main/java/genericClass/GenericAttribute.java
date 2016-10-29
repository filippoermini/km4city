package genericClass;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import XMLDomain.Tree.Class.Properties.Prop;
import genericClass.GenericAttribute.Attribute;

public class GenericAttribute {

	public class Attribute<T>{
		private final Class<T> typeParameterClass;
		private T attributeValue;
		private HashMap<String,Object> paramList;
		
		
		public Attribute(Class<T> typeParameterClass){
			this.typeParameterClass = typeParameterClass;
			paramList = new HashMap<>();
		}
		
		public Attribute(Attribute<T> a){
			if(a!=null){
				typeParameterClass = a.typeParameterClass;
				attributeValue = a.attributeValue;
				paramList = a.paramList;
			}else{
				typeParameterClass = null;
			}
		}
		
		public void setParamList(HashMap<String,Object> param){
			this.paramList = param;
		}
		
		public void putParam(String key, Object param){
			this.paramList.put(key, param);
		}
		
		public Object getParam(String key){
			return this.paramList.get(key);
		}
		public T generateAttributeValue(){
			return null;
		}
		
		public HashMap<String,Object> getAttributeList(){
			return paramList;
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
		
		public String gettAttributeValue(){
			return this.attributeValue.toString();
		}
		
		public void setValue(String type,Object value){	
			this.attributeValue = (T) genericTypeMap.getValue(type, value);
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
			return "value: "+this.attributeValue+"\n"+getObjectList();
					
		}
	}

	final static GenericTypeMap genericTypeMap = GenericTypeMap.getInstance();
	final static Logger logger = Logger.getLogger(GenericAttribute.class);
	private String valueExpression;
	private String uri;
	private Attribute<?> attribute;
	private String attributeKey;
	private boolean primaryKey;
	private boolean externalKey;
	private boolean constrain;
	private String type; 
	private String name;
	private GenericObject externalClassObject;
	
	
	
	public GenericAttribute(Prop prop){
		
		this.externalClassObject = null;
		this.name = (prop.getKey().split("/")[prop.getKey().split("/").length-1]).split("#")[(prop.getKey().split("/")[prop.getKey().split("/").length-1]).split("#").length-1];;
		this.attributeKey =  prop.getKey();
		this.valueExpression = prop.getValueExpression();
		externalKey = false;
		this.uri = prop.getUri();
		this.type = prop.getType();
		String className = genericTypeMap.getType(prop.getType().toLowerCase());
		primaryKey = (prop.getKey().toLowerCase().contains("identifier"));
		externalKey = (className == null);
		constrain = (valueExpression != null);
		if(!externalKey){
			try {
				Class clazz = java.lang.Class.forName(className);
				Attribute attribute = new Attribute<>(clazz);
				attribute.setParamList(prop.getAttributeList());
				this.attribute = attribute;
			} catch (ClassNotFoundException | SecurityException | IllegalArgumentException e) {
				// TODO Auto-generated catch block
				logger.error("Acquiring properties Error: "+e.getMessage());
			}
		}
	}
	
	
	
	public GenericAttribute(GenericAttribute ga) {
		
		this.valueExpression = ga.valueExpression;
		this.uri = ga.uri;
		this.attribute = new Attribute<>(ga.attribute);
		this.attributeKey = ga.attributeKey;
		this.primaryKey = ga.primaryKey;
		this.externalKey = ga.externalKey;
		this.constrain = ga.constrain;
		this.type = ga.type;
		this.name = ga.name;
		this.externalClassObject = ga.externalClassObject;
	}



	public String toString(){
		return  "Attribute name: "+getAttributeName()+"\n"
				+"key value:"+this.getAttributeKey()+"\n"
				+(isPrimaryKey()?"is primary\n":"")
				+(isExternalKey()?"is external\n":"")
				+(isConstrain()?"is constrain\n":"")
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
		return valueExpression;
	}

	public String getUri() {
		return uri;
	}

	public void setAttribute(Attribute<?> att){
		this.attribute = att;
	}
	public void setExternalClassObject(GenericObject go){
		this.externalClassObject = go;
		this.attribute = go.getIdentifier().getAttribute();
	}
	
	public GenericObject getExternalClassObject(){
		return this.externalClassObject;
	}
	public Attribute<?> getAttribute() {
		if(attribute == null) {
			Class clazz = String.class;
			attribute = new Attribute<>(clazz);
		}
		return attribute;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public boolean isExternalKey() {
		return externalKey;
	}
	
	public boolean isConstrain(){
		return constrain;
	}

	public String getType() {
		return type;
	}
	public HashMap<String,Object> getAttributeList(){
		return this.getAttribute().paramList;
	}
	
	
	
	
}
