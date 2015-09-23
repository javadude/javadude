package com.javadude.annotation.processors.template;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.javadude.annotation.processors.Symbols;

public class ForEach extends CompositeProcessor {
	private String expression;
	public ForEach(int line, String expression) {
		super(line);
		this.expression = expression;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(Symbols symbols, Writer writer, int ignoredLine, String padding) {
		Object value = symbols.get(expression, getLine());
		if (value == Symbols.NULL_VALUE)
			throw new ExpressionException("Expression '" + expression + "' has null value in FOREACH on line " + getLine());
		if (!(value instanceof Collection<?>)) {
			throw new ExpressionException("Expression '" + expression + "' has a non-collection value in FOREACH on line " + getLine());
		}
		Collection<Map<String, Object>> values = (Collection<Map<String, Object>>) value;
		boolean first = true;
		for (Iterator<Map<String, Object>> i = values.iterator(); i.hasNext();) {
			try {
				Map<String, Object> item = i.next();
				symbols.pushScope(item);
				symbols.put("LAST", !i.hasNext());
				symbols.put("FIRST", first);
				first = false;
				processChildren(symbols, writer, padding);
			} finally {
				symbols.popScope();
			}
		}
	}
}
