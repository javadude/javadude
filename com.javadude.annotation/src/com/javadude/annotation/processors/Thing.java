package com.javadude.annotation.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Thing extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	private static final Map<String, List<String>> FIELDS = new HashMap<String, List<String>>();
	private static final Map<String, List<String>> LISTS = new HashMap<String, List<String>>();

	private static void setFields(String type, String...fields) {
		FIELDS.put(type, Arrays.asList(fields));
	}
	private static void setLists(String type, String...lists) {
		LISTS.put(type, Arrays.asList(lists));
	}
	static {
		setFields("data",
			"packageName", "firstPropertyName", "className", "classModifiers", "superclass",
			"genericDecls", "paramStringModifiers", "date", "year", "cloneable", "atLeastOneObject",
			"atLeastOneDouble", "definePropertyNameConstants", "extendPropertyNameConstants", "equalsShouldCheckSuperEquals",
			"defineEqualsAndHashCode", "definePropertyChangeSupport", "extendParamString", "defineCreatePropertyMap",
			"createPropertyMapCallsSuper");
		setLists("data", "properties", "superclassConstructors", "observers", "defaultMethods", "delegates", "nullObjects");

		setFields("method", "name", "args", "argDecls", "genericDecls", "returnType", "throwsClause", "modifiers", "nullBody", "abstract", "returns");

		setFields("property", "name", "writerAccess", "readerAccess", "type", "notNull", "readable", "writeable", "bound",
			"primitive", "pluralName", "keyType", "extraMethodKeywords", "extraFieldKeywords", "omitFromToString", "kind",
			"simple", "list", "set", "map", "boolean", "short", "double", "long", "int", "char", "byte", "float");

		setFields("type", "name");
		setLists("type", "methods");
	}
	private String type;
	public Thing(String type) {
		this.type = type;
		if (!FIELDS.containsKey(type))
			throw new RuntimeException("Type " + type + " does not exist");
	}
	public void set(String name, Object value) {
		if (FIELDS.get(type) == null)
			throw new RuntimeException("Field " + name + " does not exists as a single value field in type " + type);
		super.put(name, value);
	}
	@SuppressWarnings("unchecked")
	public void add(String name, Object value) {
		if (LISTS.get(type) != null)
			throw new RuntimeException("Field " + name + " in type " + type + " is not a collection field");
		List<Object> list = (List<Object>) get(name);
		if (list == null) {
			list = new ArrayList<Object>();
			super.put(name, list);
		}
		list.add(value);
	}
	@Override
	public Object put(String key, Object value) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

}
