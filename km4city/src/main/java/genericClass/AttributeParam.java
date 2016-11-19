package genericClass;

public class AttributeParam{
	
	private Object object;
	private boolean isValueEspression;
	
	public AttributeParam(Object val){
		this.object = val;
		try{
			String v = (String) val;
			this.isValueEspression = v.contains("$");
		}catch(Exception e){
			isValueEspression = false;
		}
		
	}

	public Object getObject() {
		return object;
	}
	
	public void setObject(Object obj){
		this.object = obj;
	}
	public boolean isValueEspression() {
		return isValueEspression;
	}	
	public String toString(){
		return "Object: "+object+" "+(isValueEspression?"isValueExpression":"");
	}
}