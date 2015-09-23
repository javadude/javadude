package com.javadude.annotation.generators.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.javadude.annotation.Access;
import com.javadude.annotation.Property;

public class TargetMethod {
	private Access access;
	private String signature;
	private boolean isStatic;
	private boolean isSynchronized;
	private List<String> lines = new ArrayList<String>();

	public TargetMethod(Access access, String signaturePattern, Property property, TargetClass targetClass) {
		this(access, Util.formatProperty(property, targetClass, signaturePattern), property.isStatic(), property.isSynchronized());
	}
	public TargetMethod(Access access, String signature, boolean isStatic, boolean isSynchronized) {
		this.access = access;
		this.signature = signature;
		this.isStatic = isStatic;
		this.isSynchronized = isSynchronized;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	public void setSynchronized(boolean isSynchronized) {
		this.isSynchronized = isSynchronized;
	}
	public TargetMethod addLine(Property property, TargetClass targetClass, String lineTemplate) {
		lines.add(Util.formatProperty(property, targetClass, lineTemplate));
		return this;
	}
	public TargetMethod addLine(boolean test, Property property, TargetClass targetClass, String lineTemplate) {
		if (test) {
			addLine(property, targetClass, lineTemplate);
		}
		return this;
	}
	public void generate(PrintWriter pw, String indentUnit, String currentIndent) {
		pw.print(currentIndent);
		pw.print(access.getModifier());
		if (isStatic) {
			pw.print("static");
			pw.print(' ');
		}
		if (isSynchronized) {
			pw.print("synchronized");
			pw.print(' ');
		}
		pw.print(signature);
		pw.println(" {");
		String lineIndent = currentIndent + indentUnit;
		for (String line : lines) {
			pw.print(lineIndent);
			pw.println(line);
		}
		pw.print(currentIndent);
		pw.println("}");
	}
}
