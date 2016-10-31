package run;

import org.apache.log4j.Logger;





public class Main{
	
	public static void main(String[] args){
		
		DataSimulator TrafficSimulator = new DataSimulator(args[0], args[1],"Traffic Simulator");
		TrafficSimulator.run();
		
	}
	
	
}
