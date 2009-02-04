package com.javadude.annotation.processors.template;

import java.io.Writer;

import com.javadude.annotation.processors.Symbols;

public class If extends CompositeProcessor {
	private Condition condition;
	public If(int line, Condition condition) {
		super(line);
		this.condition = condition;
	}

	@Override
	public void process(Symbols symbols, Writer writer, int ignoredLine, String padding) {
		if (condition.test(symbols, ignoredLine))
			processChildren(symbols, writer, padding);
	}
}
