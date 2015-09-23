package com.javadude.annotation.generators.model;

import java.io.PrintWriter;

public class TargetField {
	private String access;
	private boolean isFinal;
	private boolean isStatic;
	private boolean isVolatile;
	private String name;
	private String type;
	private String initializer;

	public TargetField() {
	}
	public TargetField(String name, String type, String initializer) {
		this(name, type, initializer, false);
	}
	public TargetField(String name, String type, String initializer, boolean isStatic) {
		this.name = name;
		this.type = type;
		this.initializer = initializer;
		this.isFinal = false;
		this.isStatic = isStatic;
		this.isVolatile = false;
		this.access = "private";
	}
	public void setInitializer(String initializer) {
		this.initializer = initializer;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	public void setVolatile(boolean isVolatile) {
		this.isVolatile = isVolatile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void generate(PrintWriter pw, String indentUnit, String currentIndent) {
		pw.print(currentIndent);
		if (access != null && !"".equals(access)) {
			pw.print(access);
			pw.print(' ');
		}
		if (isFinal) {
			pw.print("final ");
		}
		if (isStatic) {
			pw.print("static ");
		}
		if (isVolatile) {
			pw.print("volatile ");
		}
		pw.print(type);
		pw.print(' ');
		pw.print(name);
		if (initializer != null) {
			pw.print(" = ");
			pw.print(initializer);
		}
		pw.println(';');
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TargetField other = (TargetField) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
