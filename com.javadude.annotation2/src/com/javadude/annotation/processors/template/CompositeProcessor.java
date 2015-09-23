package com.javadude.annotation.processors.template;

import java.io.Writer;

import com.javadude.annotation.processors.Symbols;

public abstract class CompositeProcessor extends Processor {
	private Processor children;
	private Processor lastChild;
	private int line;

	public CompositeProcessor(int line) {
		this.line = line;
	}

	public int getLine() {
		return line;
	}
	public void add(Processor processor) {
		if (lastChild == null) {
			children = processor;
		} else {
			lastChild.setNext(processor);
		}
		lastChild = processor;
	}

	protected void processChildren(Symbols symbols, Writer writer, String padding) {
		Processor p = children;
		while (p != null) {
			p.process(symbols, writer, line, padding);
			p = p.getNext();
		}
	}
}
