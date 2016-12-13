package cvsDomain;

import java.util.ArrayList;
import java.util.Iterator;

public class CVSProfiles {

	private ArrayList<Profiles> cvsProfiles;
	
	public CVSProfiles(){
		this.cvsProfiles = new ArrayList<>();
	}
	
	public void add(Profiles profiles){
		this.cvsProfiles.add(profiles);
	}
	
	public Profiles geyProfilesById(String profileId){
		Iterator<Profiles> it = this.cvsProfiles.iterator();
		Profiles profiles;
		while(it.hasNext()){
			profiles = it.next();
			if(profileId.equals(profiles.getProfileId()))
				return profiles;
		}
		return null;
	}
}
