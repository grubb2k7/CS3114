/**
 * @author grubb2k7 (Jonathan D. Grubb)
 * Project1
 * Last Modified: 01/30/2015
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Formatter;

public class GISParser {

	/**
	 * @param gisStream		Stream to the GISFile.
	 * @param RequestType	An enumerated type representing the structure
	 * 						of each identical line in the GISFile.
	 */
	private			RandomAccessFile gisStream;
	private			enum RequestType {
		FEAT_ID, FEAT_NAME, FEAT_CLASS, STATE_ALPHA_CODE, STATE_NUM_CODE,
		COUNT_NAME, COUNT_NUM_CODE, PRIM_LAT_DMS, PRIM_LONG_DMS, PRIM_LAT,
		PRIM_LONG, SRC_LAT_DMS, SRC_LONG_DMS, SRC_LAT, SRC_LONG, ELEV_M, ELEV_F,
		MAP_NAME, DATE_CREATED, DATE_EDITED
	}
	
	/**
	 * Constructor for the class that sets up the stream for the class.
	 * 
	 * @param gisFile	The File to the GISFile.
	 */
	public GISParser(File gisFile) {
		try {
			gisStream = new RandomAccessFile(gisFile, "r");
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file " + gisFile.getName());
			System.exit(1);
		}
	}
	
	/**
	 * Gets the Feature ID within the offset request if that offset exists.
	 * 
	 * @param offset	The offset of the file pointer that starts at the beginning of the line.
	 * @return			A string with either the requested information, an error pertaining, or
	 * 					null if there was an empty information in the line.
	 */
	public String getFeatID(long offset) {
		return grabInfo(offset, RequestType.FEAT_ID);
	}
	
	/**
	 * Gets the Name within the offset request if that offset exists.
	 * 
	 * @param offset	The offset of the file pointer that starts at the beginning of the line.
	 * @return			A string with either the requested information, an error pertaining, or
	 * 					null if there was an empty information in the line.
	 */
	public String getName(long offset) {
		return grabInfo(offset, RequestType.FEAT_NAME);
	}
	
	/**
	 * Gets the Latitude within the offset request if that offset exists.
	 * 
	 * @param offset	The offset of the file pointer that starts at the beginning of the line.
	 * @return			A string with either the requested information, an error pertaining, or
	 * 					null if there was an empty information in the line.
	 */
	public String getLatitude(long offset) {
		String line = grabInfo(offset, RequestType.PRIM_LAT_DMS);
		if(line.contains(" "))	return line;
		else 					return formatDMS(line);
	}
	
	/**
	 * Gets the Longitude within the offset request if that offset exists.
	 * 
	 * @param offset	The offset of the file pointer that starts at the beginning of the line.
	 * @return			A string with either the requested information, an error pertaining, or
	 * 					null if there was an empty information in the line.
	 */
	public String getLongitude(long offset) {
		String line = grabInfo(offset, RequestType.PRIM_LONG_DMS);
		if(line.contains(" "))	return line;
		else 					return formatDMS(line);
	}
	
	/**
	 * Gets the Elevation within the offset request if that offset exists.
	 * 
	 * @param offset	The offset of the file pointer that starts at the beginning of the line.
	 * @return			A string with either the requested information, an error pertaining to the
	 * 					offset that it was given, or "Elevation not given" if the field is empty
	 */
	public String getElevation(long offset) {
		String elevation = grabInfo(offset, RequestType.ELEV_F);
		if(elevation.length() > 0)	return elevation;
		else						return "Elevation not given";
	}
	
	/**
	 * Closes the gisStream variable.
	 */
	public void close() {
		try {
			gisStream.close();
		} catch(IOException e) {}
	}
	
	/**
	 * Helper function to the getX() public functions where it retrieves the information
	 * at the designated offset of the gisFile.
	 * @param offset	The offset of the file pointer that starts at the beginning of the line.
	 * @param request	The type of information that is being requested within the line.
	 * @return			A String containing either the requested information, an error message, or null
	 * 					meaning that there was no information in that line.	
	 */
	private String grabInfo(long offset, RequestType request) {
		String info = null;
		
		try {
			//Testing to see if the offset is an appropriate offset
			if(offset < 0)						return "Offset is not positive";
			if(offset == 0)						return "Unaligned offset";
			if(offset >= gisStream.length())	return "Offset too large";
			if(!offsetExist(offset))			return "Unaligned offset";
			
			//Grabbing the requested line at the offset
			gisStream.seek(offset);
			info = gisStream.readLine();
			
		} catch(IOException e) {
			System.err.println("IO error occured reading GIS file.");
			System.exit(1);
		}
		
		if(info == null) return "Offset too large";
		
		Scanner parser = new Scanner(info);
		parser.useDelimiter("\\|");
		//Parsing through the line by iterating through the tokens
		//up until the point where the scanner has returned the requested
		//token
		for(int i = -1; i < request.ordinal(); i++) {
			if(parser.hasNext()) {
				info = parser.next();
			}
		}
		parser.close();
		
		return info;
	}
	
	/**
	 * Helper function that determines whether the offset exists at the beginning of a new line
	 * in the GISFile.
	 * @param offset	The offset of the file pointer that starts at the beginning of the line.
	 * @return			Returns true if the offset begins at the line returns false otherwise.
	 */
	private boolean offsetExist(long offset) {
		char newln = '\0'; 
		
		try {
			gisStream.seek(offset-1);
			newln = (char)gisStream.readByte();
		} catch(IOException e) {
			System.err.println("IO error occured reading GIS file");
			System.exit(1);
		}
		
		if(newln == '\n') return true;
		else return false;
	}

	/**
	 * Helper function to format the longitude and latitude in degrees, minutes, seconds, and Direction
	 * format
	 * @param dms	The DMS latitude or longitude string.
	 * @return		returns the formatted string.
	 */
	private String formatDMS(String dms) {
		 	String direction, seconds, minutes, degrees;
		 	int dmsLength = dms.length();
		 	int s, m, d;
		 	
		 	direction = dms.substring(dmsLength-1);
		 	seconds = dms.substring(dmsLength-3, dmsLength-1);
		 	minutes = dms.substring(dmsLength-5,dmsLength-3);
		 	degrees = dms.substring(0, dmsLength-5);
		 	
		 	//Getting rid of the leading zeros by converting strings to an integer
		 	s = Integer.parseInt(seconds);
		 	m = Integer.parseInt(minutes);
		 	d = Integer.parseInt(degrees);
		 	
		 	Formatter f = new Formatter();
		 	
		 	switch(direction) {
		 	case "N": direction = "North";
		 		break;
		 	case "S": direction = "South";
		 		break;
		 	case "E": direction = "East";
		 		break;
		 	case "W": direction = "West";
		 		break;
		 	}
		 	
		 	f.format("%dd %dm %ds %s", d, m, s, direction);
		 	String returnStr = f.toString();
		 	f.close();
		 	
		 	return returnStr;
	}
}
