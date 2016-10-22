package GenericClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import XMLDomain.Tree.Class.Properties.Prop;

public class GenericAttribute {

	public class Attribute<T>{
		private final Class<T> typeParameterClass;
		private T maxValue;
		private T minValue;
		private T attributeValue;
		
		
		public Attribute(Class<T> typeParameterClass){
			this.typeParameterClass = typeParameterClass;
		}
		
		public void setMax(T value){
			this.maxValue = value;
		}
		public void setMin(T value){
			this.minValue = value;
		}
		
		public T getMax(){
			return this.maxValue;
		}
		
		public T getMin(){
			return this.minValue;
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
		
		public String gettAttributeValue(){
			return this.attributeValue.toString();
		}
		
		public void setValue(String type,Object max, Object min){	
			this.attributeValue = (T) genericTypeMap.getValue(type, max, min);
		}
		
		public String toString(){
			return "value: "+this.attributeValue+"\n"
					+"max: "+this.minValue+"\n"
					+"min: "+this.maxValue+"\n";
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
	private String[] hourValue;
	private String rangre;
	private GenericObject externalClassObject;
	
	
	
	public GenericAttribute(Prop prop){
		
		this.externalClassObject = null;
		this.name = (prop.getKey().split("/")[prop.getKey().split("/").length-1]).split("#")[(prop.getKey().split("/")[prop.getKey().split("/").length-1]).split("#").length-1];;
		this.attributeKey =  prop.getKey();
		this.valueExpression = prop.getValueExpression();
		this.hourValue = prop.getHourValue()!=null?prop.getHourValue().split(";"):null;
		this.rangre = prop.getRange();
		externalKey = false;
		this.uri = prop.getUri();
		this.type = prop.getType();
		String className = genericTypeMap.getType(prop.getType().toLowerCase());
		primaryKey = (prop.getKey().toLowerCase().contains("identifier"));
		externalKey = (className == null);
		constrain = (valueExpression != null);
		if(!externalKey && !constrain){
			try {
				Class clazz = java.lang.Class.forName(className);
				Attribute attribute = new Attribute<>(clazz);
				
				if(prop.getMaxValue() != null && prop.getMinValue() != null){
					Constructor<?> constructor = clazz.getConstructor(String.class);
					attribute.setMax(constructor.newInstance(prop.getMaxValue()));
					attribute.setMin(constructor.newInstance(prop.getMinValue()));
				}
				this.attribute = attribute;
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				logger.error("Acquiring properties Error: "+e.getMessage());
			}
		}
	}
	
	public String toString(){
		return  "Attribute name: "+getAttributeName()+"\n"
				+"key value:"+this.getAttributeKey()+"\n"
				+(getType() != null?"Type: "+getType()+"\n":"")
				+(getUri() != null?"uri: "+getUri()+"\n":"")
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
	
	
	
	
}
