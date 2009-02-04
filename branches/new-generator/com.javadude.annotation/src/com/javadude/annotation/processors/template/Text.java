package com.javadude.annotation.processors.template;

import java.io.IOException;
import java.io.Writer;

import com.javadude.annotation.processors.Symbols;

public class Text extends Processor {
	private String text;

	public Text(String text) {
		this.text = text;
	}

	@Override public void process(Symbols symbols, Writer writer, int line, String padding) {
		try {
			if (padding != null) {
				// count & replace leading tabs
				int i = 0;
				String indent = "";
				while (i < text.length() && text.charAt(i) == '\t') {
					i++;
					indent += padding;
				}
				text = indent + text.substring(i);
			}
			writer.write(text);
		} catch (IOException e) {
			throw new ExpressionException("Could not write text '" + text + "' on line " + line, e);
		}
	}
}
