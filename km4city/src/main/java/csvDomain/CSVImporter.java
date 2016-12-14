package csvDomain;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import csvDomain.CSVParam.Day;

public class CSVImporter {
	
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
			    	if((profiles = csvProfiles.geyProfilesById(record.profileID)) != null){
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
			    			j++;
			    		}
			    	}else{
			    		profiles = new Profiles(record.profileID);
			    		csvProfiles.add(profiles);
			    	}
			    }
		    	i++;
			}
		}
		return csvProfiles;
	}
}
