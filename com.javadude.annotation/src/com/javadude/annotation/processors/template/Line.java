package com.javadude.annotation.processors.template;

import java.io.IOException;
import java.io.Writer;

import com.javadude.annotation.processors.Symbols;

public class Line extends CompositeProcessor {
	private Condition condition;
	private static final String NL = System.getProperty("line.separator");

	public Line(int line, Condition condition) {
		super(line);
		this.condition = condition;
	}

	@Override public void process(Symbols symbols, Writer writer, int ignoredLine, String padding) {
		if (condition != null && !condition.test(symbols, getLine()))
			return;
		processChildren(symbols, writer, padding);
		try {
			writer.write(Line.NL);
		} catch (IOException e) {
			throw new ExpressionException("IO Error while writing generated code", e);
		}
	}
}
