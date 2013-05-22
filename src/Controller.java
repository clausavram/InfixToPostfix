import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * This is the Controller of the program
 * 
 * We considered using a SINGLETON pattern for this class but since it does not
 * maintain any state we went with using STATIC METHODS
 */
public class Controller {
	private static Scanner consoleScanner = new Scanner(System.in);

	/**
	 * this is the entry point to the program
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err
					.println("Application usage: application input.txt output.txt");
			System.exit(1);
		}

		doLogic(args[0], args[1]);
	}

	/**
	 * This method uses all the other classes from the program to proccess the
	 * data from the input file
	 * 
	 * @param inputFile
	 *            the file where the operations are read from
	 * @param outputFile
	 *            the file where the postfix conversion is done
	 */
	private static void doLogic(String inputFile, String outputFile) {
		ArrayList<String> postFixes = new ArrayList<String>();

		ArrayList<String> inputLines = new ArrayList<String>();
		try {
			inputLines = FileIO.getFileLines(inputFile);
		} catch (FileNotFoundException ex) {
			System.out.println("input file: " + inputFile
					+ " could not be located");
			System.exit(2);
		}

		for (String line : inputLines) {
			// try catch to find a badly formatted input and step over it
			try {
				LinkedList<String> postfix = FixConverter
						.convertInToPostfix(line);
				// this will throw a badly formatted error
				List<String> unknowns = FixConverter.getUnknowns(postfix);

				System.out.print("Intfix:\t\t");
				System.out.println(line);
				System.out.print("Postfix:\t");
				String writablePostfix = FixConverter.convertToString(postfix);
				System.out.println(writablePostfix);

				postFixes.add(writablePostfix);

				Map<String, Double> values = readUnknowns(unknowns);
				Double computation = FixConverter.compute(postfix, values);
				System.out.print("Result:\t\t");
				System.out.println(computation);
				System.out.println();
			} catch (IllegalOperationInput ilOpIn) { // treat the badly
														// formatted input
														// exception
				System.out.println(ilOpIn + " at line: " + line);
			} catch (Exception e) { // treat any other exception may be thrown
				e.printStackTrace();
				System.exit(3);
			}
		}

		try {
			FileIO.writeToFile(outputFile, postFixes);
		} catch (IOException ex2) {
			System.out.println(ex2);
			System.exit(2);
		}
	}

	/**
	 * This method provides a way to read all the unknows from the operations
	 * from the input
	 * 
	 * @param unknowns
	 *            the unknowns of the program
	 * @return the values of the unknows read from the keyboard
	 */
	private static Map<String, Double> readUnknowns(List<String> unknowns) {
		Map<String, Double> values = new HashMap<>();
		Double readValue;
		for (String unknown : unknowns) {
			if (!values.containsKey(unknown)) {
				readValue = readDouble(unknown);
				values.put(unknown, readValue);
			}
		}
		return values;
	}

	/**
	 * Thsi method provides a way to read a double from the keyboard
	 * 
	 * @param name
	 *            the name of the unknown
	 * @return the value of the unknown
	 */
	private static Double readDouble(String name) {
		boolean success = false;
		Double result = null;
		while (!success) {
			try {
				System.out.print(name + " = ");
				String strIn = consoleScanner.next();
				result = Double.parseDouble(strIn);
				success = true;
			} catch (Exception e) {
				System.err.println("Please insert the value correctly");
			}
		}
		return result;
	}
}
