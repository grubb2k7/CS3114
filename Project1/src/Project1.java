import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.io.FileWriter;
import java.io.RandomAccessFile;
public class Project1 {

	static FileWriter	logStream;
	static int			cmdNum;
	static GISCmdParser	cmdParser;
	static GISParser	gisParser;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		cmdNum = 0;
		
		//Error checking the command line
		if(args.length < 2) {
			System.err.println("Too few arguments given: " +
					"GIS file and Command file is required");
			System.exit(1);
		}
		else if(args.length > 2) {
			System.err.println("Too many arguments given: " +
					"GIS file and Command file is required");
			System.exit(1);
		}
		
		//Setting up the 2 parsers
		File gisFile = new File(args[0]);
		File cmdFile = new File(args[1]);
		
		if(!gisFile.exists()) {
			System.err.println("File " + gisFile.getName() +
					" does not exist");
			System.exit(1);
		}
		if(!cmdFile.exists()) {
			System.err.println("File " + cmdFile.getName() +
					" does not exist");
			System.exit(1);
		}
		
		gisParser = new GISParser(gisFile);
		cmdParser = new GISCmdParser(cmdFile);
		
		//Setting up the log file
		File logFile = new File("Results.txt");
		try {
			logStream = new FileWriter(logFile);
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
		
		//Writing to the log file about the GIS file information
		writeToLog("GIS data file contains the following records:\n\n");
		try {
			RandomAccessFile gisStream = new RandomAccessFile(gisFile, "r");			
			Formatter f = new Formatter();
			long offset;
			String testLn = gisStream.readLine();
			
			//Going through GIS file and grabbing the offset and ID of each offset
			while((testLn = gisStream.readLine()) != null) {
				offset = gisStream.getFilePointer();
				f.format("\t%d\t%s\n", offset, gisParser.getFeatID(offset));
				writeToLog(f.toString());
				f.flush();
			}
			writeToLog("\n");
			gisStream.close();
			f.close();
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
		
		
		cmdParser.searchCmd();
		
		GISCmdParser.CommandType	cmd = cmdParser.getCurrCmd();
		long 						offset;
		
		while(cmd != GISCmdParser.CommandType.QUIT) {
			offset = cmdParser.getCurrOffset();
			writeToLog(interpretCmd(cmd, offset));
			
			cmdParser.searchCmd();
			cmd = cmdParser.getCurrCmd();
		}
		
		writeToLog(interpretCmd(cmd, 0));
		
		gisParser.close();
		cmdParser.close();
		try {
			logStream.close();
		} catch(IOException e){}
	}
	
	static String interpretCmd(GISCmdParser.CommandType cmd, long offset) {
		Formatter f = new Formatter();
		String firstLn;
		cmdNum++;
		f.format("%d:\t%s\n", cmdNum, cmdParser.toString());
		firstLn = f.toString();
		f.flush();
		switch(cmd) {
		case SHOW_NAME:
			f.format("\t\t%s\n", gisParser.getName(offset));
			break;
		case SHOW_LAT:
			f.format("\t\t%s\n", gisParser.getName(offset));
			break;
		case SHOW_LONG:
			f.format("\t\t%s\n", gisParser.getName(offset));
			break;
		case SHOW_ELEV:
			f.format("\t\t%s\n", gisParser.getName(offset));
			break;
		case QUIT:
			f.format("\t\tExiting\n");
			break;
		case NO_CMD:
			f.close();
			return null;
		}		
		firstLn += f.toString();
		f.close();
		return firstLn;
	}
	
	static void writeToLog(String line) {
		try {
			logStream.flush();
			logStream.write(line);
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
	}

}
