/**
 * This class provides a way for the program to transform a string defining an
 * operator into an corresponding object
 * 
 * @author Sorin
 * 
 */
public abstract class ArithmeticOperation {
	/**
	 * The simbol for the pow
	 */
	public static final String POWER = "^";
	/**
	 * The simbol for the division
	 */
	public static final String DIVIDE = "/";
	/**
	 * The simbol for the multiplication
	 */
	public static final String MULTIPLY = "*";
	/**
	 * The simbol for the minus
	 */
	public static final String MINUS = "-";
	/**
	 * The simbol for the plus
	 */
	public static final String PLUS = "+";
	/**
	 * The ascii simbol for the unary plus
	 */
	public static final String UNARY_PLUS = "\u2795";
	/**
	 * The ascii simbol for the unary minus
	 */
	public static final String UNARY_MINUS = "\u2014";

	/**
	 * This enumeration contains all the possible priorities of operators
	 * 
	 * @author Sorin
	 * 
	 */
	public static enum Precedence {
		VERY_STRONG(3), STRONG(2), WEAK(1), VERY_WEAK(0);

		private final int precedence;

		private Precedence(int precedence) {
			this.precedence = precedence;
		}

		public int getValue() {
			return precedence;
		}
	}

	/**
	 * This method provides a way to convert the operation String into its
	 * corresponding object
	 * 
	 * @param op
	 *            the operation
	 * @return the corresponding object for the operation
	 */
	public static ArithmeticOperation getInstance(String op) {
		if (op.equals(PLUS)) {
			return new Addition();
		} else if (op.equals(MINUS)) {
			return new Subtraction();
		} else if (op.equals(MULTIPLY)) {
			return new Multiplication();
		} else if (op.equals(DIVIDE)) {
			return new Division();
		} else if (op.equals(POWER)) {
			return new Power();
		} else if (op.equals(UNARY_PLUS)) {
			return new UnaryPlus();
		} else if (op.equals(UNARY_MINUS)) {
			return new UnaryMinus();
		} else {
			throw new IllegalArgumentException("The \"" + op
					+ "\" was not recognized");
		}
	}

	/**
	 * This method provides a way for an ArithmeticOperation object to execute
	 * its functionality
	 * 
	 * @param operand1
	 *            The first operand
	 * @param operand2
	 *            The second operand
	 * @return The result of the operation
	 */
	public abstract Double execute(Double operand1, Double operand2);

	/**
	 * This class provides a way to define the unary operators
	 * 
	 * @author Sorin
	 * 
	 */
	public abstract static class UnaryOperation extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return unaryExecution(operand1);
		}

		protected abstract Double unaryExecution(Double operand);
	}

	/**
	 * This class defines the unary plus operator
	 * 
	 * @author Sorin
	 * 
	 */
	private static class UnaryPlus extends UnaryOperation {
		@Override
		protected Double unaryExecution(Double operand) {
			return +operand;
		}
	}

	/**
	 * This class defines the unary minus operator
	 * 
	 * @author Sorin
	 * 
	 */
	private static class UnaryMinus extends UnaryOperation {

		@Override
		protected Double unaryExecution(Double operand) {
			return -operand;
		}
	}

	/**
	 * This class defines the addition operator
	 * 
	 * @author Sorin
	 * 
	 */
	private static class Addition extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return operand1 + operand2;
		}
	}

	/**
	 * This class defines the subtraction operator
	 * 
	 * @author Sorin
	 * 
	 */
	private static class Subtraction extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return operand1 - operand2;
		}
	}

	/**
	 * This class defines the multiplication operator
	 * 
	 * @author Sorin
	 * 
	 */
	private static class Multiplication extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return operand1 * operand2;
		}
	}

	/**
	 * This class defines the division operator
	 * 
	 * @author Sorin
	 * 
	 */
	private static class Division extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return operand1 / operand2;
		}
	}

	/**
	 * This class defines the power operator
	 * 
	 * @author Sorin
	 * 
	 */
	private static class Power extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return Math.pow(operand1, operand2);
		}
	}

	/**
	 * This method provides a way to get the precedence of a operation (the
	 * priority it has in execution). The first operation to be executed is the
	 * one with the strongest precedence
	 * 
	 * @param operator
	 *            the operator to be evaluated
	 * @return the priority of the evaluated operator
	 */
	public static int precedence(String operator) {
		switch (operator) {
		case UNARY_PLUS:
		case UNARY_MINUS:
			return Precedence.VERY_STRONG.getValue();
		case POWER:
			return Precedence.STRONG.getValue();
		case DIVIDE:
		case MULTIPLY:
			return Precedence.WEAK.getValue();
		case PLUS:
		case MINUS:
			return Precedence.VERY_WEAK.getValue();
		}
		return 0;
	}
}
