import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import java.util.Scanner;

public class GISCmdParser {

	private RandomAccessFile	cmdStream;
	private String			 	currCmd;
	private long 				currOffset;
	
	public enum CommandType {
		NO_CMD, SHOW_NAME, SHOW_LAT, SHOW_LONG, SHOW_ELEV, QUIT
	}
	
	public GISCmdParser(File cmdFile) {
		//Opening a RAF Stream for Command File
		try {
			cmdStream = new RandomAccessFile(cmdFile, "r");
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file " + cmdFile.getName());
			System.exit(1);
		}
	}
	
	public void close() {
		try{
			cmdStream.close();
		} catch(IOException e) {}
	}
	
	public void searchCmd() {		
		while(true) {
			String line = null;
			try {
				line = cmdStream.readLine();
			} catch(IOException e) {
				System.err.println("IO error occured");
				System.exit(1);
			}
			
			if(line.contains(";") || line == null) continue;
			
			Scanner		cmdScanner = new Scanner(line);			
			
			//Parsing the command and seeing if the offset is available
			currCmd = cmdScanner.next();
			if(cmdScanner.hasNextLong()) {
				currOffset = cmdScanner.nextLong();
			}
			else currOffset = 0;
			cmdScanner.close();
			
			break;
		}
	}
	
	public CommandType getCurrCmd() {
		if(currCmd.contentEquals("show_name")) {
			return CommandType.SHOW_NAME;
		}
		else if(currCmd.contentEquals("show_latitude")) {
			return CommandType.SHOW_LAT;
		}
		else if(currCmd.contentEquals("show_longitude")) {
			return CommandType.SHOW_LONG;
		}
		else if(currCmd.contentEquals("show_elevation")) {
			return CommandType.SHOW_ELEV;
		}
		else if(currCmd.contentEquals("quit")) {
			return CommandType.QUIT;
		}
		//Should never execute this line; just in case
		else {
			return CommandType.NO_CMD;
		}
	}
	
	public String toString() {
		return currCmd;
	}
	
	public long getCurrOffset() {
		return currOffset;
	}
}
