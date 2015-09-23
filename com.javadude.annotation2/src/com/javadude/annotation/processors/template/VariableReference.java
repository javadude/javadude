package com.javadude.annotation.processors.template;

import java.io.IOException;
import java.io.Writer;

import com.javadude.annotation.processors.Symbols;

public class VariableReference extends Processor {
	private String variable;

	public VariableReference(String variable) {
		this.variable = variable;
	}

	@Override public void process(Symbols symbols, Writer writer, int line, String padding) {
		Object value = symbols.get(variable, line).toString();
		if (value == Symbols.NULL_VALUE)
			throw new ExpressionException("Variable '" + variable + "' has null value on line " + line);
		try {
			writer.write(symbols.get(variable, line).toString());
		} catch (IOException e) {
			throw new ExpressionException("Could not write varible '" + variable + "' (value='" + value + "') on line " + line, e);
		}
	}
}
