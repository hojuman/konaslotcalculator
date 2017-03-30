package wtc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import wtc.obj.Athlete;
import wtc.parse.BibListParser;
import wtc.process.AgeGroupCounter;
import wtc.process.CountryCounter;
import wtc.process.KonaSlotCalculator;

public class Test {
	public static void main(String[] args) {
		String filename = args[0];
	    try{
	    	HashMap<String,ArrayList<Athlete>> agegroups = BibListParser.parseBibList(filename);
	    	//AgeGroupCounter.registrantsByAgeGroup(agegroups, true);
	    	//CountryCounter.athleteCountByCountry(agegroups, true);
	    	//CountryCounter.getAthletesByCountry(agegroups, "CAN", true);
	    	KonaSlotCalculator.calculateKonaSlots(agegroups, 75, true);
	    } catch(IOException e) {
	    	System.out.println("Exception caught: " + e);
	    	e.printStackTrace();
	    } catch(Exception e) {
	    	System.out.println("Exception caught: " + e);
	    	e.printStackTrace();	    	
	    }
	}
}
