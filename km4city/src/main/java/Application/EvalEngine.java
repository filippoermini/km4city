package Application;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class EvalEngine {

	private static EvalEngine instance = new EvalEngine();
	private ScriptEngineManager manager;
	private ScriptEngine engine;
	private EvalEngine(){
		manager = new ScriptEngineManager();
		engine = manager.getEngineByName("js");
		
	}
	public ScriptEngine getEngine(){
		return engine;
	}
	
	public static EvalEngine getInstance(){
		return instance;
	}
}
