package com.javadude.annotation.processors;

import java.util.HashMap;

public class NullHoldingMap extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	@Override public Object put(String key, Object value) {
		if (value == null)
			value = Symbols.NULL_VALUE;
		return super.put(key, value);
	}
}
