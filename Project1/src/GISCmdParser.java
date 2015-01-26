import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileWriter;
import java.io.File;
import java.util.Scanner;
import java.util.Formatter;

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
		} catch(IOException e) {
			System.err.println("IO error occured");
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
		
		readAndExecute();
	}
	
	private void readAndExecute() {
		String line = null;
		
		try {
			while((line = cmdStream.readLine()) != null) {
				parseCmdAndExecute(line);
			}
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
	}
	
	private void parseCmdAndExecute(String line) {
		if(line.contains(";")) return;
		
		String cmd = null;
		long offset = 0;
		Scanner cmdScanner = new Scanner(line);
		Formatter f = new Formatter();
		
		//Parsing the command and seeing if the offset is available
		cmd = cmdScanner.next();
		if(cmdScanner.hasNextLong()) {
			offset = cmdScanner.nextLong();
		}
		
		//Writing to the log the original command and it's number
		cmdNum++;
		f.format("%d:\t%s", cmdNum, line);
		writeToLog(f.toString());
		f.flush();
		
		//Executing the requested command and formating it
		if(cmd.contentEquals("show_name")) {
			f.format("\t%s", gisParser.getName(offset));
		}
		else if(cmd.contentEquals("show_latitude")) {
			f.format("\t%s", gisParser.getLatitude(offset));
		}
		else if(cmd.contentEquals("show_longitude")) {
			f.format("\t%s", gisParser.getLongitude(offset));
		}
		else if(cmd.contentEquals("show_elevation")) {
			f.format("\t%s", gisParser.getElevation(offset));
		}
		else if(cmd.contentEquals("quit")) {
			f.format("\tExiting");
		}
		
		//Writing the response to the log
		writeToLog(f.toString());
	}
	
	private void writeToLog(String line) {
		
	}

}
