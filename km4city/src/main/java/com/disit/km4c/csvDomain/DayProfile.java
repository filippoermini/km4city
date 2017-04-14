package com.disit.km4c.csvDomain;

import com.disit.km4c.csvDomain.CSVParam.Day;

public class DayProfile {

	private Day day;
	private double[] timeSlotValue;
	
	public DayProfile(Day day){
		this.day = day;
		this.timeSlotValue = new double[96];
	}
	
	public void setTimeSlot(int index,double value){
		this.timeSlotValue[index] = value;
	}
	
	public Day getDay(){
		return this.day;
	}
	
	public double[] getTimeSlotValue(){
		return this.timeSlotValue;
	}
}
