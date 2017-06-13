package com.disit.km4c.genericInstance;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;

import com.disit.km4c.application.CommonValue;
import com.disit.km4c.application.EvalEngine;
import com.disit.km4c.application.RDFconnector;
import com.disit.km4c.application.RDFconnector.RepositoryManager;
import com.disit.km4c.csvDomain.CSVImporter;
import com.disit.km4c.csvDomain.CSVProfiles;
import com.disit.km4c.exceptions.MissingAttributeExceptions;
import com.disit.km4c.xmlDomain.Tree;
import com.disit.km4c.xmlDomain.Tree.QueryInfo;



public class GenericCommand {
	
	final static Logger logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
	final static EvalEngine javascriptEngine = EvalEngine.getInstance();

	public class GenerateIntValue implements Command<Integer>{

		@Override
		public Integer valueGenerator(Object... args) throws MissingAttributeExceptions {
			
			Random rand = new Random();
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			
			if( param.get("minValue").getObject() != null && param.get("maxValue").getObject() != null){
			
				int min = Integer.parseInt((String) param.get("minValue").getObject());	
				int max = Integer.parseInt((String) param.get("maxValue").getObject());
				int randomNum = rand.nextInt((max - min) + 1) + min;
				return randomNum;
			}else{
				throw new MissingAttributeExceptions("Integer Type required minValue and maxValue attributes");
			}
		}
	}
	
	public class GenerateFloatValue implements Command<Float>{
		
		@Override
		public Float valueGenerator(Object... args) throws MissingAttributeExceptions {
			Random rand = new Random();
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			
			if( param.get("minValue").getObject() != null && param.get("maxValue").getObject() != null){
			
				float min = Float.parseFloat((String) param.get("minValue").getObject());
				float max = Float.parseFloat((String) param.get("maxValue").getObject());
			    float randomNum = rand.nextFloat() * (max - min) + min;
			    return randomNum;
			}else{
				throw new MissingAttributeExceptions("Float Type required minValue and maxValue attributes");
			}
		}
	}
	public class GenerateUUIDValue implements Command<String> {
		
		@Override
		public String valueGenerator(Object... args) {
			return UUID.randomUUID().toString();
		}
	}
	
	public class GenerateDateTimeValue implements Command<String> {

		@Override
		public String valueGenerator(Object... args) {
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			String min = (String) param.get("minValue").getObject();
			String max = (String) param.get("maxValue").getObject();
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
		public String valueGenerator(Object... args) throws MissingAttributeExceptions {
			//questa tipologia di dato varia in base al valore dell'ora in cui viene generato il valore
			//in base al valore di riferimento definito per ogni ora si determina il valore della simulazione dato un coefficiente di scarto
			// il coefficiente di scarto è un valore intero e serve a limiare il range di variazione del valore orario.
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			
			if( param.get("hourValue").getObject() != null && param.get("range").getObject() != null){
			
				String[] hourValue = ((String) param.get("hourValue").getObject()).split(";");
				Calendar rightNow = Calendar.getInstance();
				int hour = rightNow.get(Calendar.HOUR_OF_DAY);
				String r = "";
				if(((String) param.get("range").getObject()).split(";").length > 1){
					r = ((String) param.get("range").getObject()).split(";")[hour];
				}else {
					r = (String) param.get("range").getObject();
				}
				Random rand = new Random();
				if(((String) param.get("uri").getObject()).contains("float")){
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
			}else{
				throw new MissingAttributeExceptions("HourDependent Type required hourValue and range attributes");
			}
		}
		
	}
	public class GenerateFromSetValue implements Command<String>{

		@Override
		public String valueGenerator(Object... args) throws MissingAttributeExceptions {
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			
			if ( param.get("set").getObject() != null){
			
				String[] set = ((String) param.get("set").getObject()).split(";");
				Random rand = new Random();
				int bound = set.length;
				int index = rand.nextInt(bound);
				return set[index];
			}else{
				throw new MissingAttributeExceptions("FromSet Type required set attribute");
			}
		}
		
	}
	
	public class GenerateQueryValue implements Command<String>{
		@Override
		public String valueGenerator(Object... args) throws MissingAttributeExceptions {
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			
			Tree.QueryInfo queryInfo = (QueryInfo) param.get("queryInfo").getObject();
			
			if (queryInfo.getServer() == null) throw new MissingAttributeExceptions("Query Type required server attribute");
			if (queryInfo.getQuery() == null) throw new MissingAttributeExceptions("Query Type required query attribute");
			
			
			RepositoryManager rm = RDFconnector.getInstance(queryInfo.getServer());
			TupleQueryResult results = rm.SPARQLExecute(queryInfo.getQuery());
			String bindingField;
			if (queryInfo.getBindingValue().isEmpty()){
				bindingField = getBindingField(queryInfo.getQuery());
			}else{
				bindingField = queryInfo.getBindingValue();
			}
			if(results.hasNext()) {
				BindingSet bindingSet = results.next();
				Value value = bindingSet.getValue(bindingField);
				if(value==null){
					return "";
				}
				return value.stringValue();
				
			}
			return "";
		}
		
		private String getBindingField(String query){
			
		    Pattern pattern = Pattern.compile("select(.*?)where");
		    Matcher matcher = pattern.matcher(query);
		    while (matcher.find()) {
		        String value = matcher.group(1);
		        value = value.replace("select", "").replace("where", "").replace("SELECT", "").replace("WHERE","").replace("Select", "").replace("Where", "").trim();
		        String[] bindingValue = value.split("[?]");
		        if(bindingValue.length>0){
		        	if(bindingValue.length>2){
		        		logger.warn("The query should return only one column, find: "+bindingValue.length+". ");
		        		logger.warn("Output column selected: "+bindingValue[1]);
		        		return bindingValue[1];
		        	}else{
		        		return bindingValue[1];
		        	}
		        }
		    }
		    logger.warn("Fail to retrive outpur field, value set to 'id'");
        	return "id";
		}
		
	}
	
	
	public class GenerateMD5Value implements Command<String>{
		@Override
		public String valueGenerator(Object... args) throws MissingAttributeExceptions {
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			
			if (param.get("md5String").getObject() != null){ 
			
				String arg = (String) param.get("md5String").getObject();
				try {
					String md5String = md5(arg);
					return md5String;
				} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					logger.error("MD5 encription error: "+e.getMessage());
				}
				return "";
			}else{
				throw new MissingAttributeExceptions("MD5Value Type required md5String attribute ");
			}
		}
		
		private String md5(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException{
			StringBuffer hexString = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));

			for (int i = 0; i < hash.length; i++) {
			    if ((0xff & hash[i]) < 0x10) {
			        hexString.append("0"
			                + Integer.toHexString((0xFF & hash[i])));
			    } else {
			        hexString.append(Integer.toHexString(0xFF & hash[i]));
			    }
			}
			return hexString.toString();
		}
		
	}
	
	public class GenerateExpressionValue implements Command<String>{

		@Override
		public String valueGenerator(Object... args) throws MissingAttributeExceptions {
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			
			if (param.get("valueExpression").getObject() != null){
			
				String expression = (String) param.get("valueExpression").getObject();
				String format = (String) param.get("format").getObject();
				if (format == null || format == "") format = "%s";
				try {
					Object obj = javascriptEngine.getEngine().eval(expression);
					return getOutputFormat(obj,format);
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					logger.error("Evaluate expression error: "+ e.getMessage());
				}
				return null;
			}else{
				throw new MissingAttributeExceptions("ValueExpression Type required valueExpression attribute");
			}
		}
		
	}
	
	public class GenerateProfileDependentvalue implements Command<String>{

		@Override
		public String valueGenerator(Object... args) throws MissingAttributeExceptions {
			HashMap<String,AttributeParam> param = (HashMap<String, AttributeParam>) args[0];
			
			if( param.get("profilesFile").getObject() != null && param.get("profileId").getObject() != null && param.get("maxValue").getObject() != null && param.get("variance").getObject() != null){
			
				String file = (String) param.get("profilesFile").getObject();
				String profileID = (String) param.get("profileId").getObject();
				double maxValue = Double.parseDouble((String) param.get("maxValue").getObject());
				double variance = Double.parseDouble((String) param.get("variance").getObject());
				CSVProfiles csvProfile;
				try {
					 csvProfile = CSVImporter.importProfile(file);
					 double value = csvProfile.getValue(profileID);
					 Random r = new Random();
					 double error = r.nextGaussian()*variance;
					 return value*maxValue + error+"";
					 
					 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("CSV Importer error: "+e.getMessage());
					logger.error("Process interrupted");
					System.exit(-1);
				}
				return null;
			}else{
				throw new MissingAttributeExceptions("ProfileDependent Type required profilesFile, profileId, maxValue and variance attributes");
			}
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
	
	private String getOutputFormat(Object obj,String format){
		Formatter formatter = new Formatter();
		if(format.contains("%d")){
			try{
				int num = Integer.parseInt(obj.toString());
				return formatter.format(format, num).toString();
			}catch(NumberFormatException ex){
				logger.error("Cannot cast value"+ obj.toString()+ " to integer value" );
				return obj.toString();
			}
		}
		if(format.contains("%") && format.contains("f")){
			try{
				double num = Double.parseDouble(obj.toString());
				String output = formatter.format(format, num).toString();
				return output.replace(",", ".");
			}catch(NumberFormatException ex){
				logger.error("Cannot cast value"+ obj.toString()+ " to double value" );
				return obj.toString();
			}
		}
		return formatter.format(format, obj.toString()).toString();
	}
	
	public class CommandFactory {
		private final HashMap<String, Command>	commands;
		
		private CommandFactory() {
			commands = new HashMap<>();
		}

		public void addCommand(String name, Command command) {
			commands.put(name, command);
		}
		
		public Object executeCommand(String name,Object... args) throws MissingAttributeExceptions {
			if (commands.containsKey(name.toLowerCase())) {
				return commands.get(name.toLowerCase()).valueGenerator(args);
			}
			return null;
		}
		
		public void init() {
			
			this.addCommand("integer",new GenerateIntValue());
			this.addCommand("float",new GenerateFloatValue());
			this.addCommand("uuid", new GenerateUUIDValue());
			this.addCommand("datetime", new GenerateDateTimeValue());
			this.addCommand("hourdependent", new GenerateHourDependentValue());
			this.addCommand("fromset", new GenerateFromSetValue());
			this.addCommand("query", new GenerateQueryValue());
			this.addCommand("md5", new GenerateMD5Value());
			this.addCommand("valueexpression", new GenerateExpressionValue());
			this.addCommand("profiledependent", new GenerateProfileDependentvalue());
		}
	}

	
}
