import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class GISParser {

	private RandomAccessFile rafStream;
	private ArrayList<Long> offsetList;
	
	private enum RequestType {
		FEAT_ID, FEAT_NAME, FEAT_CLASS, STATE_ALPHA_CODE, STATE_NUM_CODE,
		COUNT_NAME, COUNT_NUM_CODE, PRIM_LAT_DMS, PRIM_LONG_DMS, PRIM_LAT,
		PRIM_LONG, SRC_LAT_DMS, SRC_LONG_DMS, SRC_LAT, SRC_LONG, ELEV_M, ELEV_F,
		MAP_NAME, DATE_CREATED, DATE_EDITED
	}
	
	public GISParser(File GISFile) {
		try {
			rafStream = new RandomAccessFile(GISFile, "r");
			offsetList = new ArrayList<Long>();
			
			//Ignoring offset of 0 as that is the header line in
			//the GIS record file
			while(rafStream.readLine() != null) {
				offsetList.add(rafStream.getFilePointer());
			}
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file " + GISFile.getName());
			System.exit(1);
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
	}
	
	public String getName(long offset) {
		return grabInfo(offset, RequestType.FEAT_NAME);
	}
	
	public String getLatitude(long offset) {
		return grabInfo(offset, RequestType.PRIM_LAT);
	}
	
	public String getLongitude(long offset) {
		return grabInfo(offset, RequestType.PRIM_LONG);
	}
	
	public String getElevation(long offset) {
		return grabInfo(offset, RequestType.ELEV_F);
	}
	
	public ArrayList<Long> getOffsets() {
		ArrayList<Long> cloneList = new ArrayList<Long>();
		for(long x : offsetList) {
			cloneList.add(x);
		}
		return cloneList;
	}
	
	public void close() {
		try {
			rafStream.close();
		} catch(IOException e) {}
	}
	
	private String grabInfo(long offset, RequestType request) {
		String info = null;
		
		try {
			//Testing to see if the offset is an appropriate offset
			if(offset < 0) return "Offset not positive";
			if(!offsetExist(offset)) {
				if(offset > rafStream.length()) {
					return "Offset too large";
				}
				else return "Unaligned offset";
			}
			
			//Grabbing the requested line at the offset
			rafStream.seek(offset);
			info = rafStream.readLine();
			
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
		
		Scanner parser = new Scanner(info);
		parser.useDelimiter("|");
		 
		//Parsing through the line by iterating through the tokens
		//up until the point where the scanner has returned the requested
		//token
		for(int i = 0; i < request.ordinal(); i++) {
			if(parser.hasNext()) {
				info = parser.next();
			}
		}
		parser.close();
		
		return info;
	}
	
	//Come back to this as to what to do if given an offset of zero for the first line
	private boolean offsetExist(long offset) {
		
		for(long x : offsetList) {
			if(x == offset) return true;
			else if(x > offset) return false;
		}
		
		return false;
	}

}
