import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileWriter;
import java.io.File;

public class GISCmdParser {

	private RandomAccessFile cmdStream;
	private FileWriter logStream;
	private GISParser gisParser;
	
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
		
		readAndExecut();
	}
	
	private void readAndExecute() {
		String line = null;
		
		try {
			while((line = cmdStream.readLine()) != null) {
				if(line.contains(";")) {
					continue;
				}
				
			}
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
	}
	
	private parseCmd(String cmd) {
		
	}

}
