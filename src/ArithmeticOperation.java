public abstract class ArithmeticOperation {
	public static final String POWER = "^";
	public static final String DIVIDE = "/";
	public static final String MULTIPLY = "*";
	public static final String MINUS = "-";
	public static final String PLUS = "+";
	public static final String UNARY_PLUS = "\u2795";
	public static final String UNARY_MINUS = "\u2014";

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
			throw new IllegalArgumentException("The \"" + op + "\" was not recognized");
		}
	}

	public abstract Double execute(Double operand1, Double operand2);

	public abstract static class UnaryOperation extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return unaryExecution(operand1);
		}

		protected abstract Double unaryExecution(Double operand);
	}

	private static class UnaryPlus extends UnaryOperation {
		@Override
		protected Double unaryExecution(Double operand) {
			return +operand;
		}
	}

	private static class UnaryMinus extends UnaryOperation {

		@Override
		protected Double unaryExecution(Double operand) {
			return -operand;
		}
	}

	private static class Addition extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return operand1 + operand2;
		}
	}

	private static class Subtraction extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return operand1 - operand2;
		}
	}

	private static class Multiplication extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return operand1 * operand2;
		}
	}

	private static class Division extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return operand1 / operand2;
		}
	}

	private static class Power extends ArithmeticOperation {
		@Override
		public Double execute(Double operand1, Double operand2) {
			return Math.pow(operand1, operand2);
		}
	}

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
