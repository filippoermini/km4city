package com.disit.km4c.csvDomain;

import java.awt.image.DataBufferDouble;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.disit.km4c.application.CommonValue;
import com.disit.km4c.csvDomain.CSVParam.Day;

public class CSVProfiles {

	public final static Logger logger = Logger.getLogger(CommonValue.getInstance().getSimulationName());
	private ArrayList<Profiles> cvsProfiles;
	
	public CSVProfiles(){
		this.cvsProfiles = new ArrayList<>();
	}
	
	public void add(Profiles profiles){
		this.cvsProfiles.add(profiles);
	}
	
	public Profiles getProfilesById(String profileId){
		Iterator<Profiles> it = this.cvsProfiles.iterator();
		Profiles profiles;
		while(it.hasNext()){
			profiles = it.next();
			if(profileId.equals(profiles.getProfileId()))
				return profiles;
		}
		return null;
	}
	
	public double getValue(String profileID){
		
		 LocalDateTime datetime = LocalDateTime.now();
		 Day day = Day.valueOf(datetime.getDayOfWeek().name().substring(0, 3).toLowerCase());
		 int indexOfTimeSlot = (datetime.getHour()*4) + (datetime.getMinute()/15);
		 Profiles profile = this.getProfilesById(profileID);
		 DayProfile dayProfile = null;
		 if(profile != null){
			 dayProfile = profile.getProfileByDay(day)!=null?profile.getProfileByDay(day):profile.getProfileByDay(Day.any);
		 }else{
			 logger.error("Profile id: "+profileID+" do no exist");
			 logger.error("Process interrupted");
			 System.exit(-1);
		 }
		 return dayProfile.getTimeSlotValue()[indexOfTimeSlot];
		 
	}
	
	public Profiles getProfileByIndex(int index){
		if (index<=this.cvsProfiles.size()){
			return this.cvsProfiles.get(index);
		}
		return null;
	}

	public int length(){
		return this.cvsProfiles.size();
	}
}
