package wtc.parse;

import wtc.obj.Athlete;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
/**
 * Parses a WTC Ironman Bib list and assembles a list 
 * of athletes broken down by age group
 * ie; <AgeGroup => ArrayList<Athlete>>
 * @author Robert Johnson
 *
 */
public class BibListParser {
	/**
	 * Parse the PDF file referenced by the filelocation
	 * @param fileLocation
	 * @return a Hashmap containing the list of Athletes broken up by age group
	 * @throws IOException if the file is not found or does not exist
	 */
	public static HashMap<String,ArrayList<Athlete>> parseBibList(String filename) throws IOException {
		PDDocument document = null;
		HashMap<String,ArrayList<Athlete>> agegroups = new HashMap<String,ArrayList<Athlete>>();
		try {
			File file = new File(filename);
	    	document = PDDocument.load(file);
	    	PDFTextStripper pdfStripper = new PDFTextStripper();
	    	String text = pdfStripper.getText(document);
	    	String[] lines = text.split("\r\n");
	    	for(String line : lines) {
	    		line.replaceAll("\r", "");
	    		if(Athlete.isAthlete(line)) {
	    			Athlete at = new Athlete(line);
	    			ArrayList<Athlete> agegroup = agegroups.get(at.getAgegroup());
	    			if(agegroup!=null) {}
	    			else {
	    				//age group hasn't been created yet.. add it
	    				agegroup = new ArrayList<Athlete>();
	    				agegroups.put(at.getAgegroup(), agegroup);
	    			}
	    			agegroup.add(at);
	    		}
	    	}
		}catch(IOException e) {
			//remove this in production and add a logger
			System.out.println("Error occurred");
			e.printStackTrace();
			throw e;
		} finally {
			if(document!=null)
				document.close();
		}
		return agegroups;
	}
}
