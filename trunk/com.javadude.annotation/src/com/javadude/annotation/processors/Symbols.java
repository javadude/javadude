package com.javadude.annotation.processors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Symbols {
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
	public void pushScope(Map<String, Object> newScope) {
		values_.push(Collections.unmodifiableMap(newScope));
	}
	public void popScope() {
		values_.pop();
	}
	public Object get(String key) {
		int num = values_.size() - 1;
		while (key.startsWith("up:")) {
			key = key.substring(3);
			num--;
		}
		while (num >= 0) {
			Object value = values_.get(num).get(key);
			if (value != null) {
				return value;
			}
			num--;
		}
		return null;
	}
	public void put(String key, Object value) {
		values_.peek().put(key, value);
	}
}
