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
		
		public void setValue(){
			
		}
	}
	
	

	

	final static Logger logger = Logger.getLogger(GenericAttribute.class);
	private String valueExpression;
	private String uri;
	private Attribute<?> attribute;
	private String attributeKey;
	private boolean primaryKey;
	private boolean externalKey;
	private boolean constrain;
	private String type; 
	private static final HashMap<String,String> ClassMap = new HashMap<String,String>(){{
		put("integer","java.lang.Integer");
		put("float","java.lang.Float");
		put("datetime","java.util.Date");
		put("uid","java.lang.Integer");
		put("id","java.lang.Integer");
	}};
	private static GenericCommand command;
	
	public GenericAttribute(Prop prop){
		
		this.attributeKey =  prop.getKey();
		//this.value = prop.getValue();
		externalKey = false;
		this.uri = prop.getUri();
		this.type = prop.getType();
		String className = ClassMap.get(prop.getType().toLowerCase());
		primaryKey = (prop.getType().toLowerCase().contains("id"));
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

	public String getAttributeKey() {
		return attributeKey;
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
