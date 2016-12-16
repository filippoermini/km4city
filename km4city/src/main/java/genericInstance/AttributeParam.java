package genericInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AttributeParam{
	
	private Object object;
	private boolean isValueEspression;
	
	public AttributeParam(Object val){
		this.object = val;
		try{
			String v = val.toString();
			this.isValueEspression = v.contains("$");
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
	public boolean isValueEspression() {
		return isValueEspression;
	}	
	public String toString(){
		return "Object: "+object+" "+(isValueEspression?"isValueExpression":"");
	}
}