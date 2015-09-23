package com.javadude.annotation.generators.model;

import com.javadude.annotation.Property;

public class Util {
	public static String formatProperty(Property property, TargetClass targetClass, String template) {
		StringBuffer buffer = new StringBuffer();
		int startVar;
		int endVar = -1;
		loop: while (true) {
			if (endVar == -1) {
				startVar = template.indexOf("${");
			} else {
				startVar = template.indexOf("${", endVar+1);
			}
			if (startVar == -1) {
				if (endVar + 1 < template.length()) {
					// append from end of last variable to end of pattern
					buffer.append(template.substring(endVar + 1));
				}
				break loop;
			}
			
			// append from end of last variable to the current variable
			buffer.append(template.substring(endVar + 1, startVar));
			// find the end of this variable
			endVar = template.indexOf('}', startVar);
			if (endVar == -1) {
				throw new IllegalArgumentException("Pattern '" + template + "' does not have proper balanced ${...}");
			}
			String variableName = template.substring(startVar + 2, endVar);

			// TODO add simpleType, simpleKeyType - names w/o package qualification
			if ("type".equals(variableName)) {
				if (property == null) {
					throw new IllegalArgumentException("Null passed for property and template asked for ${type}");
				}
				buffer.append(getTypeString(property.typeString(), property.type()));
			} else if ("keyType".equals(variableName)) {
				if (property == null) {
					throw new IllegalArgumentException("Null passed for property and template asked for ${keyType}");
				}
				buffer.append(getTypeString(property.keyTypeString(), property.keyType()));
			} else if ("name".equals(variableName)) {
				if (property == null) {
					throw new IllegalArgumentException("Null passed for property and template asked for ${name}");
				}
				buffer.append(property.name());
			} else if ("upperName".equals(variableName)) {
				if (property == null) {
					throw new IllegalArgumentException("Null passed for property and template asked for ${upperName}");
				}
				buffer.append(upperFirst(property.name()));
			} else if ("className".equals(variableName)) {
				if (targetClass == null) {
					throw new IllegalArgumentException("Null passed for targetClass and template asked for ${className}");
				}
				buffer.append(targetClass.getPackage() + '.' + targetClass.getName());
			} else if ("simpleClassName".equals(variableName)) {
				if (targetClass == null) {
					throw new IllegalArgumentException("Null passed for targetClass and template asked for ${simpleClassName}");
				}
				buffer.append(targetClass.getName());
			} else {
				throw new IllegalArgumentException("Pattern contains unknown variable ${" + variableName + "");
			}
		}
		return buffer.toString();
	}
	private static StringBuffer upperFirst(String value) {
		StringBuffer buffer = new StringBuffer(value);
		buffer.setCharAt(0, Character.toUpperCase(buffer.charAt(0)));
		return buffer;
	}
	public static String getTypeString(String typeString, Class<?> typeClass) {
		if (!"".equals(typeString)) {
			return typeString;
		}
		if (Void.class == typeClass) {
			return "java.lang.String";
		} else {
			return typeClass.getName();
		}
	}
}
