package wtc.obj;

import java.util.Iterator;
import java.util.SortedSet;

/**
 * Represents an age group.. mostly used for sorting
 * @author Robert
 *
 */
public class AgeGroup implements Comparable<AgeGroup>{
	public String agegroup;//represents teh age group
	
	public double val;//some rank to sort by
	/**
	 * Construct an AgeGroup
	 * @param ag
	 * @param r
	 */
	public AgeGroup(String ag, double val) {
		this.agegroup = ag;
		this.val = val;
	}
	/**
	 * Compare these two objects
	 * @param ag
	 * @return AgeGroups with higher vals should rank higher
	 */
	public int compareTo(AgeGroup ag) {
		if(ag instanceof AgeGroup) {
			AgeGroup ag2 = (AgeGroup)ag;
			if(val>ag2.val) 
				return -1;
			else 
				return 1;
		} else {
			return -1;
		}
	}
	public boolean isEqual(Object ag) {
		if(ag instanceof AgeGroup) {
			AgeGroup ag2 = (AgeGroup)ag;
			return this.agegroup.equals(ag2.agegroup);
		} else {
			return false;
		}
	}
	/**
	 * 
	 */
	public String toString() {
		return this.agegroup;
	}
	/**
	 * Finds the index in a sorted set of an AgeGroup
	 * USed to get the ranking
	 * @param sorted
	 * @param toFind
	 * @return
	 */
	public static int indexOf(SortedSet<AgeGroup> sorted, AgeGroup toFind) {
		int rank = -1;
		int count = 0;
		Iterator<AgeGroup> iter = sorted.iterator();
		while(iter.hasNext()) {
			AgeGroup ag = iter.next();
			if(ag==toFind) {
				rank = count;
				break;
			} else {
				count++;
			}
		}
		return rank;
	}
}
