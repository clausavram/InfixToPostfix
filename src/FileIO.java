import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class provides a way for the program to fetch and write data to a file
 * 
 * @author Sorin
 * 
 */
public class FileIO {

	/**
	 * This method provides a way to get the lines from a file
	 * 
	 * @param inputFileName
	 *            the name of the input file
	 * @return 
	 * @throws FileNotFoundException
	 */
	public static ArrayList<String> getFileLines(String inputFileName)
			throws FileNotFoundException {
		ArrayList<String> lines = new ArrayList<String>();

		File inputFile = new File(inputFileName);
		if (!inputFile.exists()) {
			System.err.println("Input file '" + inputFileName
					+ "' could not be located.");
			System.exit(2);
		}

		Scanner fileReader = new Scanner(inputFile);

		while (fileReader.hasNext()) {
			String line = fileReader.nextLine();
			if (!line.isEmpty()) {
				lines.add(line);
			}
		}
		fileReader.close();
		return lines;
	}

	public static void writeToFile(String outputFileName,
			ArrayList<String> content) throws IOException {
		File outputFile = new File(outputFileName);
		if (!outputFile.exists()) {
			outputFile.createNewFile();
		}
		PrintWriter fileWriter = new PrintWriter(outputFile);
		for (String str : content) {
			fileWriter.println(str);
		}
		fileWriter.close();
	}
}
