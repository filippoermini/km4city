package Application;

public class CommonValue {

	private static CommonValue instance;
	
	private CommonValue(String name) { simulationName = name;}
	private String simulationName;

	public static CommonValue getInstance( ) {
	      return instance;
	}

	public static void init(String name){
		instance = new CommonValue(name);
	}
	public String getSimulationName(){
		return this.simulationName;
	}
}
