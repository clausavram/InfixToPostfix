import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
	private static Scanner consoleScanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Application usage: application input.txt output.txt");
			System.exit(1);
		}

		ArrayList<String> postFixes = new ArrayList<String>();
		for (String line : FileIO.getFileLines(args[0])) {

			LinkedList<String> postfix = FixConverter.convertInToPostfix(line);
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
		}
		FileIO.writeToFile(args[1], postFixes);
	}

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
