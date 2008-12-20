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
		values_.push(Collections.unmodifiableMap(newScope));
	}
	public void popScope() {
		values_.pop();
	}
	public Object get(String key) {
		int num = values_.size() - 1;
		while (key.startsWith("parent:")) {
			key = key.substring(7);
			num--;
		}
		boolean upper = false;
		boolean lower = false;
		boolean stripPackage = false;
		if (key.startsWith("stripPackage:")) {
			key = key.substring(13);
			stripPackage = true;
		}
		if (key.startsWith("upper:")) {
			upper = true;
			key = key.substring(6);
		} else if (key.startsWith("lower:")) {
			lower = true;
			key = key.substring(6);
		}
		while (num >= 0) {
			Object value = values_.get(num).get(key);
			if (value != null) {
				if (stripPackage){
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
			num--;
		}
		return null;
	}
	public void put(String key, Object value) {
		values_.peek().put(key, value);
	}
}
