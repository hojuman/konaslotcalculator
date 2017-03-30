package wtc.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import wtc.obj.Athlete;

/**
 * Processes the age group list by country
 * @author Robert
 *
 */
public class CountryCounter {
	/**
	 * Return a hash containing registrants by country
	 * @param agegroups a hash of the agegroups
	 * @param print indicates if this should be printed to Standard out or not
	 * @return Hash containing registrants by country
	 */
	public static HashMap<String,ArrayList<Athlete>> registrantsByCountry(HashMap<String,ArrayList<Athlete>> agegroups) throws Exception{
		HashMap<String,ArrayList<Athlete>> countries = new HashMap<String,ArrayList<Athlete>>();
		if(agegroups==null)
			throw new Exception("Age groups cannot be empty");
		HashMap<String,Integer> count = new HashMap<String,Integer>();
    	Iterator<String> iter = agegroups.keySet().iterator();
    	while(iter.hasNext()) {
    		String key = iter.next();
    		ArrayList<Athlete> athletes = agegroups.get(key);
    		for(Athlete athlete : athletes) {
    			ArrayList<Athlete> country = countries.get(athlete.getCountry());
    			if(country==null) {
    				country = new ArrayList<Athlete>();
    				countries.put(athlete.getCountry(), country);
    			}
    			country.add(athlete);
    		}
    	}
		return countries;
	}
	/**
	 * Return a count of the number of registrants by country in a hash
	 * @param agegroups
	 * @param print indicate if this should be printed to standard out or not
	 * @return hash containing countries and count of athletes in that country
	 * @throws Exception if hash is empty
	 */
	public static HashMap<String,Integer> athleteCountByCountry(HashMap<String,ArrayList<Athlete>> agegroups,boolean print) throws Exception{ 
		HashMap<String,ArrayList<Athlete>> countries = registrantsByCountry(agegroups);
		HashMap<String,Integer> count = new HashMap<String,Integer>();
		Iterator<String> iter = countries.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			ArrayList<Athlete> country = countries.get(key);
			if(print) {
				System.out.println(key + " " + country.size());
			}
			count.put(key, country.size());
		}
		return count;
	}
	/**
	 * Get a list of the athletes for a given country
	 * @param agegroups agegroup hash
	 * @param country the country we wish to get athletes for
	 * @param print boolean indicates if we want the athletes printed to standard out or not
	 * @return list of athlete objects from this country
	 * @throws Exception
	 */
	public static ArrayList<Athlete> getAthletesByCountry(HashMap<String,ArrayList<Athlete>> agegroups,String country,boolean print) throws Exception{
		if(agegroups==null)
			throw new Exception("Age groups cannot be empty");
		HashMap<String,ArrayList<Athlete>> countries = registrantsByCountry(agegroups);
		ArrayList<Athlete> list = countries.get(country);
		if(list!=null) {
			if(print) {
				for(Athlete athlete : list) {
					System.out.println(athlete);
				}
			}
			return list;
		} else {
			return new ArrayList<Athlete>();
		}
	}
}
