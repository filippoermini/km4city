package genericInstance;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeParam{
	
	private Object object;
	private boolean isValueEspression;
	private boolean isForegoingValue;
	private boolean isPreviusState;
	
	public AttributeParam(Object val){
		if (val == null || val.getClass() == String.class )
			this.object = val;
		else{
			
			try {
				Class<?> clazz = null;
				Constructor<?> ctor;
				clazz = Class.forName(val.getClass().getName());
				ctor = clazz.getConstructor(val.getClass());
				this.object = ctor.newInstance(new Object[] { val });
			}catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try{
			String v = val.toString();
			this.isForegoingValue  = belongToClass("@[\\[]+[\\w-']*+[\\]]+[{]+[\\w-]*+[}]", v); //@['id']{nomeproprietà}
			this.isValueEspression = belongToClass("[$]+[{]+[\\w-]*+[}]", v); //${nomeproprietà}
			this.isPreviusState = belongToClass("#[\\[]+[\\d+]*+[\\]]+[\\[]+[\\w-']*+[\\]]+[{]+[\\w-]*+[}]", v); //#[indice]['id']{nomeproprietà}
		}catch(Exception e){

			isValueEspression = false;
		}
		
	}

	public Object getObject() {
		return object;
	}
	
	public void setObject(Object obj){
		if(!this.object.getClass().equals(String.class)){
			try {
				Class[] cArg = new Class[1];
		        cArg[0] = String.class;
				Method method = this.object.getClass().getMethod("setStringValue",cArg);
				Object[] param = new Object[1];
				param[0] = obj;
				method.invoke(this.object, param);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				
			}
		}else{
			this.object = obj;
		}
		
	}
	public boolean isPreviusStateValue() {
		return isPreviusState;
	}
	public boolean isForegoingValue() {
		return isForegoingValue;
	}
	public boolean isValueEspression() {
		return isValueEspression;
	}	
	public String toString(){
		return "Object: "+object+" "+(isValueEspression?"isValueExpression":"");
	}
	
	public static boolean belongToClass(String regexClass,String value){
		Pattern pattern = Pattern.compile(regexClass);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}
}