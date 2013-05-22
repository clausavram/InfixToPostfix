import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class FixConverter {

	public static LinkedList<String> convertInToPostfix(String input) {
		String[] infix = input.replaceAll(" ", "").replaceAll("[+\\(\\)\\-\\*\\^\\/]", " $0 ").replaceAll(" +", " ").trim().split(" ");
		Stack<String> stack = new Stack<String>();
		LinkedList<String> postfix = new LinkedList<>(); // Queue: offer / peek / poll

		// Unary adaptation:
		for (int i = 0; i < infix.length; i++) {
			if (infix[i].matches("[+-]") && (i == 0 || isOperator(infix[i - 1]) || infix[i - 1].matches("\\("))) {
				infix[i] = (infix[i].equals("+")) ? ArithmeticOperation.UNARY_PLUS : ArithmeticOperation.UNARY_MINUS;
			}
		}

		// Reverse Polish Notation conversion:
		for (String element : infix) {
			if (element.equals("(")) {
				stack.push(element);
			} else if (isOperator(element)) {
				while (!stack.isEmpty() && !stack.peek().equals(")")) {
					if (ArithmeticOperation.precedence(stack.peek()) >= ArithmeticOperation.precedence(element)) {
						postfix.offer(stack.pop());
					} else {
						break;
					}
				}
				stack.push(element);
			} else if (element.equals(")")) {
				while (!stack.isEmpty() && !stack.peek().equals("(")) {
					postfix.offer(stack.pop());
				}
				if (!stack.isEmpty()) {
					stack.pop();
				}
			} else {
				postfix.offer(element);
			}
		}

		// empty remaining opartors
		while (!stack.isEmpty()) {
			postfix.offer(stack.pop());
		}

		return postfix;
	}

	public static double compute(LinkedList<String> postfix, Map<String, Double> variables) {
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
				ArithmeticOperation operation = ArithmeticOperation.getInstance(copyList.poll());

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
		return builder.toString().replaceAll(ArithmeticOperation.UNARY_PLUS, "u+").replaceAll(ArithmeticOperation.UNARY_MINUS, "u-");
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
		return str.matches("^[+-/*\\^]$") || str.equals(ArithmeticOperation.UNARY_PLUS) || str.equals(ArithmeticOperation.UNARY_MINUS);
	}
}
