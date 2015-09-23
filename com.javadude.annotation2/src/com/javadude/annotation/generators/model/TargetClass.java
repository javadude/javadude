package com.javadude.annotation.generators.model;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.javadude.annotation.Access;

public class TargetClass {
	private List<String> annotations = new ArrayList<String>();
	private String pkg;
	private Access access;
	private String name;
	private String superclass;
	private boolean isFinal;
	private boolean isStatic;
	private boolean isAbstract;
	private boolean requiresPropertyChangeSupport;
	private boolean requiresStaticPropertyChangeSupport;
	private List<String> interfaces = new ArrayList<String>();
	private List<TargetMethod> methods = new ArrayList<TargetMethod>();
	private List<TargetClass> innerClasses = new ArrayList<TargetClass>();
	private List<TargetField> fields = new ArrayList<TargetField>();
	private List<TargetField> toStringFields = new ArrayList<TargetField>();
	public void setPackage(String pkg) {
		this.pkg = pkg;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public String getPackage() {
		return pkg;
	}
	
	public void requirePropertyChangeSupport(boolean isStaticProperty) {
		if (isStaticProperty) {
			if (!this.requiresStaticPropertyChangeSupport) {
				fields.add(new TargetField("staticPcs", "java.beans.PropertyChangeSupport", "new java.beans.PropertyChangeSupport(" + pkg + '.' + name + ".class)", true));
				methods.add(new TargetMethod(Access.PUBLIC, "void addStaticPropertyChangeListener(java.beans.PropertyChangeListener listener)", true, false).
						addLine(null, this, "staticPcs.addPropertyChangeListener(listener);")
				);
				methods.add(new TargetMethod(Access.PUBLIC, "void addStaticPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener)", true, false).
						addLine(null, this, "staticPcs.addPropertyChangeListener(propertyName, listener);")
				);
				methods.add(new TargetMethod(Access.PUBLIC, "void removeStaticPropertyChangeListener(java.beans.PropertyChangeListener listener)", true, false).
						addLine(null, this, "staticPcs.removePropertyChangeListener(listener);")
				);
				methods.add(new TargetMethod(Access.PUBLIC, "void removeStaticPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener)", true, false).
						addLine(null, this, "staticPcs.removePropertyChangeListener(propertyName, listener);")
				);
				this.requiresStaticPropertyChangeSupport = true;
			}
		} else {
			if (!this.requiresPropertyChangeSupport) {
				fields.add(new TargetField("pcs", "java.beans.PropertyChangeSupport", "new java.beans.PropertyChangeSupport(this)"));
				methods.add(new TargetMethod(Access.PUBLIC, "void addPropertyChangeListener(java.beans.PropertyChangeListener listener)", false, false).
						addLine(null, this, "pcs.addPropertyChangeListener(listener);")
				);
				methods.add(new TargetMethod(Access.PUBLIC, "void addPropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener)", false, false).
						addLine(null, this, "pcs.addPropertyChangeListener(propertyName, listener);")
				);
				methods.add(new TargetMethod(Access.PUBLIC, "void removePropertyChangeListener(java.beans.PropertyChangeListener listener)", false, false).
						addLine(null, this, "pcs.removePropertyChangeListener(listener);")
				);
				methods.add(new TargetMethod(Access.PUBLIC, "void removePropertyChangeListener(java.lang.String propertyName, java.beans.PropertyChangeListener listener)", false, false).
						addLine(null, this, "pcs.removePropertyChangeListener(propertyName, listener);")
				);
				this.requiresPropertyChangeSupport = true;
			}
		}
	}
	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	public void addInterface(String iface) {
		if (!interfaces.contains(iface)) {
			interfaces.add(iface);
		}
	}
	public void addInnerClass(TargetClass innerClass) {
		if (innerClasses.contains(innerClass)) {
			throw new IllegalStateException("Adding two inner classes with the same name (" + innerClass.name + ")!");
		}
		innerClasses.add(innerClass);
	}
	public void addAnnotation(String annotation) {
		if (!annotations.contains(annotation)) {
			annotations.add(annotation);
		}
	}
	public void addField(TargetField field) {
		if (fields.contains(field)) {
			throw new IllegalStateException("Adding two fields with the same name (" + field.getName() + ")!");
		}
		fields.add(field);
	}
	public void addToStringField(TargetField field) {
		toStringFields.add(field);
	}
	public void addMethod(TargetMethod method) {
		// TODO check for duplicates!
		methods.add(method);
	}
	public void generate(PrintWriter pw, String indentUnit, String currentIndent) {
		// TODO TRANSLATE LEADING '    ' IN LINES TO SPECIFIED INDENT
		if (pkg != null) {
			pw.print(currentIndent);
			pw.println("package " + pkg + ';');
		}
		for (String annotation : annotations) {
			pw.print(currentIndent);
			pw.println(annotation);
		}
		pw.print(currentIndent);
		if (access == Access.PUBLIC) {
			pw.print("public ");
		}
		if (isStatic) {
			pw.print("static ");
		}
		if (isAbstract) {
			pw.print("abstract ");
		}
		if (isFinal) {
			pw.print("final ");
		}
		pw.print("class " + name + ' ');
		if (superclass != null) {
			pw.print("extends " + superclass + ' ');
		}
		if (!interfaces.isEmpty()) {
			pw.print("implements ");
			boolean first = true;
			for (String iface : interfaces) {
				if (first) {
					first = false;
				} else {
					pw.print(", ");
				}
				pw.print(iface);
			}
		}
		pw.println('{');
		if (!innerClasses.isEmpty()) {
			for (TargetClass innerClass : innerClasses) {
				innerClass.generate(pw, indentUnit, currentIndent + indentUnit);
			}
			pw.println();
		}
		if (!fields.isEmpty()) {
			for (TargetField field : fields) {
				field.generate(pw, indentUnit, currentIndent + indentUnit);
			}
			pw.println();
		}
		for (TargetMethod method : methods) {
			method.generate(pw, indentUnit, currentIndent + indentUnit);
		}
		
		// TODO generate toString() and hashCode() using toStringFields
		
		pw.print(currentIndent);
		pw.println('}');
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
		TargetClass other = (TargetClass) obj;
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
