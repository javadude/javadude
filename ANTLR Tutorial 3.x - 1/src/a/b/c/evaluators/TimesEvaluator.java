package a.b.c.evaluators;

public class TimesEvaluator implements Evaluator {
	private final Evaluator op1;
	private final Evaluator op2;
	public TimesEvaluator(Evaluator op1, Evaluator op2) {
		this.op1 = op1;
		this.op2 = op2;
	}
	@Override public int evaluate() {
		return op1.evaluate() * op2.evaluate();
	}
	public String toTreeString(String indent) {
		return '\n' + indent + "Times" +
			op1.toTreeString(indent + "  ") +
			op2.toTreeString(indent + "  ");
	}
}
