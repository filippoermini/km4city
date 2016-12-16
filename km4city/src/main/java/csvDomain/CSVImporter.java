package csvDomain;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

import Application.CommonValue;
import csvDomain.CSVParam.Day;

public class CSVImporter {
	
	public final static Logger logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
	public static class ProfileRecord{
		
		protected String profileID;
		protected Day dayType;
		protected String timeSlot;
		protected String value;
		
		public ProfileRecord(String line) throws Exception{
			String[] value = line.split(",");
			if(value.length==4){
				this.profileID = value[0];
				this.dayType = CSVParam.getDay(value[1]);
				this.timeSlot = value[2];
				this.value = value[3];
			}else{
				throw new Exception();
			}
		}
		
	}
	public static CSVProfiles importProfile(String file) throws Exception{
		CSVProfiles csvProfiles= new CSVProfiles();
		String line;
		try (
		    InputStream fis = new FileInputStream(file);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader br = new BufferedReader(isr);
		) {
			int i=0;
			int j = 0;
		    while ((line = br.readLine()) != null) {
		    	if(i!=0){
			    	ProfileRecord record = new ProfileRecord(line);
			    	Profiles profiles;
			    	if((profiles = csvProfiles.getProfilesById(record.profileID)) != null){
			    		//esiste già un profilo con quell'id
			    		DayProfile dayProfile;
			    		if((dayProfile = profiles.getProfileByDay(record.dayType)) != null){
			    			//esite già un profilo giornaliero
			    			dayProfile.setTimeSlot(j, Double.parseDouble(record.value));
			    			j++;
			    		}else{
			    			//creo un profilo giornaliero
			    			j = 0;
			    			dayProfile = new DayProfile(record.dayType);
			    			dayProfile.setTimeSlot(j, Double.parseDouble(record.value));
			    			profiles.add(dayProfile);
			    			//se esiste almeno un profilo giornaliero controllo se sono stati inseriti 96 timeSlot
			    			if(profiles.length()>1){
			    				int index = profiles.length()-2;
			    				if(profiles.getDayProfileByIndex(index).getTimeSlotValue().length!=96){
			    					logger.error("CVS Error: incorrect number of time slot - ProfileId: "+profiles.getProfileId()
			    					+"Day:"+profiles.getDayProfileByIndex(index).getDay().toString()+" ");
			    					logger.fatal("Process interrupted");
			    					System.exit(-1);
			    				}
			    			}
			    			j++;
			    		}
			    	}else{
			    		profiles = new Profiles(record.profileID);
			    		csvProfiles.add(profiles);
			    		//se nin ono al primo inserimento controllo se il profilo inserito contiene almeno il
			    		//profilo any
			    		if(csvProfiles.length()>1){
			    			int index = csvProfiles.length()-2;
			    			if(csvProfiles.getProfileByIndex(index).getProfileByDay(Day.any) == null){
			    				logger.error("CVS Error: day profile missing: any - Profile: "+csvProfiles.getProfileByIndex(index).getProfileId());
			    				logger.fatal("Process interrupted");
			    				System.exit(-1);
			    			}
			    		}
			    	}
			    }
		    	i++;
			}
		}
		return csvProfiles;
	}
}
