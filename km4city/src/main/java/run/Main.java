package run;

import java.util.Scanner;

import org.apache.log4j.Logger;





public class Main{
	
	public static void main(String[] args){
		
		String xml = "";
		String simulationName = "";
        if(args.length <=1){
            System.out.println("Arguments requires\n");
            printUsage();
            System.exit(0);
        }
        int i=0;
        String arg = "";
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];
            if (arg.equals("-x")) {
                if (i < args.length){
                	xml = args[i++];
                }else{
                	System.err.println("-x: requires the xml file");
                    System.exit(-1);
                }
            }else if(arg.equals("-n")){
            	if (i < args.length){
            		simulationName = args[i++];
                }else{
                	System.err.println("-x: requires the simulation's name");
                    System.exit(-1);
                }
            }else if(arg.equals("-help")){
            	printUsage();
            	System.exit(0);
            }
        }
		DataSimulator Simulator = new DataSimulator(xml,simulationName);
		Simulator.run();
		
	}
	
	public static void printUsage(){
		System.out.println("Usage:\nData Simulator for KM4C "
                + "[-x <xml file>] [-n <simulation name >] ");
		}
	
}
