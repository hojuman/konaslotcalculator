package wtc.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import wtc.obj.AgeGroup;
import wtc.obj.Athlete;
import wtc.util.PrintUtil;

/**
 * This calculator aims to take an agegroup hash and calculate the number of kona slots
 * available to each age group based upon the number of total kona slots in the race
 * @author Robert
 *
 */
public class KonaSlotCalculator {
	private static final String OLDGUY = "[MF]80-99";
	
	private static final String[] OLDGUYS = {"80-84","85-89","85-89","95-99"};
	/**
	 * Calculate the total kona slots for each age group
	 * @param agegroups age group hash
	 * @param totalslots total number of kona slots in the race
	 * @param print determins if we should print the results to standard out or not
	 * @return a hash that contains the number of kona slots per age group
	 * @throws Exception
	 */
	public static HashMap<String,Integer> calculateKonaSlots(HashMap<String,ArrayList<Athlete>> agegroups,int totalslots,boolean print) throws Exception {
		if(agegroups==null)
			throw new Exception("Age groups cannot be empty");
		agegroups = filterAgeGroups(agegroups);
		HashMap<String,Integer> count = null;
		HashMap<String,Integer> registrantsByAgeGroup = AgeGroupCounter.registrantsByAgeGroup(agegroups, false);
		
		HashMap<String,Integer> freebee = generateEmptySlots();
		calculateFreeBeeSlots(agegroups,freebee);
		HashMap<String,Double> remaining = KonaSlotCalculator.calculateRemaining(agegroups,registrantsByAgeGroup, freebee, totalslots);
		HashMap<String,Double> floorrem = KonaSlotCalculator.floorRemaining(remaining);
		HashMap<String,AgeGroup> slotsdecimal = KonaSlotCalculator.calculateSlotsDecimal(remaining, floorrem);
		
		count = KonaSlotCalculator.calculateSlots(agegroups, freebee, floorrem, slotsdecimal);
		new PrintUtil<Integer>().printHash(count);
		return count;
	}
	/**
	 * Filters out the age groups so we don't end up with anything that isn't an age group.. ie; challenged athletes
	 * @param agegroups 
	 * @return filtered age groups hash
	 */
	private static HashMap<String,ArrayList<Athlete>> filterAgeGroups(HashMap<String,ArrayList<Athlete>> agegroups) {
		HashMap<String,ArrayList<Athlete>> ret = new HashMap<String,ArrayList<Athlete>>();
		String regex = "[MF]\\d\\d-\\d\\d";
		Iterator<String> iter = agegroups.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			if(Pattern.matches(regex, key)){
				//looks like an age group
				ret.put(key, agegroups.get(key));
			}
		}
		
		return ret;
	}
	/**
	 * Calculate the number of freebee slots
	 * Every age group gets a slot so if there are any registrants in an age group, give them a slot
	 * @param agegroups
	 * @param freebee
	 * @return
	 */
	private static void calculateFreeBeeSlots(HashMap<String,ArrayList<Athlete>> agegroups,HashMap<String,Integer> freebee) throws Exception{
		Iterator<String> iter = freebee.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			if(Pattern.matches(OLDGUY, key)) {
				//this is the old guy slot.. so we need to check 80 up
				int count = 0;
				for(String oldguy : OLDGUYS) {
					count+= AgeGroupCounter.getRegistrantsByAgeGroup(agegroups, key.charAt(0)+""+oldguy);
				}
				if(count>0) 
					freebee.put(key, 1);
			} else {
				//just check if this age group has any registrans
				int count = AgeGroupCounter.getRegistrantsByAgeGroup(agegroups, key);
				if(count>0) {
					freebee.put(key, 1);//this will overwrite the empty
				}
			}
		}
	}
	/**
	 * Calculates the remaining slots ratio
	 * Remaining slots ration is the proportion of the remaining slots (slots left over after freebees were distributed)
	 * that are available to a given age group
	 * @param registrantsByAgeGroup the agegroups in this race with total number of registrants
	 * @param freebee freebee slots hash that denotes free spots already allocated
	 * @param totalslots the total slots in this race
	 * @return a hash of the agegroups along with their calcualated ratio
	 */
	private static HashMap<String,Double> calculateRemaining(HashMap<String,ArrayList<Athlete>> agegroups,HashMap<String,Integer> registrantsByAgeGroup,HashMap<String,Integer> freebee,int totalslots) {
		HashMap<String,Double> ration = new HashMap<String,Double>();
		
		Iterator<String> iter = agegroups.keySet().iterator();
		
		int totalfree = KonaSlotCalculator.calculateSlotsUsed(freebee);
		int totalregistered = KonaSlotCalculator.calculateSlotsUsed(registrantsByAgeGroup);
		while(iter.hasNext()) {
			String key = iter.next();
			int agegroupcount = registrantsByAgeGroup.get(key);
			double ratio = (((double)agegroupcount/totalregistered)*(totalslots-totalfree));
			ration.put(key, ratio);
		}
		
		return ration;
	}
	/**
	 * Calculates the floor of the remaining for the remaining hash
	 * @param remaining
	 * @return Hash of the remaining for each age group
	 */
	private static HashMap<String,Double> floorRemaining(HashMap<String,Double> remaining) {
		HashMap<String,Double> rem = new HashMap<String,Double>();
		Iterator<String> iter = remaining.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			double remain = remaining.get(key);
			rem.put(key, Math.floor(remain));
		}
		return rem;
	}
	/**
	 * Calulates teh slots decimal 
	 * For each age group this is the remaining - floor remaining
	 * @param remaining
	 * @param floorremaining
	 * @return hash of the slots decimal for each age group in an AgeGroup object.. we need to sort these values
	 */
	private static HashMap<String,AgeGroup> calculateSlotsDecimal(HashMap<String,Double> remaining, HashMap<String,Double> floorremaining) {
		HashMap<String,AgeGroup> rem = new HashMap<String,AgeGroup>();
		Iterator<String> iter = remaining.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			double remain = remaining.get(key);
			double floor = floorremaining.get(key);
			AgeGroup ag = new AgeGroup(key,remain-floor);
			rem.put(key, ag);
		}
		return rem;
	}
	/**
	 * Final Calculation for Kona slots
	 * For each age group:
	 * FreeSlots + FloorRemaining + {if slots decimal <= decimalrank then 1 else 0}
	 * @param agegroups
	 * @param freebee
	 * @param slotsdecimal
	 * @return Hash of each age group with it's number of Kona Slots
	 */
	private static HashMap<String,Integer> calculateSlots(HashMap<String,ArrayList<Athlete>> agegroups,HashMap<String,Integer> freebee,HashMap<String,Double> floorremaining,HashMap<String,AgeGroup> slotsdecimal) {
		HashMap<String,Integer> count = new HashMap<String,Integer>();
		//sort the slots decimal
		SortedSet<AgeGroup> decimalrank = new TreeSet<AgeGroup>(slotsdecimal.values());
		new PrintUtil<AgeGroup>().printSet(decimalrank);
		Iterator<String> iter = agegroups.keySet().iterator();
		int slotsSum = KonaSlotCalculator.calculateSum(slotsdecimal);
		while(iter.hasNext()) {
			String key = iter.next();
			if(freebee.get(key)!=null) {
				AgeGroup ag = slotsdecimal.get(key);
				int freeslots = freebee.get(key);
				double floorrem = floorremaining.get(key);
				int rank = AgeGroup.indexOf(decimalrank, ag);
				int rankslot = rank<=slotsSum?1:0;
				count.put(key, freeslots + (int)floorrem+rankslot);
			}
		}
		
		return count;
	}
	/**
	 * Create a hash of all the age groups that are possible
	 * @return
	 */
	private static HashMap<String,Integer> generateEmptySlots() {
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		String[] genders = {"M","F"};
		for(String gender : genders) {
			map.put(gender+"18-24", 0);
			map.put(gender+"25-29", 0);
			map.put(gender+"30-34", 0);
			map.put(gender+"35-39", 0);
			map.put(gender+"40-44", 0);
			map.put(gender+"45-49", 0);
			map.put(gender+"50-54", 0);
			map.put(gender+"55-59", 0);
			map.put(gender+"60-64", 0);
			map.put(gender+"65-69", 0);
			map.put(gender+"70-74", 0);
			map.put(gender+"75-79", 0);
			map.put(gender+"80-99", 0);
		}
		return map;
	}
	/**
	 * Calculate total slots used by a hash
	 * @param hash
	 * @return
	 */
	private static int calculateSlotsUsed(HashMap<String,Integer> hash) {
		int ret = 0;
		
		Iterator<String> iter = hash.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			ret += hash.get(key).intValue();
		}
		
		return ret;
	}
	/**
	 * Calculate the floor of the sum of the decimal values in this hash
	 * @param hash
	 * @return
	 */
	private static int calculateSum(HashMap<String,AgeGroup> hash) {
		Iterator<String> iter = hash.keySet().iterator();
		double sum = 0.0;
		while(iter.hasNext()) {
			String key = iter.next();
			sum += hash.get(key).val;
		}
		return (int)Math.floor(sum);
	}
}
