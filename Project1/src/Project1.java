// On my honor:
//
// - I have not discussed the Java language code in my program with
// anyone other than my instructor or the teaching assistants
// assigned to this course.
//
// - I have not used Java language code obtained from another student,
// or any other unauthorized source, either modified or unmodified.
//
// - If any Java language code or documentation used in my program
// was obtained from another source, such as a text book or course
// notes, that has been clearly noted with a proper citation in
// the comments of my program.
//
// - I have not designed this program in such a way as to defeat or
// interfere with the normal operation of the Curator System.
//
// Jonathan D. Grubb

/**
 * @author grubb2k7 (Jonathan D. Grubb)
 * Project1
 * Last Modified: 01/30/2015
 * 
 * This program takes in two different files, one being a GIS file and the other being the Command file.
 * Depending on the commands that are parsed through the commands file, it will retrieve specific information
 * within the GIS file. The parsing is accomplished by using a specific offset from the commands file and
 * ensuring that that offset starts at the beginning of the line.  There is also other types of error checking
 * to make sure the offset is appropriate for the file. Enumerator types are used to define either the type of
 * command that is being executed or the type of information needed to be retrieved from the file.
 * 
 * Everything is reported in the log file "Results.txt". It is written in the format of:
 * 
 * ------------------------------------------------------------------
 * |	(Offsets in GIS File)	(Feature ID at that offset)
 * |	...
 * |	...
 * |
 * |	(Command number):	(Listed Command)
 * |						(Result from Command)
 * |	...
 * |	...
 * ------------------------------------------------------------------
 */

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.io.FileWriter;
import java.io.RandomAccessFile;

public class Project1 {

	/**
	 * @param logStream		The stream to the log file "Results.txt".
	 * @param cmdNum		Global command number keeps track of which command it is executing.
	 * @param cmdParser		The CommandFile Parser.
	 * @param gisParser		The GISFile Parser.
	 */
	static FileWriter	logStream;
	static int			cmdNum;
	static GISCmdParser	cmdParser;
	static GISParser	gisParser;

	/**
	 * main goes through the GISFile extracting all the line offset information and writing
	 * that information along with the lines Feature ID of that file into a log file "Results.txt".
	 * Afterwards, main then starts parsing through the commands in the CommandFile and then parse
	 * the needed information in the GISFile using two different special parsing classes for each file.
	 * The result of the commands is written in the log file once retrieved.  Essentially main is a wrapper
	 * function between the two parsers.  It gets and information from one parser and feeds it to the other.
	 * 
	 * @param args	main needs two command line arguments, GISFile and CommandFile
	 *            	in order to execute. GISFile contains GIS information and CommandFile
	 *            	contains information to execute commands to parse certain parts of the
	 *            	GISFile
	 */
	public static void main(String[] args) {
		cmdNum = 0;

		// Error checking the command line
		if (args.length < 2) {
			System.err.println("Too few arguments given: "
					+ "GIS file and Command file is required");
			System.exit(1);
		} else if (args.length > 2) {
			System.err.println("Too many arguments given: "
					+ "GIS file and Command file is required");
			System.exit(1);
		}

		// Setting up the 2 parsers
		File gisFile = new File(args[0]);
		File cmdFile = new File(args[1]);

		if (!gisFile.exists()) {
			System.err.println("File " + gisFile.getName() + " does not exist");
			System.exit(1);
		}
		if (!cmdFile.exists()) {
			System.err.println("File " + cmdFile.getName() + " does not exist");
			System.exit(1);
		}

		gisParser = new GISParser(gisFile);
		cmdParser = new GISCmdParser(cmdFile);

		// Setting up the log file
		File logFile = new File("Results.txt");
		try {
			logStream = new FileWriter(logFile);
		} catch (IOException e) {
			System.err.println("IO error occured in opening Results.txt");
			System.exit(1);
		}

		// Obtaining GISFile offset information and writing it to the log file
		writeToLog("GIS data file contains the following records:\n\n");
		try {
			RandomAccessFile gisStream = new RandomAccessFile(gisFile, "r");
			Formatter f = new Formatter();
			long offset;

			// Going through GIS file and grabbing the offset and ID of each
			// offset
			while ((gisStream.readLine()) != null) {
				if (gisStream.getFilePointer() >= gisStream.length())
					break;
				offset = gisStream.getFilePointer();
				f.format("\t%12d\t%s\n", offset, gisParser.getFeatID(offset));
			}
			writeToLog(f.toString());
			writeToLog("\n");
			gisStream.close();
			f.close();
		} catch (IOException e) {
			System.err.println("IO error occured reading from GIS file");
			System.exit(1);
		}

		//Ready to start parsing through command file and execute commands
		cmdParser.searchCmd();

		GISCmdParser.CommandType cmd = cmdParser.getCurrCmd();
		long offset;

		while (cmd != GISCmdParser.CommandType.QUIT) {
			offset = cmdParser.getCurrOffset();
			writeToLog(interpretCmd(cmd, offset));

			cmdParser.searchCmd();
			cmd = cmdParser.getCurrCmd();
		}

		writeToLog(interpretCmd(cmd, 0));

		//Finished and closing all open files
		gisParser.close();
		cmdParser.close();
		try {
			logStream.close();
		} catch (IOException e) {}
	}

	/**
	 * interpretCommand is a helper function that takes in the command that was parsed from the CommandFile and executes
	 * that command. The commands needed to be executed are all within GISParser class. The executed commands return the information
	 * that was requested in String format.  After receiving the requested command, interpretCmd formats a string that
	 * has the command that was requested and the returned information.
	 * @param cmd		Command that is being requested.
	 * @param offset	The offset of the beginning of the needed information line in the GISFile.
	 * @return			interpretCmd formats a string that has the command that was requested and the returned information.
	 */
	static String interpretCmd(GISCmdParser.CommandType cmd, long offset) {
		Formatter	f	 = new Formatter();
		String		line = null;
		cmdNum++;
		
		f.format("%4d:\t%s\n", cmdNum, cmdParser.toString());
		
		switch (cmd) {
		case SHOW_NAME:
			f.format("\t\t%s\n", gisParser.getName(offset));
			break;
		case SHOW_LAT:
			f.format("\t\t%s\n", gisParser.getLatitude(offset));
			break;
		case SHOW_LONG:
			f.format("\t\t%s\n", gisParser.getLongitude(offset));
			break;
		case SHOW_ELEV:
			f.format("\t\t%s\n", gisParser.getElevation(offset));
			break;
		case QUIT:
			f.format("\t\tExiting\n");
			break;
		//Not expecting to get this command
		case NO_CMD:
			f.close();
			return null;
		}
		line = f.toString();
		f.close();
		return line;
	}

	/**
	 * writeToLog is a helper function to write to the log file.
	 * @param line	The line that needs to get written to the log file.
	 */
	static void writeToLog(String line) {
		try {
			logStream.write(line);
			logStream.flush();
		} catch (IOException e) {
			System.err.println("IO error occured with log file.");
			System.exit(1);
		}
	}

}
