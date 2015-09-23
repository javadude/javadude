package com.javadude.annotation.processors.template;

import com.javadude.annotation.processors.Symbols;

public class And extends Condition {
	private Condition op1;
	private Condition op2;

	public And(Condition op1, Condition op2) {
		this.op1 = op1;
		this.op2 = op2;
	}

	@Override public boolean test(Symbols symbols, int line) {
		return op1.test(symbols, line) && op2.test(symbols, line);
	}

	@Override public String paramString() {
		return "op1=" + op1 + ",op2=" + op2;
	}
}
