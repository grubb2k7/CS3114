import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;
import java.util.Formatter;
import java.util.ArrayList;

public class GISCmdParser {

	private RandomAccessFile cmdStream;
	private FileWriter logStream;
	private GISParser gisParser;
	private int cmdNum;
	
	public GISCmdParser(File cmdFile, File gisFile, File logFile) {
		try {
			cmdStream = new RandomAccessFile(cmdFile, "r");
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file " + cmdFile.getName());
			System.exit(1);
		}
		
		try {
			logStream = new FileWriter(logFile);
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file " + logFile.getName());
			System.exit(1);
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
		
		gisParser = new GISParser(gisFile);
		cmdNum = 0;
		
		printGISOffsets();
		
		while(parseCmdAndExecute()){}
		
		try{
			cmdStream.close();
		} catch(IOException e) {}
		
		gisParser.close();
	}
	
	private void printGISOffsets() {
		ArrayList<Long> offsetList = gisParser.getOffsets();
		
		writeToLog("GIS data file contains the following records:\n\n");
		
		int			half = offsetList.size() / 2;
		int			arrayOffset = 0;
		Formatter	f = new Formatter();
		
		if(offsetList.size() % 2 != 0) {
			arrayOffset = 1;
		}
		
		for(int i = 0; i < half; i++) {
			f.format("\t%d\t%d\n",
					offsetList.get(i), offsetList.get(i + half + arrayOffset));
			writeToLog(f.toString());
			f.flush();
		}
		
		if(arrayOffset == 1) {
			f.format("\t%d\n", offsetList.get(half));
			writeToLog(f.toString());
			f.flush();
		}
		
		f.close();
		writeToLog("\n");	
	}
	
	private boolean parseCmdAndExecute() {
		String line = null;
		try {
			line = cmdStream.readLine();
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
		
		if(line.contains(";") || line == null) return true;
		
		String		cmd = null;
		long		offset = 0;
		boolean		proceed = true;
		Scanner		cmdScanner = new Scanner(line);
		Formatter 	f = new Formatter();
		
		
		//Parsing the command and seeing if the offset is available
		cmd = cmdScanner.next();
		if(cmdScanner.hasNextLong()) {
			offset = cmdScanner.nextLong();
		}
		cmdScanner.close();
		
		//Writing to the log the original command and it's number
		cmdNum++;
		f.format("%d:\t%s\n", cmdNum, line);
		writeToLog(f.toString());
		f.flush();
		
		//Executing the requested command and formating it
		if(cmd.contentEquals("show_name")) {
			f.format("\t%s\n", gisParser.getName(offset));
		}
		else if(cmd.contentEquals("show_latitude")) {
			f.format("\t%s\n", gisParser.getLatitude(offset));
		}
		else if(cmd.contentEquals("show_longitude")) {
			f.format("\t%s\n", gisParser.getLongitude(offset));
		}
		else if(cmd.contentEquals("show_elevation")) {
			f.format("\t%s\n", gisParser.getElevation(offset));
		}
		else if(cmd.contentEquals("quit")) {
			f.format("\tExiting\n");
			proceed = false;
		}
		
		//Writing the response to the log
		writeToLog(f.toString());
		f.close();
		return proceed;
	}
	
	private void writeToLog(String line) {
		try {
			logStream.flush();
			logStream.write(line);
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
	}

}
