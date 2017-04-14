package com.disit.km4c.csvDomain;

public class CSVParam {

	public static enum Day{
		sun, mon, tue, wed,
	    thu, fri, sat, any,
	}
	public static Day getDay(String day){
		for(Day d:Day.values()){
			if (d.toString().equals(day)){
				return d;
			}
		}
		return null;
	}
}
