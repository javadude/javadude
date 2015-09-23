package com.javadude.annotation.processors;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.javadude.annotation.processors.template.ExpressionException;

public class Symbols {
	private static final char DELIMITER = '_';
	private static final String UPPER = "UPPER";
	private static final String LOWER = "LOWER";
	private static final String NOPACKAGE = "NOPACKAGE";
	private static final String PARENT = "PARENT";
	public static final Object NULL_VALUE = new Object() {@Override public String toString() {return "<NULL>";}};
	private Stack<Map<String, Object>> values_ = new Stack<Map<String, Object>>();
	public Symbols() {
		pushScope();
	}
	public Symbols(Map<String, Object> newScope) {
		pushScope(newScope);
	}
	public void pushScope() {
		values_.push(new HashMap<String, Object>());
	}
	public void pushScope(String key, Object value) {
		pushScope();
		put(key, value);
	}
	public void pushScope(String key, Object value, String key2, Object value2) {
		pushScope();
		put(key, value);
		put(key2, value2);
	}
	public void pushScope(Map<String, Object> newScope) {
		pushScope();
		for (Map.Entry<String, Object> entry : newScope.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}
	public void popScope() {
		values_.pop();
	}
	public Object get(String key, int line) {
		int scope = values_.size() - 1; // which scope to start lookup
		boolean upper = false;
		boolean lower = false;
		boolean noPackage = false;

		String originalKey = key;

		// pick off the modifiers
		while (true) {
			int delimiter = key.indexOf(Symbols.DELIMITER);
			if (delimiter == -1)
				break;
			if (delimiter == key.length())
				throw new RuntimeException("Invalid variable reference; trailing delimiter (" + Symbols.DELIMITER + "): " + originalKey + " on line " + line);
			String before = key.substring(0, delimiter);
			String after = key.substring(delimiter + 1);
			if (Symbols.PARENT.equals(before)) {
				scope--;
				key = after;
			} else if (Symbols.UPPER.equals(before)) {
				if (upper)
					throw new ExpressionException("Multiple " + Symbols.UPPER + " refs in " + originalKey + " on line " + line);
				upper = true;
				key = after;
			} else if (Symbols.LOWER.equals(before)) {
				if (lower)
					throw new ExpressionException("Multiple " + Symbols.LOWER + " refs in " + originalKey + " on line " + line);
				lower = true;
				key = after;
			} else if (Symbols.NOPACKAGE.equals(before)) {
				if (noPackage)
					throw new ExpressionException("Multiple " + Symbols.NOPACKAGE + " refs in " + originalKey + " on line " + line);
				noPackage = true;
				key = after;
			} else {
				// must be a variable with throw-away suffix
				key = before; // throw away the suffixes
			}
		}
		while (scope >= 0) {
			Object value = values_.get(scope).get(key);
			if (value != null) {
				if (noPackage){
			        int i = ((String) value).lastIndexOf('.');
			        if (i != -1) {
			        	value = ((String) value).substring(i + 1);
			        }
				}
				if (upper)
					value = Utils.upperFirstChar(value);
				if (lower)
					value = Utils.lowerFirstChar(value);
				return value;
			}
			scope--;
		}
		throw new ExpressionException("Variable '" + key + "' undefined for expression '" + originalKey + "' on line " + line);
	}
	public void put(String key, Object value) {
		if (value == null)
			value = Symbols.NULL_VALUE;
		values_.peek().put(key, value);
	}
	public boolean testSymbol(String variable, int line) {
		Object value = get(variable, line);
		if (value == Symbols.NULL_VALUE)
			return false;
		if (value.getClass() == Boolean.class)
			return ((Boolean) value).booleanValue();
		else if (value instanceof Collection<?>)
			return !((Collection<?>) value).isEmpty();
		else
			return true;

	}
}
