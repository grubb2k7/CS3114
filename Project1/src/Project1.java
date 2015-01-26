import java.io.File;
import java.util.Formatter;
public class Project1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		
		File logFile = new File("Results.txt");
		
	}

}
