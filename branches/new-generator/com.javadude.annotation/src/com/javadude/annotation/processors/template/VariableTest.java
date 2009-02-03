package com.javadude.annotation.processors.template;

import com.javadude.annotation.processors.Symbols;

public class VariableTest extends Condition {
	private String variable;

	public VariableTest(String variable) {
		this.variable = variable;
	}

	@Override public boolean test(Symbols symbols, int line) {
		return symbols.testSymbol(variable, line);
	}
	@Override public String paramString() {
		return "variable=" + variable;
	}
}
