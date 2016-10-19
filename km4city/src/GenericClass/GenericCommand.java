package GenericClass;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;


import GenericClass.GenericAttribute.Attribute;
import GenericClass.GenericCommand.GenerateFloatValue;


public class GenericCommand {

	public class GenerateIntValue implements Command<Integer>{

		@Override
		public Integer valueGenerator(Integer max,Integer min) {
			Random rand = new Random();
		    int randomNum = rand.nextInt((max - min) + 1) + min;
		    return randomNum;
		}
	}
	public class GenerateFloatValue implements Command<Float>{
		
		@Override
		public Float valueGenerator(Float max, Float min) {
			Random rand = new Random();
		    float randomNum = rand.nextFloat() * (max - min) + min;
		    return randomNum;
		}
	}
	public class GenerateUIDValue implements Command<String> {
		
		@Override
		public String valueGenerator(String max, String min) {
			return UUID.randomUUID().toString();
		}
	}
	
	private CommandFactory genericCommand;
	public GenericCommand(){
		genericCommand = new CommandFactory();
		genericCommand.init();
	}
	public CommandFactory getCommand(){
		return genericCommand;
	}
	
	public class CommandFactory {
		private final HashMap<String, Command>	commands;
		
		private CommandFactory() {
			commands = new HashMap<>();
		}

		public void addCommand(String name, Command command) {
			commands.put(name, command);
		}
		
		public Object executeCommand(String name,Object max,Object min) {
			if (commands.containsKey(name.toLowerCase())) {
				return commands.get(name).valueGenerator(max,min);
			}
			return null;
		}

		/* Factory pattern */
		public void init() {
			CommandFactory cf = new CommandFactory();
			this.addCommand("integer",new GenerateIntValue());
			this.addCommand("float",new GenerateFloatValue());
			this.addCommand("UID", new GenerateUIDValue());
			
		}
	}

	
}
