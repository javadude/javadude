package com.javadude.annotation.processors.template;

import com.javadude.annotation.processors.Symbols;

public abstract class Condition {
	public abstract boolean test(Symbols symbols, int line);
	@Override public String toString() {
		return getClass().getSimpleName() + "[" + paramString() + ']';
	}
	public abstract String paramString();
}
