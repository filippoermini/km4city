package GenericClass;

import java.util.HashMap;

public class GenericTypeMap {

	private static GenericTypeMap genericTypeMap = new GenericTypeMap();
	private static GenericCommand commandMap;
	private static final HashMap<String,String> ClassMap = new HashMap<String,String>(){{
		put("integer","java.lang.Integer");
		put("float","java.lang.Float");
		put("datetime","java.lang.String");
		put("uid","java.lang.String");
		put("id","java.lang.Integer");
	}};
	
	private GenericTypeMap(){
		commandMap = new GenericCommand();
	}
	
	public static GenericTypeMap getInstance(){
		return genericTypeMap;
	}
	
	protected Object getValue(String type,Object max,Object min){
		return commandMap.getCommand().executeCommand(type, max, min);
	}
	protected String getType(String key){
		return ClassMap.get(key);
	}
}
