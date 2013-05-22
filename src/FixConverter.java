import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class provides a way to convert infix to postfix and also check if the
 * input operations are correctly formatted
 * 
 */
public class FixConverter {

	/**
	 * This method provides a way to convert a String representing an infix
	 * operation to postfix
	 * 
	 * @param input
	 *            the input string representing the operation
	 * @return the list representing all the tokens in the operation
	 * @throws IllegalOperationInput
	 */
	public static LinkedList<String> convertInToPostfix(String input)
			throws IllegalOperationInput {

		String[] infix = convertBrackets(input).replaceAll(" ", "")
				.replaceAll("[+\\(\\)\\-\\*\\^\\/]", " $0 ")
				.replaceAll(" +", " ").trim().split(" ");

		if (!isLegalOperation(infix)) {
			throw new IllegalOperationInput();
		}

		Stack<String> stack = new Stack<String>();
		LinkedList<String> postfix = new LinkedList<>(); // Queue: offer / peek
															// / poll

		// Unary adaptation:
		for (int i = 0; i < infix.length; i++) {
			if (infix[i].matches("[+-]")
					&& (i == 0 || isOperator(infix[i - 1]) || infix[i - 1]
							.matches("\\("))) {
				infix[i] = (infix[i].equals("+")) ? ArithmeticOperation.UNARY_PLUS
						: ArithmeticOperation.UNARY_MINUS;
			}
		}

		// Reverse Polish Notation conversion:
		for (String element : infix) {
			if (element.equals("(")) {
				stack.push(element);
			} else if (isOperator(element)) {
				if (stack.empty()) {
					stack.push(element);
				} else {
					while (!stack.isEmpty()) {
						String item = stack.pop();
						if (item.equals("(")) {
							stack.push(item);
						} else if (isOperator(item)) {
							if (ArithmeticOperation.precedence(item) < ArithmeticOperation
									.precedence(element)) {
								stack.push(item);
							} else {
								postfix.offer(item);
							}
						}
						if(ArithmeticOperation.precedence(item) < ArithmeticOperation
									.precedence(element) || item.equals("(")){
							break;
						}
					}
					stack.push(element);
				}
			} else if (element.equals(")")) {
				while (!stack.isEmpty()) {
					String item = stack.pop();
					if (!item.equals("(")) {
						postfix.offer(item);
					} else {
						break;
					}
				}

			} else {
				postfix.offer(element);
			}
		}

		// empty remaining operators
		while (!stack.isEmpty()) {
			postfix.offer(stack.pop());
		}

		return postfix;
	}

	/**
	 * This method converts all brackets to standard brackets -> (,)
	 * 
	 * @param input
	 *            the string to be converted
	 * @return the converted string
	 */
	private static String convertBrackets(String input) {
		String inputConverted;

		inputConverted = input;
		inputConverted = inputConverted.replace("{", "(");
		inputConverted = inputConverted.replace("}", ")");
		inputConverted = inputConverted.replace("[", "(");
		inputConverted = inputConverted.replace("]", ")");

		return inputConverted;
	}

	private static boolean isLegalOperation(String[] input) {
		int nrBracketsUnclosed = 0;

		if (input.length < 1) {
			return false;
		}

		if (!input[input.length - 1].matches("[a-z]{1,}")
				&& !input[input.length - 1].matches("[0-9]{1,}")
				&& !input[input.length - 1].matches("[\\(\\)]")) {

			return false;
		}

		if (!input[0].matches("[a-z]{1,}") && !input[0].matches("[0-9]{1,}")
				&& !input[0].matches("[-+].{0,}")
				&& !input[0].matches("[+-]{0,1}[0-9]{1,}[.]{0,1}[0-9]{1,}")
				&& !input[input.length - 1].matches("[\\(\\)]")) {
			return false;
		}

		for (String token : input) {
			if (token.equals("(")) {
				nrBracketsUnclosed++;
			} else if (token.equals(")")) {
				nrBracketsUnclosed--;
			} else if (!isOperator(token) && !token.matches("[a-z]{1,}") // checks
																			// if
																			// it
																			// not
																			// an
																			// unknown
																			// &
																			// an
																			// operator
					&& !token.matches("[+-]{0,1}[0-9]{1,}") // check if it it is
															// not a number
					&& !token.matches("[+-]{0,1}[0-9]{1,}[.]{0,1}[0-9]{1,}")) { // checks
																				// it
																				// is
																				// not
																				// a
																				// number
																				// with
																				// decimal
																				// point

				return false;
			}

		}
		if (nrBracketsUnclosed != 0) {

			return false;
		}

		return true;

	}

	public static double compute(LinkedList<String> postfix,
			Map<String, Double> variables) {
		LinkedList<String> copyList = new LinkedList<>(postfix);
		Stack<Double> results = new Stack<>();
		while (!copyList.isEmpty()) {
			if (!isOperator(copyList.peek())) {
				if (isNumber(copyList.peek())) {
					results.push(Double.parseDouble(copyList.poll()));
				} else {
					results.push(variables.get(copyList.poll()));
				}
			} else {
				Double operand1, operand2 = null;
				ArithmeticOperation operation = ArithmeticOperation
						.getInstance(copyList.poll());

				if (!(operation instanceof ArithmeticOperation.UnaryOperation)) {
					operand2 = results.pop();
				}
				if (!results.empty()) {
					operand1 = results.pop();
				} else {
					operand1 = 0.0;
				}
				results.push(operation.execute(operand1, operand2));
			}
		}

		return results.pop();
	}

	static String convertToString(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (String item : list) {
			builder.append(item);
			builder.append(" ");
		}
		return builder.toString()
				.replaceAll(ArithmeticOperation.UNARY_PLUS, "u+")
				.replaceAll(ArithmeticOperation.UNARY_MINUS, "u-");
	}

	public static List<String> getUnknowns(LinkedList<String> postfix) {
		List<String> unknowns = new ArrayList<>();
		for (String element : postfix) {
			if (!isNumber(element) && !isOperator(element)) {
				unknowns.add(element);
			}
		}
		return unknowns;
	}

	private static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private static boolean isOperator(String str) {
		return str.matches("^[+-/*\\^]$")
				|| str.equals(ArithmeticOperation.UNARY_PLUS)
				|| str.equals(ArithmeticOperation.UNARY_MINUS);
	}
}
