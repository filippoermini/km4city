package genericClass;

import java.util.Calendar;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import genericClass.GenericAttribute.Attribute;
import genericClass.GenericCommand.GenerateFloatValue;


public class GenericCommand {

	public class GenerateIntValue implements Command<Integer>{

		@Override
		public Integer valueGenerator(Object... args) {
			Random rand = new Random();
			HashMap<String,Object> param = (HashMap<String, Object>) args[0];
			int min = Integer.parseInt((String) param.get("minValue"));
			int max = Integer.parseInt((String) param.get("maxValue"));
		    int randomNum = rand.nextInt((max - min) + 1) + min;
		    return randomNum;
		}
	}
	public class GenerateFloatValue implements Command<Float>{
		
		@Override
		public Float valueGenerator(Object... args) {
			Random rand = new Random();
			HashMap<String,Object> param = (HashMap<String, Object>) args[0];
			float min = Float.parseFloat((String) param.get("minValue"));
			float max = Float.parseFloat((String) param.get("maxValue"));
		    float randomNum = rand.nextFloat() * (max - min) + min;
		    return randomNum;
		}
	}
	public class GenerateUIDValue implements Command<String> {
		
		@Override
		public String valueGenerator(Object... args) {
			return UUID.randomUUID().toString();
		}
	}
	
	public class GenerateDateTimeValue implements Command<String> {

		@Override
		public String valueGenerator(Object... args) {
			HashMap<String,Object> param = (HashMap<String, Object>) args[0];
			String min = (String) param.get("minValue");
			String max = (String) param.get("maxValue");
			if((max=="" || max == null) && (min == "" || min == null)){
				
				return  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
                        .withZone(ZoneOffset.systemDefault())
                        .format(Instant.now())
                        .toString();
			}
			
			Random random = new Random();
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").withZone(ZoneOffset.systemDefault());
			LocalDate maxDate = LocalDate.parse(max, df);
			LocalDate minDate = LocalDate.parse(min, df);
			
			int minDay = (int) LocalDate.of(minDate.getYear(), minDate.getMonth(), minDate.getDayOfMonth()).toEpochDay();
			int maxDay = (int) LocalDate.of(maxDate.getYear(), maxDate.getMonth(), maxDate.getDayOfMonth()).toEpochDay();
			long randomDay = minDay + random.nextInt(maxDay - minDay);

			LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
			return randomDate.toString();
			
		}
		
	}
	
	public class GenerateHourDependentValue implements Command<String>{

		@Override
		public String valueGenerator(Object... args) {
			//questa tipologia di dato varia in base al valore dell'ora in cui viene generato il valore
			//in base al valore di riferimento definito per ogni ora si determina il valore della simulazione dato un coefficiente di scarto
			// il coefficiente di scarto è un valore intero e serve a limiare il range di variazione del valore orario.
			HashMap<String,Object> param = (HashMap<String, Object>) args[0];
			String[] hourValue = ((String) param.get("hourValue")).split(";");
			Calendar rightNow = Calendar.getInstance();
			int hour = rightNow.get(Calendar.HOUR_OF_DAY);
			String r = "";
			if(((String) param.get("range")).split(";").length > 1){
				r = ((String) param.get("range")).split(";")[hour];
			}else {
				r = (String) param.get("range");
			}
			Random rand = new Random();
			if(((String) param.get("uri")).contains("float")){
				//i valori orari e il range sono float
				float refValue = Float.parseFloat(hourValue[hour]);
				float range = Float.parseFloat(r);
				float min = refValue - range;
				float max = refValue + range;
			    float randomNum = rand.nextFloat() * (max - min) + min;
			    return randomNum+"";
			}else{
				//i valori orari e il range sono int
				int refValue = Integer.parseInt(hourValue[hour]);
				int range = Integer.parseInt(r);
				int min = refValue - range;
				int max = refValue + range;
			    int randomNum = (int)(rand.nextFloat()* (max - min) + min);
			    return randomNum+"";
			}
		}
		
	}
	public class GenerateFromSetValue implements Command<String>{

		@Override
		public String valueGenerator(Object... args) {
			HashMap<String,Object> param = (HashMap<String, Object>) args[0];
			String[] set = ((String) param.get("fromSet")).split(";");
			Random rand = new Random();
			int bound = set.length;
			int index = rand.nextInt(bound);
			return set[index];
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
		
		public Object executeCommand(String name,Object... args) {
			if (commands.containsKey(name.toLowerCase())) {
				return commands.get(name.toLowerCase()).valueGenerator(args);
			}
			return null;
		}

		/* Factory pattern */
		public void init() {
			
			this.addCommand("integer",new GenerateIntValue());
			this.addCommand("float",new GenerateFloatValue());
			this.addCommand("uid", new GenerateUIDValue());
			this.addCommand("datetime", new GenerateDateTimeValue());
			this.addCommand("hourdependent", new GenerateHourDependentValue());
			this.addCommand("fromset", new GenerateFromSetValue());
		}
	}

	
}
