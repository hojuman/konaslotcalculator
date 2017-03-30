package wtc.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * 
 * @author Robert
 *
 */
public class PrintUtil<E> {
	/**
	 * Print the key and value for this hash
	 * @param hash
	 */
	public void printHash(HashMap<String,E> hash) {
		Iterator<String> iter = hash.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			System.out.println(key + " " + hash.get(key));
		}
	}
	/**
	 * Print the key and value for this hash
	 * @param hash
	 */
	public void printSet(SortedSet<E> hash) {
		Iterator<E> iter = hash.iterator();
		while(iter.hasNext()) {
			E key = iter.next();
			System.out.println(key);
		}
		System.out.println("");
	}
}
