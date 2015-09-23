package com.javadude.annotation.processors.template;

import java.io.Writer;

import com.javadude.annotation.processors.Symbols;

public class Template extends CompositeProcessor {
	public Template(int line) {
		super(line);
	}

	@Override
	public void process(Symbols symbols, Writer writer, int ignoredLine, String padding) {
		processChildren(symbols, writer, padding);
	}
}
