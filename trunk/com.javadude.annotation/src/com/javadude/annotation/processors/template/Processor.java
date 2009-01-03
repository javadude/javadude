package com.javadude.annotation.processors.template;

import java.io.Writer;

import com.javadude.annotation.processors.Symbols;

public abstract class Processor {
	public Processor getNext() { return next; }
	public void setNext(Processor next) { this.next = next; }
	private Processor next;
	public abstract void process(Symbols symbols, Writer writer, int line);
}
