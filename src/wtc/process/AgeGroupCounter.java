package wtc.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import wtc.obj.Athlete;

/**
 * Calculates values based on age groups
 * @author Robert
 *
 */
public class AgeGroupCounter {
	/**
	 * Calculate the number of registrants for each age group
	 * @param agegroups hash containing the age groups
	 * @param boolean to indicate if this should be printed to standard out or not
	 * @return a hash containing the age groups and the number of registrants
	 */
	public static HashMap<String,Integer> registrantsByAgeGroup(HashMap<String,ArrayList<Athlete>> agegroups,boolean print) throws Exception{
    	//get the count of each age group
		if(agegroups==null)
			throw new Exception("Age groups cannot be empty");
		HashMap<String,Integer> count = new HashMap<String,Integer>();
    	Iterator<String> iter = agegroups.keySet().iterator();
    	while(iter.hasNext()) {
    		String key = iter.next();
    		ArrayList<Athlete> athletes = agegroups.get(key);
    		count.put(key, athletes.size());
    		if(print)
    			System.out.println(key + "<" + athletes.size() + ">");
    	}
    	return count;
	}
	/**
	 * Get the number of registrants for a particular age group
	 * 
	 * @param agegroups the agegroup hash
	 * @param agegroup a string representing the age group
	 * @return the count of the number of registrants or 0 if that age group is not found
	 */
	public static int getRegistrantsByAgeGroup(HashMap<String,ArrayList<Athlete>> agegroups,String agegroup) throws Exception{
		int count = 0;
		if(agegroups==null)
			throw new Exception("Age groups cannot be empty");
		ArrayList<Athlete> ag = agegroups.get(agegroup);
		if(ag!=null)
			count = ag.size();
		return count;
	}
	/**
	 * Convenience method.. returns everyone in an age group
	 * @param agegroups the hash of age groups
	 * @param agegroup the string representing the age group
	 * @return list of athletes in the age group or null if empty
	 */
	public static ArrayList<Athlete> getAgeGroup(HashMap<String,ArrayList<Athlete>> agegroups,String agegroup) throws Exception{
		if(agegroups==null)
			throw new Exception("Age groups cannot be empty");
		return agegroups.get(agegroup);
	}
}
