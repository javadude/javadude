package com.javadude.annotation.test;

import java.lang.annotation.Annotation;

import com.javadude.annotation.Access;
import com.javadude.annotation.Property;
import com.javadude.annotation.PropertyKind;

public class MockProperty implements Property {
	private boolean bound;
	private boolean isStatic;
	private boolean isSynchronized;
	private boolean notNull;
	private boolean omitFromToString;
	private Class<?> keyType;
	private String keyTypeString;
	private Class<?> type;
	private String typeString;
	private String name;
	private Access reader;
	private Access writer;
	private PropertyKind kind;
	
	public MockProperty(boolean bound, boolean isStatic,
			boolean isSynchronized, boolean notNull, boolean omitFromToString,
			Class<?> keyType, String keyTypeString, Class<?> type,
			String typeString, String name, Access reader, Access writer,
			PropertyKind kind) {
		this.bound = bound;
		this.isStatic = isStatic;
		this.isSynchronized = isSynchronized;
		this.notNull = notNull;
		this.omitFromToString = omitFromToString;
		this.keyType = keyType;
		this.keyTypeString = keyTypeString;
		this.type = type;
		this.typeString = typeString;
		this.name = name;
		this.reader = reader;
		this.writer = writer;
		this.kind = kind;
	}
	public boolean bound() { return bound; }
	public boolean isStatic() { return isStatic; }
	public boolean isSynchronized() { return isSynchronized; }
	public boolean notNull() { return notNull; }
	public boolean omitFromToString() { return omitFromToString; }
	public Class<?> keyType() { return keyType; }
	public String keyTypeString() { return keyTypeString; }
	public Class<?> type() { return type; }
	public String typeString() { return typeString; }
	public String name() { return name; }
	public Access reader() { return reader; }
	public Access writer() { return writer; }
	public PropertyKind kind() { return kind; }

	public Class<? extends Annotation> annotationType() {
		return null;
	}
}
