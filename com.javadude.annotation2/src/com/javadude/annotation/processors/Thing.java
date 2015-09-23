package com.javadude.annotation.processors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;

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
			"atLeastOneDouble", "atLeastOneDefault", "atLeastOneBound", "definePropertyNameConstants", "propertyNameConstantsInherited", "equalsAndHashCodeCallSuper",
			"defineEqualsAndHashCode", "definePropertyChangeSupport", "defineCreatePropertyMap",
			"createPropertyMapInherited", "createPropertyMapModifiers",
			"getPropertyChangeSupportInherited", "getPropertyChangeSupportModifiers",
			"paramStringInherited", "paramStringModifiers");
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
		if (!FIELDS.containsKey(type)) {
			throw new RuntimeException("Internal Error: Type " + type + " does not exist");
		}
	}
	@SuppressWarnings("unchecked")
	public void add(String name, Object value) {
		List<String> lists = LISTS.get(type);
		if (lists == null) {
			throw new RuntimeException("Internal Error: Type " + type + " not defined");
		} else if (!lists.contains(name)) {
			throw new RuntimeException("Internal Error: Field " + name + " in type " + type + " is not a collection field");
		}
		if (value == null) {
			value = Symbols.NULL_VALUE;
		}
		List<Object> list = (List<Object>) get(name);
		if (list == null) {
			list = new ArrayList<Object>();
			super.put(name, list);
		}
		list.add(value);
	}
	public void setEmpty(String name) {
		List<String> lists = LISTS.get(type);
		if (lists == null) {
			throw new RuntimeException("Internal Error: Type " + type + " not defined");
		} else if (!lists.contains(name)) {
			throw new RuntimeException("Internal Error: Field " + name + " in type " + type + " is not a collection field");
		}
		super.put(name, Collections.emptyList());
	}
	@Override
	public Object put(String key, Object value) {
		List<String> fields = FIELDS.get(type);
		if (fields == null) {
			throw new RuntimeException("Internal Error: Type " + type + " not defined");
		} else if (!fields.contains(key)) {
			throw new RuntimeException("Internal Error: Field " + key + " does not exist as a single value field in type " + type);
		}
		if (value == null) {
			value = Symbols.NULL_VALUE;
		}
		return super.put(key, value);
	}
	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}
	public void checkAllValuesSet(Declaration declaration, BeanAnnotationProcessor beanAnnotationProcessor) {
		for (String field : FIELDS.get(type)) {
			if (get(field) == null) {
				beanAnnotationProcessor.error(declaration, "Internal error: Field " + field + " for type " + type + " not set in processor");
			}
		}
		if (LISTS.get(type) != null) {
			for (String field : LISTS.get(type)) {
				if (get(field) == null) {
					beanAnnotationProcessor.error(declaration, "Internal error: Field " + field + " for type " + type + " not set in processor");
				}
			}
		}
	}
	public void checkAllValuesSet(AnnotationValue value, BeanAnnotationProcessor beanAnnotationProcessor) {
		for (String field : FIELDS.get(type)) {
			if (get(field) == null) {
				beanAnnotationProcessor.error(value, "Internal error: Field " + field + " for type " + type + " not set in processor");
			}
		}
		if (LISTS.get(type) != null) {
			for (String field : LISTS.get(type)) {
				if (get(field) == null) {
					beanAnnotationProcessor.error(value, "Internal error: Field " + field + " for type " + type + " not set in processor");
				}
			}
		}
	}

}
