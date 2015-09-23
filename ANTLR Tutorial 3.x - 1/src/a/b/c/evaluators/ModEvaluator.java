package a.b.c.evaluators;

public class ModEvaluator implements Evaluator {
	private final Evaluator op1;
	private final Evaluator op2;
	public ModEvaluator(Evaluator op1, Evaluator op2) {
		this.op1 = op1;
		this.op2 = op2;
	}
	@Override public int evaluate() {
		return op1.evaluate() % op2.evaluate();
	}
	public String toTreeString(String indent) {
		return '\n' + indent + "Mod" +
			op1.toTreeString(indent + "  ") +
			op2.toTreeString(indent + "  ");
	}
}
