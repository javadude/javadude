package com.javadude.annotation.generators;

import com.javadude.annotation.Property;
import com.javadude.annotation.generators.model.TargetClass;
import com.javadude.annotation.generators.model.TargetField;
import com.javadude.annotation.generators.model.TargetMethod;
import com.javadude.annotation.generators.model.Util;

public class PropertyGenerator {
	public static void generate(TargetClass c, Property p) {
		TargetField field = new TargetField();
		field.setStatic(p.isStatic());
		field.setAccess("private");
		field.setVolatile(false);
		field.setFinal(false);
		field.setName(p.name());
		c.addField(field);
		if (!p.omitFromToString()) {
			c.addToStringField(field);
		}
		if (p.bound()) {
			c.requirePropertyChangeSupport(p.isStatic());
		}
		
		switch (p.kind()) {
			case LIST:
				field.setType(f(p, c, "java.util.List<${type}>"));
				if (p.bound()) {
					field.setInitializer(f(p, c, "new com.javadude.annotation.rt.ChangeReportingList(new java.util.ArrayList<${type}>(), \"${name}\", pcs)"));
				} else {
					field.setInitializer(f(p, c, "new java.util.ArrayList<${type}>()"));
				}
				c.addMethod(
						new TargetMethod(p.reader(), "java.util.List<${type}> get${upperName}()", p, c).
							addLine(p, c, "return ${name};")
				);
				break;
				
			case MAP:
				field.setType(f(p, c, "java.util.Map<${keyType}, ${type}>"));
				if (p.bound()) {
					field.setInitializer(f(p, c, "new com.javadude.annotation.rt.ChangeReportingMap(new java.util.HashMap<${keyType}, ${type}>(), \"${name}\", pcs)"));
				} else {
					field.setInitializer(f(p, c, "new java.util.HashMap<${keyType}, ${type}>()"));
				}
				c.addMethod(
						new TargetMethod(p.reader(), "java.util.Map<${keyType}, ${type}> get${upperName}()", p, c).
						addLine(p, c, "return ${name};")
				);
				break;

			case SET:
				field.setType(f(p, c, "java.util.Set<${type}>"));
				if (p.bound()) {
					field.setInitializer(f(p, c, "new com.javadude.annotation.rt.ChangeReportingSet(new java.util.HashSet<${type}>(), \"${name}\", pcs)"));
				} else {
					field.setInitializer(f(p, c, "new java.util.HashSet<${type}>()"));
				}
				c.addMethod(
						new TargetMethod(p.reader(), "java.util.Set<${type}> get${upperName}()", p, c).
						addLine(p, c, "return ${name};")
				);
				break;

			case SIMPLE:
				field.setName(p.name());
				field.setType(f(p, c, "${type}"));
				field.setInitializer(null);
				c.addMethod(
						new TargetMethod(p.reader(), "${type} get${upperName}()", p, c).
						addLine(p, c, "return ${name};")
				);
				c.addMethod(
						new TargetMethod(p.writer(), "void set${upperName}(${type} ${name})", p, c).
						addLine(p.notNull(),                p, c, "if (${name} == null)").
						addLine(p.notNull(),                p, c, "    throw new IllegalArgumentException(\"Property ${name} cannot be set to null via set${upperName}(${type} ${name})\");").
						addLine(p.bound(),                  p, c, "${type} old = this.${name};").
						addLine(p.isStatic(),               p, c, "${className}.${name} = ${name};").
						addLine(!p.isStatic(),              p, c, "this.${name} = ${name};").
						addLine(p.bound() && p.isStatic(),  p, c, "staticPcs.firePropertyChange(\"${name}\", old, ${name});").
						addLine(p.bound() && !p.isStatic(), p, c, "pcs.firePropertyChange(\"${name}\", old, ${name});")
				);
				break;
		}
	}
	private static final String f(Property property, TargetClass targetClass, String template) {
		return Util.formatProperty(property, targetClass, template);
	}
}
