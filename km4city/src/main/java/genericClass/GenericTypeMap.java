package genericClass;

import java.util.HashMap;

public class GenericTypeMap {

	private static GenericTypeMap genericTypeMap = new GenericTypeMap();
	private static GenericCommand commandMap;
	private static final HashMap<String,String> ClassMap = new HashMap<String,String>(){{
		put("integer","java.lang.Integer");
		put("float","java.lang.Float");
		put("datetime","java.lang.String");
		put("uuid","java.lang.String");
		put("id","java.lang.String");
		put("hourdependent","java.lang.String");
		put("fromset","java.lang.String");
		put("string","java.lang.String");
		put("query","java.lang.String");
		put("md5","java.lang.String");
		put("valueexpression","java.lang.String");
	}};
	
	private GenericTypeMap(){
		commandMap = new GenericCommand();
	}
	
	public static GenericTypeMap getInstance(){
		return genericTypeMap;
	}
	
	protected Object getValue(String type,Object... args){
		return commandMap.getCommand().executeCommand(type, args);
	}
	protected String getType(String key){
		return ClassMap.get(key);
	}
}
