package cvsDomain;

import java.util.ArrayList;
import java.util.Iterator;

import cvsDomain.CVSParam.Day;

public class Profiles {

	private ArrayList<DayProfile> profiles;
	private String profileId;
	
	public Profiles(String profileId){
		this.profiles = new ArrayList<>();
		this.profileId = profileId;
	}
	
	public void add(DayProfile dayProfile){
		this.profiles.add(dayProfile);
	}
	
	public String getProfileId(){
		return this.profileId;
	}
	
	private DayProfile getProfileByDay(Day day){
		Iterator<DayProfile> it = this.profiles.iterator();
		DayProfile dayProfile;
		while(it.hasNext()){
			dayProfile = it.next();
			if(dayProfile.getDay().equals(day))
				return dayProfile;
		}
		return null;
	}
	
	public double getTimeSlotValue(Day day){
		return 1f; // TODO estrarre il valore orario ricavare il time slot relativo  
	}
}
