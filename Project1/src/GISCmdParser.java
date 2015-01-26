import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

public class GISCmdParser {

	private FileReader frStream;
	private GISParser gisParser;
	private String logName;
	
	public GISCmdParser(File cmdFile, File gisFile, String logFileName) {
		try {
			frStream = new FileReader(cmdFile);
		} catch(FileNotFoundException e) {
			System.err.println("Could not find file " + cmdFile.getName());
			System.exit(1);
		} catch(IOException e) {
			System.err.println("IO error occured");
			System.exit(1);
		}
		
		gisParser = new GISParser(gisFile);
		logName = logFileName;
	}
	
	private readAndExecute

}
