package com.javadude.annotation.processors.template;

import java.io.Writer;

import com.javadude.annotation.processors.Symbols;

public class Context {
	private Symbols symbols;
	private int line;
	private Writer writer;
	private String indent;
	public Symbols getSymbols() {
		return symbols;
	}
	public void setSymbols(Symbols symbols) {
		this.symbols = symbols;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public Writer getWriter() {
		return writer;
	}
	public void setWriter(Writer writer) {
		this.writer = writer;
	}
	public String getIndent() {
		return indent;
	}
	public void setIndent(String indent) {
		this.indent = indent;
	}
}
