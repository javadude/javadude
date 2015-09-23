package com.javadude.annotation.processors.template;

import com.javadude.annotation.processors.Symbols;

public class Not extends Condition {
	private Condition op;

	public Not(Condition op) {
		this.op = op;
	}

	@Override public boolean test(Symbols symbols, int line) {
		return !op.test(symbols, line);
	}
	@Override public String paramString() {
		return "op=" + op;
	}
}
