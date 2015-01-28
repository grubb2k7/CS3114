import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Formatter;

public class GISParser {

	private RandomAccessFile gisStream;
	
	private enum RequestType {
		FEAT_ID, FEAT_NAME, FEAT_CLASS, STATE_ALPHA_CODE, STATE_NUM_CODE,
		COUNT_NAME, COUNT_NUM_CODE, PRIM_LAT_DMS, PRIM_LONG_DMS, PRIM_LAT,
		PRIM_LONG, SRC_LAT_DMS, SRC_LONG_DMS, SRC_LAT, SRC_LONG, ELEV_M, ELEV_F,
		MAP_NAME, DATE_CREATED, DATE_EDITED
	}
	
	public GISParser(File gisFile) {
		try {
			gisStream = new RandomAccessFile(gisFile, "r");
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file " + gisFile.getName());
			System.exit(1);
		}
	}
	
	public String getFeatID(long offset) {
		return grabInfo(offset, RequestType.FEAT_ID);
	}
	
	public String getName(long offset) {
		return grabInfo(offset, RequestType.FEAT_NAME);
	}
	
	public String getLatitude(long offset) {
		return formatDMS(grabInfo(offset, RequestType.PRIM_LAT_DMS));
	}
	
	public String getLongitude(long offset) {
		return formatDMS(grabInfo(offset, RequestType.PRIM_LONG_DMS));
	}
	
	public String getElevation(long offset) {
		return grabInfo(offset, RequestType.ELEV_F);
	}
	
	public void close() {
		try {
			gisStream.close();
		} catch(IOException e) {}
	}
	
	private String grabInfo(long offset, RequestType request) {
		String info = null;
		
		try {
			//Testing to see if the offset is an appropriate offset
			if(offset < 0)						return "Offset not positive";
			if(offset == 0)						return "Unaligned offset";
			if(offset >= gisStream.length())	return "Offset too large";
			if(!offsetExist(offset))			return "Unaligned offset";
			
			//Grabbing the requested line at the offset
			gisStream.seek(offset);
			info = gisStream.readLine();
			
		} catch(IOException e) {
			System.err.println("IO error occured 4");
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
	
	private boolean offsetExist(long offset) {
		char newln = '\0'; 
		
		try {
			gisStream.seek(offset-1);
			newln = (char)gisStream.readByte();
		} catch(IOException e) {
			System.err.println("IO error occured 5");
			System.exit(1);
		}
		
		if(newln == '\n') return true;
		else return false;
	}

	private String formatDMS(String dms) {
		 	String direction, seconds, minutes, degrees;
		 	int dmsLength = dms.length();
		 	
		 	direction = dms.substring(dmsLength-1);
		 	seconds = dms.substring(dmsLength-3, dmsLength-1);
		 	minutes = dms.substring(dmsLength-5,dmsLength-3);
		 	degrees = dms.substring(0, dmsLength-5);
		 	
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
		 	
		 	f.format("%sd %sm %ss %s", degrees, minutes, seconds, direction);
		 	String returnStr = f.toString();
		 	f.close();
		 	
		 	return returnStr;
	}
}
