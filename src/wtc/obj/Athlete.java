package wtc.obj;
/**
 * Represents a WTC Athlete
 * @author Robert
 *
 */
public class Athlete {
	
	private String bib;//athlete bib
	
	private String firstName;//first name
	
	private String lastName;//last name
	
	private String agegroup;//age group of this athlete as a string, ie; M25-29, F25-29
	
	private String country;//country represented
	/**
	 * Create an empty athlete
	 */
	public Athlete() {
		
	}
	/**
	 * Create an athlete object from a string representing a line
	 * An athlete line typically resembles the following format
	 * Bib# LastName FirstName AgeGroup Country
	 * @param line a line from the pdf
	 */
	public Athlete(String line) {
		String[] cells = line.split(" ");
		this.bib = cells[0];
		this.firstName = cells[2];
		this.lastName = cells[1];
		this.agegroup = cells[3];
		this.country = cells[4];
	}
	public String getBib() {
		return bib;
	}
	public void setBib(String bib) {
		this.bib = bib;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAgegroup() {
		return agegroup;
	}
	public void setAgegroup(String agegroup) {
		this.agegroup = agegroup;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * Determines if a line represents an athlete
	 * Bib# LastName FirstName AgeGroup Country
	 * @param line a line from the pdf
	 * @return true if this is an athlete line, false otherwise
	 */
	public static boolean isAthlete(String line) {
		String[] cells = line.split(" ");
		if(cells.length==5) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Override toString.. 
	 * Bib FirstName Lastname AgeGroup Country
	 */
	public String toString() {
		return this.bib + " " + this.firstName + " " + this.lastName + " " + this.agegroup + " " + this.country;
	}
}
