package GenericClass;

import java.util.HashMap;
import java.util.Random;

import GenericClass.GenericAttribute.Attribute;


public class GenericCommand {

	public class GenerateIntValue implements Command<Integer>{

		private int max;
		private int min;
		
		public GenerateIntValue(int max, int min) {
			this.max = max;
			this.min = min;
		}
		@Override
		public Integer valueGenerator() {
			Random rand = new Random();
		    int randomNum = rand.nextInt((max - min) + 1) + min;
		    return randomNum;
		}
	}
	public class GenerateFloatValue implements Command<Float>{

		private float max;
		private float min;
		
		public GenerateFloatValue(float max, float min) {
			this.max = max;
			this.min = min;
		}
		@Override
		public Float valueGenerator() {
			Random rand = new Random();
		    float randomNum = rand.nextFloat() * (max - min) + min;
		    return randomNum;
		}
	}
	private CommandFactory genericCommand;
	public GenericCommand(Attribute<?> att){
		genericCommand.init(att.getMax(), att.getMin());
	}
	public class CommandFactory {
		private final HashMap<String, Command>	commands;
		
		private CommandFactory() {
			commands = new HashMap<>();
		}

		public void addCommand(String name, Command command) {
			commands.put(name, command);
		}
		
		public void executeCommand(String name) {
			if (commands.containsKey(name)) {
				commands.get(name).valueGenerator();
			}
		}

		
		/* Factory pattern */
		public CommandFactory init(Object max, Object min) {
			CommandFactory cf = new CommandFactory();
			cf.addCommand("integer",new GenericCommand.GenerateIntValue((int)max, (int)min));
			cf.addCommand("float",new GenerateFloatValue((float)max, (float)min));

			return cf;
		}
	}
}
