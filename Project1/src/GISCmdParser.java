/**
 * @author grubb2k7 (Jonathan D. Grubb)
 * Project1
 * Last Modified: 01/30/2015
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import java.util.Scanner;

public class GISCmdParser {

	/**
	 * @param cmdStream		The stream to the GIS Command file.
	 * @param currCmd		The current command that was last parsed.
	 * @param currOffset	The current offset that was last parsed.
	 * @param offsetParam	Flag that determines if there is an offsetParam in the command.
	 * @param CommandType	Enumerated type that represents the type of commands
	 * 						found in the GIS Command file.
	 */
	private RandomAccessFile	cmdStream;
	private String			 	currCmd;
	private long 				currOffset;
	private boolean				offsetParam;
	public enum					CommandType {
		NO_CMD, SHOW_NAME, SHOW_LAT, SHOW_LONG, SHOW_ELEV, QUIT
	}
	
	/**
	 * Constructor that sets up the cmdStream.
	 * @param cmdFile	The file that contains all the commands for requesting information
	 * 					from a GIS file.
	 */
	public GISCmdParser(File cmdFile) {
		//Opening a RAF Stream for Command File
		try {
			cmdStream = new RandomAccessFile(cmdFile, "r");
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file " + cmdFile.getName());
			System.exit(1);
		}
	}
	
	/**
	 * Function to close the cmdStream.
	 */
	public void close() {
		try{
			cmdStream.close();
		} catch(IOException e) {}
	}
	
	/**
	 * Goes to the next available command in the stream and parses the command.
	 * If there is an offset available it will parse that two. All values are stored in
	 * their respected global variables.
	 */
	public void searchCmd() {		
		while(true) {
			String line = null;
			try {
				line = cmdStream.readLine();
			} catch(IOException e) {
				System.err.println("IO error occured reading Command file.");
				System.exit(1);
			}
			
			if(line.contains(";") || line == null) continue;
			
			Scanner		cmdScanner = new Scanner(line);			
			
			//Parsing the command and seeing if the offset is available
			currCmd = cmdScanner.next();
			if(cmdScanner.hasNextLong()) {
				currOffset = cmdScanner.nextLong();
				offsetParam = true;
			}
			else offsetParam = false;
			cmdScanner.close();
			
			break;
		}
	}
	
	/**
	 * After searchCmd has been executed, the getCurrCmd can return the command that was parsed.
	 * @return	Returns the command that was parsed in the form of a CommandType.
	 */
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
		//Should never execute this line for this project; just in case
		else {
			return CommandType.NO_CMD;
		}
	}
	
	/**
	 * Overwritten toString function that returns the last parsed message.
	 * @return	Returns a string containing the original message from the last
	 * 			parsed command.
	 */
	public String toString() {
		if(offsetParam)	return currCmd + "\t" + currOffset;
		else			return currCmd;

	}
	
	/**
	 * Gets the offset request within the line.
	 * @return	The offset of the GIS File for the current command. Be aware that
	 * 			the only command that does not require an offset is QUIT. If this
	 * 			is called with a command that does not have an offset parameter in
	 * 			the Command File, then the user will get the value of the previous
	 * 			command that had an offset value.
	 */
	public long getCurrOffset() {
		return currOffset;
	}
}
