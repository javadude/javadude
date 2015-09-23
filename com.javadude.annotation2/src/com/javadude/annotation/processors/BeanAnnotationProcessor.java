/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Scott Stanchfield - initial API and implementation
 *******************************************************************************/
package com.javadude.annotation.processors;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.javadude.annotation.Access;
import com.javadude.annotation.Bean;
import com.javadude.annotation.Default;
import com.javadude.annotation.Property;
import com.javadude.annotation.PropertyKind;
import com.javadude.annotation.processors.template.ExpressionException;
import com.javadude.annotation.processors.template.Processor;
import com.javadude.annotation.processors.template.TemplateReader;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.EnumConstantDeclaration;
import com.sun.mirror.declaration.ExecutableDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;

// does not support standard indexed properties
// does not support constrained properties

// TODO check delegation -- limit to methods exposed via property type if defined as property
// TODO delegation + extractInterface -> must allow superinterfaces to be specified for generated interface

// TODO set up generic type mapping for Delegates
// TODO check for addPropertyChangeListener et al - should treat it separately
//      -- if getPropertyChangeSupport() present, can still delegate to it

public class BeanAnnotationProcessor implements AnnotationProcessor {
	private Map<String, AnnotationValue> getAnnotationValues(AnnotationMirror a) {
		Map<String, AnnotationValue> values = new HashMap<String, AnnotationValue>();
		Map<AnnotationTypeElementDeclaration, AnnotationValue> elementValues = a.getElementValues();
		for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> entry : elementValues.entrySet()) {
			values.put(entry.getKey().getSimpleName(), entry.getValue());
		}
		return values;
	}

	private Map<String, AnnotationValue> getAnnotationValues(Declaration declaration, String annotationName) {
		Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
		for (AnnotationMirror annotationMirror : annotationMirrors) {
			if (annotationName.equals(annotationMirror.getAnnotationType().getDeclaration().getQualifiedName())) {
				return getAnnotationValues(annotationMirror);
			}
		}
		return null;
	}

	private void setValue(Thing data, Map<String, AnnotationValue> values, String name, Object def) {
		AnnotationValue annotationValue = values.get(name);
		if (annotationValue == null) {
			if (def != null) {
				data.put(name, def);
			}
		} else {
			data.put(name, annotationValue.getValue());
		}
	}
	private List<AnnotationValue> l(Map<String, AnnotationValue> values, String name) {
		AnnotationValue annotationValue = values.get(name);
		if (annotationValue == null) {
			return Collections.emptyList();
		}
		@SuppressWarnings("unchecked")
		List<AnnotationValue> value = (List<AnnotationValue>) annotationValue.getValue();
		return value;
	}
//	private String s(Map<String, AnnotationValue> values, String name, String def) {
//		AnnotationValue annotationValue = values.get(name);
//		if (annotationValue == null) {
//			return def;
//		}
//		return (String) annotationValue.getValue();
//	}
	private int i(Map<String, AnnotationValue> values, String name, int def) {
		AnnotationValue annotationValue = values.get(name);
		if (annotationValue == null) {
			return def;
		}
		return ((Integer) annotationValue.getValue()).intValue();
	}
	private boolean b(Map<String, AnnotationValue> values, String name) {
		AnnotationValue annotationValue = values.get(name);
		if (annotationValue == null) {
			return false;
		}
		return ((Boolean) annotationValue.getValue()).booleanValue();
	}
	private boolean b(AnnotationValue value) {
		if (value == null) {
			return false;
		}
		return ((Boolean) value.getValue()).booleanValue();
	}
	private static final Set<String> createSet(String... items) {
		Set<String> set = new HashSet<String>();
		for (String item : items) {
			set.add(item);
		}
		return set;
	}
//	private static final Set<String> METHODS_TO_SKIP = BeanAnnotationProcessor.createSet("equals", "hashCode", "toString", "wait", "notify", "notifyAll");
	private static final Set<String> PRIMITIVE_TYPES = BeanAnnotationProcessor.createSet("byte", "short", "int", "long", "float", "double", "char", "boolean");
	private static Processor template;
	private static final boolean nestedEclipse;
	private static final String workspace;

	static {
		workspace = System.getProperty("workspace");
		nestedEclipse = "true".equals(System.getProperty("nested.eclipse", "false"));
		if (!nestedEclipse) {
			InputStream stream = BeanAnnotationProcessor.class.getResourceAsStream("/$packageName$/$className$Gen.java");
			template = new TemplateReader().readTemplate(new InputStreamReader(stream));
		}
	}

	// TODO add parameters to METHODS_TO_SKIP and only skip those methods that match those parameters...
//	private static final Class<?>[] EMPTY_PARAMS = {};
//	private static final Object[] EMPTY_ARGS = {};
	private final AnnotationProcessorEnvironment env_;

	public BeanAnnotationProcessor(AnnotationProcessorEnvironment env) {
		env_ = env;
	}
	public void error(AnnotationMirror annotationMirror, String message) {
		env_.getMessager().printError(annotationMirror.getPosition(), message);
	}
	public void error(Declaration declaration, String message) {
		env_.getMessager().printError(declaration.getPosition(), message);
	}
	public void error(AnnotationValue value, String message) {
		env_.getMessager().printError(value.getPosition(), message);
	}
	private String commaSeparate(Collection<?> values) {
		String result = null;
		for (Object object : values) {
			result = addWithCommasBetween(result, object);
		}
		if (result == null) {
			return "";
		}
		return result;
	}
	private String addWithCommasBetween(String list, Object item) {
		if (list == null) {
			return item.toString();
		} else {
			return list + ", " + item.toString();
		}
	}
	private String normalizeList(String list) {
		if (list == null) {
			return "";
		}
		return list + ' ';
	}
	private Thing setupMethod(ExecutableDeclaration declaration, boolean forceNonAbstract) {
		String name = null;
		String returnType = null;
		boolean isAbstract = false;
		String nullBody = null;
		String throwsClause = "";
		if (declaration instanceof ConstructorDeclaration) {
			name = "<init>";
		} else {
			MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
			name = methodDeclaration.getSimpleName();
			returnType = methodDeclaration.getReturnType().toString();
			if (!forceNonAbstract) {
				isAbstract = methodDeclaration.getModifiers().contains(Modifier.ABSTRACT);
			}
			if ("void".equals(returnType)) {
				nullBody = "// null object implementation; do nothing";
			} else if ("boolean".equals(returnType)) {
				nullBody = "false; // null object implementation";
			} else if ("char".equals(returnType)) {
				nullBody = "' '; // null object implementation";
			} else if (BeanAnnotationProcessor.PRIMITIVE_TYPES.contains(returnType)) {
				nullBody = "0; // null object implementation";
			} else {
				nullBody = "null; // null object implementation";
			}
		}
		Collection<ReferenceType> thrownTypes = declaration.getThrownTypes();
		if (!thrownTypes.isEmpty()) {
			throwsClause = "throws " + commaSeparate(thrownTypes);
		}
		String genericDecls = null;
		String argDecls = null;
		String args = null;
		String modifiers = "";
		Collection<ParameterDeclaration> parameters = declaration.getParameters();
		for (ParameterDeclaration parameterDeclaration : parameters) {
			argDecls = addWithCommasBetween(argDecls, parameterDeclaration.getType() + " " + parameterDeclaration.getSimpleName());
			args = addWithCommasBetween(args, parameterDeclaration.getSimpleName());
		}
		Collection<TypeParameterDeclaration> formalTypeParameters = declaration.getFormalTypeParameters();
		for (TypeParameterDeclaration typeParameterDeclaration : formalTypeParameters) {
			genericDecls = addWithCommasBetween(genericDecls, typeParameterDeclaration);
		}
		Collection<Modifier> modifiers2 = declaration.getModifiers();
		for (Modifier modifier : modifiers2) {
			if (!"abstract".equals(modifier.toString())) {
				modifiers += modifier.toString() + ' ';
			}
		}
		if (genericDecls == null) {
			genericDecls = "";
		} else {
			genericDecls = '<' + genericDecls + "> ";
		}
		Thing method = new Thing("method");
		method.put("name", name);
		method.put("returnType", returnType);
		method.put("abstract", isAbstract);
		method.put("nullBody", nullBody);
		method.put("modifiers", modifiers); // do not normalize; already "" or has ' ' at end
		method.put("argDecls", normalizeList(argDecls));
		method.put("args", normalizeList(args));
		method.put("genericDecls", genericDecls);
		method.put("throwsClause", throwsClause);
		return method;
	}
	public void process() {
		final AnnotationTypeDeclaration beanAnn = (AnnotationTypeDeclaration) env_.getTypeDeclaration(Bean.class.getName());
		for (Declaration declaration : env_.getDeclarationsAnnotatedWith(beanAnn)) {
			try {
				if (!(declaration instanceof ClassDeclaration)) {
					error(declaration, "You can only annotate class declarations with @Bean");
					return;
				}

				Thing data = new Thing("data");

				ClassDeclaration classDeclaration = (ClassDeclaration) declaration;
				PackageDeclaration packageDeclaration = classDeclaration.getPackage();

				Map<String, AnnotationValue> beanValues = getAnnotationValues(classDeclaration, "com.javadude.annotation.Bean");

				checkExtends(classDeclaration, packageDeclaration);		// check that the class extends XXXGen
				processSuperclass(data, beanValues);					// process the superclass attribute
				processProperties(data, beanValues);
				processObservers(data, beanValues);
				processNullObjects(data, beanValues);
				processDelegates(data, beanValues);
				processDefaultMethods(data, classDeclaration);

				setValue(data, beanValues, "cloneable", false);
				setValue(data, beanValues, "defineEqualsAndHashCode", false);
				setValue(data, beanValues, "equalsAndHashCodeCallSuper", false);
				setValue(data, beanValues, "definePropertyNameConstants", false);
				setValue(data, beanValues, "defineCreatePropertyMap", false);
				data.put("date", new Date());
				data.put("year", Calendar.getInstance().get(Calendar.YEAR));
				data.put("className", classDeclaration.getSimpleName());
				data.put("packageName", packageDeclaration.getQualifiedName());

				data.checkAllValuesSet(declaration, this);

				Filer f = env_.getFiler();
				PrintWriter pw = f.createSourceFile(classDeclaration.getQualifiedName() + "Gen");

				if (nestedEclipse) {
					// debugging in eclipse -- reread the template each time
					FileReader fileReader = new FileReader(workspace + "/com.javadude.annotation/template/$packageName$/$className$Gen.java");
					template = new TemplateReader().readTemplate(fileReader);
				}
				int spacesForLeadingTabs = i(beanValues, "spacesForLeadingTabs", -1);
				String padding = null;
				if (spacesForLeadingTabs != -1) {
					padding = "";
					for (int i = 0; i < spacesForLeadingTabs; i++) {
						padding += ' ';
					}
				}
				template.process(new Symbols(data), pw, -1, padding);
				pw.close();
			} catch (ThreadDeath e) {
				throw e;
			} catch (ExpressionException e) {
				error(declaration, "@Bean generator error: " + e.getMessage());
			} catch (Throwable t) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				t.printStackTrace(printWriter);
				printWriter.close();
				error(declaration, "Unexpected exception: " + stringWriter.toString());
			}
		}
	}
	private void processSuperclass(Thing data, Map<String, AnnotationValue> beanValues) {
		AnnotationValue value = beanValues.get("superclass");
		data.put("superclass", null);
		data.put("genericDecls", "");
		data.put("classModifiers", "");
		data.put("propertyNameConstantsInherited", false);
		data.put("getPropertyChangeSupportInherited", false);
		data.put("getPropertyChangeSupportModifiers", "protected");
		data.put("paramStringInherited", false);
		data.put("paramStringModifiers", "protected");
		data.put("createPropertyMapInherited", false);
		data.put("createPropertyMapModifiers", "public");
		data.put("atLeastOneDouble", false);
		data.put("atLeastOneBound", false);
		data.put("atLeastOneObject", false);
		data.put("atLeastOneDefault", false);

		if (value == null) {
			data.setEmpty("superclassConstructors");
		} else {
			if (!(value.getValue() instanceof ClassDeclaration)) {
				error(value, "superclass must be a class");
				return;
			}
			ClassDeclaration superclass = (ClassDeclaration) value.getValue();
			data.put("superclass", superclass.getQualifiedName());
			boolean hasProperties = !l(beanValues, "properties").isEmpty();
			// check if getPropertyChangeSupport or some superclass defines bound properties in @Bean
			checkInheritedMethod(data, "getPropertyChangeSupport", "java.beans.PropertyChangeSupport", superclass, true,
					new InheritCheck() {
						public boolean isInherited(Thing d, ClassDeclaration classDeclaration) {
							Bean beanAnn = classDeclaration.getAnnotation(Bean.class);
							if (beanAnn != null) {
								Property[] properties = beanAnn.properties();
								for (Property property : properties) {
									if (property.bound()) {
										d.put("getPropertyChangeSupportInherited", true);
										d.put("getPropertyChangeSupportModifiers", "protected");
										return true;
									}
								}
							}
							return false;
						} });
			// check if paramString inherited or some superclass has @Bean
			checkInheritedMethod(data, "paramString", "java.lang.String", superclass, !hasProperties,
					new InheritCheck() {
						public boolean isInherited(Thing d, ClassDeclaration classDeclaration) {
							if (classDeclaration.getAnnotation(Bean.class) != null) {
								d.put("paramStringInherited", true);
								d.put("paramStringModifiers", "protected");
								return true;
							}
							return false;
						} });
			// check if createPropertyMap inherited or some superclass has @Bean with defineCreatePropertyMap
			checkInheritedMethod(data, "createPropertyMap", "java.lang.String", superclass, !hasProperties,
					new InheritCheck() {
						public boolean isInherited(Thing d, ClassDeclaration classDeclaration) {
							Bean beanAnn = classDeclaration.getAnnotation(Bean.class);
							if (beanAnn != null && beanAnn.defineCreatePropertyMap()) {
								d.put("createPropertyMapInherited", true);
								d.put("createPropertyMapModifiers", "public");
								return true;
							}
							return false;
						} });

			String genericDecls = null;
			Collection<TypeParameterDeclaration> formalTypeParameters2 = superclass.getFormalTypeParameters();
			for (TypeParameterDeclaration typeParameterDeclaration : formalTypeParameters2) {
				genericDecls = addWithCommasBetween(genericDecls, typeParameterDeclaration);
			}
			if (genericDecls == null) {
				genericDecls = "";
			} else {
				genericDecls = '<' + genericDecls + '>';
			}
			data.put("genericDecls", genericDecls);

			String classModifiers = "";
			for (Modifier modifier : superclass.getModifiers()) {
				if (!"abstract".equals(modifier.toString())) {
					classModifiers += modifier.toString() + ' ';
				}
			}
			data.put("classModifiers", classModifiers);

			Collection<ConstructorDeclaration> constructors = superclass.getConstructors();
			if (constructors.isEmpty()) {
				data.setEmpty("superclassConstructors");
			} else {
				for (ConstructorDeclaration constructorDeclaration : constructors) {
					data.add("superclassConstructors", setupMethod(constructorDeclaration, false));
				}
			}

			boolean extendPropertyNameConstants = false;
			if (b(beanValues, "definePropertyNameConstants")) {
				// if superclass has a PropertyNameConstants interface or a Bean annotation with
				//     definePropertyNameConstants=true, we need to have our PropertyNameConstants
				//     extend it
				Collection<TypeDeclaration> nestedTypes = superclass.getNestedTypes();
				for (TypeDeclaration typeDeclaration : nestedTypes) {
					if ("PropertyNames".equals(typeDeclaration.getSimpleName()) && (typeDeclaration instanceof InterfaceDeclaration)) {
						extendPropertyNameConstants = true;
					}
				}
				if (!extendPropertyNameConstants) {
					// check if the superclass is annotated with Bean
					Bean annotation = superclass.getAnnotation(Bean.class);
					if (annotation != null) {
						extendPropertyNameConstants = annotation.definePropertyNameConstants();
					}
				}
			}
			data.put("propertyNameConstantsInherited", extendPropertyNameConstants);
		}
	}

//	private AnnotationValue get(Map<String, AnnotationValue> values, String name, AnnotationValue def) {
//		AnnotationValue value = values.get(name);
//		if (value != null)
//			return value;
//		return def;
//	}
	private void processProperties(Thing data, Map<String, AnnotationValue> beanValues) {
		boolean atLeastOneBound = false;
		boolean atLeastOneDouble = false;
		boolean atLeastOneObject = false;
		String firstPropertyName = null;
		AnnotationValue propertiesValue = beanValues.get("properties");
		if (propertiesValue == null) {
			data.setEmpty("properties");
		} else {
			Set<String> propertyNames = new HashSet<String>();
			AnnotationValue defaultType = null;
			AnnotationValue defaultTypeString = null;
			AnnotationValue defaultKeyType = null;
			AnnotationValue defaultKeyTypeString = null;
			AnnotationValue defaultReader = null;
			AnnotationValue defaultWriter = null;
			AnnotationValue defaultBound = null;
			AnnotationValue defaultKind = null;
			AnnotationValue defaultOmitFromToString = null;
			AnnotationValue defaultNotNull = null;
			AnnotationValue defaultIsStatic = null;
			AnnotationValue defaultIsSynchronized = null;

    		@SuppressWarnings("unchecked")
    		List<AnnotationValue> properties = (List<AnnotationValue>) propertiesValue.getValue();
		    for (AnnotationValue annotationValue : properties) {
				AnnotationMirror propertyMirror = (AnnotationMirror) annotationValue.getValue();
				Map<String, AnnotationValue> propertyValues = getAnnotationValues(propertyMirror);
				AnnotationValue name = propertyValues.get("name");
				AnnotationValue plural = propertyValues.get("plural");
				AnnotationValue type = propertyValues.get("type");
				AnnotationValue typeString = propertyValues.get("typeString");
				AnnotationValue keyType = propertyValues.get("keyType");
				AnnotationValue keyTypeString = propertyValues.get("keyTypeString");
				AnnotationValue reader = propertyValues.get("reader");
				AnnotationValue writer = propertyValues.get("writer");
				AnnotationValue bound = propertyValues.get("bound");
				AnnotationValue kind = propertyValues.get("kind");
				AnnotationValue omitFromToString = propertyValues.get("omitFromToString");
				AnnotationValue notNull = propertyValues.get("notNull");
				AnnotationValue isStatic = propertyValues.get("isStatic");
				AnnotationValue isSynchronized = propertyValues.get("isSynchronized");

				if (Property.DEFAULTS.equals(name.getValue())) {
					defaultType = type;
					defaultTypeString = typeString;
					defaultKeyType = keyType;
					defaultKeyTypeString = keyTypeString;
					defaultReader = reader;
					defaultWriter = writer;
					defaultBound = bound;
					defaultKind = kind;
					defaultOmitFromToString = omitFromToString;
					defaultNotNull = notNull;
					defaultIsStatic = isStatic;
					defaultIsSynchronized = isSynchronized;
					continue;
				}

				// plugin the default values
				if (type == null && typeString == null) {
					type = defaultType;
					typeString = defaultTypeString;
				}
				if (keyType == null && keyTypeString == null) {
					keyType = defaultKeyType;
					keyTypeString = defaultKeyTypeString;
				}
				if (reader == null) {
					reader = defaultReader;
				}
				if (writer == null) {
					writer = defaultWriter;
				}
				if (bound == null) {
					bound = defaultBound;
				}
				if (kind == null) {
					kind = defaultKind;
				}
				if (omitFromToString == null) {
					omitFromToString = defaultOmitFromToString;
				}
				if (notNull == null) {
					notNull = defaultNotNull;
				}
				if (isStatic == null) {
					isStatic = defaultIsStatic;
				}
				if (isSynchronized == null) {
					isSynchronized = defaultIsSynchronized;
				}

				Thing property = new Thing("property");
				property.put("name", name.getValue());

				if (typeString == null) {
					if (type == null) {
						property.put("type", "java.lang.String");
					} else {
						if (type.getValue() instanceof TypeDeclaration) {
							property.put("type", ((TypeDeclaration) type.getValue()).getQualifiedName());
						} else {
							property.put("type", ((PrimitiveType) type.getValue()).toString());
						}
					}
				} else {
					if (type != null) {
						String message = "@Property cannot have both type and typeString attributes specified";
						error(typeString, message);
						error(type, message);
						property.put("type", "<ERROR>");
					} else {
						property.put("type", typeString.getValue());
					}
				}

				PropertyKind propertyKind = PropertyKind.SIMPLE;
				if (kind != null) {
					propertyKind = PropertyKind.valueOf(kind.getValue().toString());
				}

				// check for duplicate keytype specifications
				if (propertyKind.isMap()) {
					if (keyTypeString == null) {
						if (keyType == null) {
							property.put("keyType", "java.lang.String");
						} else {
							property.put("keyType", ((TypeDeclaration) keyType.getValue()).getQualifiedName());
						}
					} else
						if (keyType != null) {
							String message = "@Property cannot have both keyType and keyTypeString attributes specified";
							error(keyType, message);
							error(keyTypeString, message);
							property.put("keyType", "<ERROR>");
						}
				} else {
					if (keyTypeString != null) {
						error(keyTypeString, "@Property can only have a keyTypeString attribute if kind is MAP or UNMODIFIABLE_MAP");
					}
					if (keyType != null) {
						error(keyType, "@Property can only have a keyType attribute if kind is MAP or UNMODIFIABLE_MAP");
					}
					property.put("keyType", "<ERROR>");
				}

				// check for plural names
				if (propertyKind.isSimple()) {
					if (plural != null) {
						error(plural, "@Property cannot have plural specified if kind is SIMPLE");
					}
					property.put("pluralName", null);
				} else {
					if (plural == null) {
						property.put("pluralName", name.getValue() + "s");
					} else {
						property.put("pluralName", plural.getValue());
					}
				}

				property.put("simple", propertyKind.isSimple());
				property.put("list", propertyKind.isList());
				property.put("map", propertyKind.isMap());
				property.put("set", propertyKind.isSet());
				String typeName = (String) property.get("type");

				property.put("float", "float".equals(typeName));
				property.put("double", "double".equals(typeName));
				property.put("boolean", "boolean".equals(typeName));
				property.put("char", "char".equals(typeName));
				property.put("byte", "byte".equals(typeName));
				property.put("long", "long".equals(typeName));
				property.put("int", "int".equals(typeName));
				property.put("short", "short".equals(typeName));

				if (property.containsKey("firstPropertyName")) {
					property.put("firstPropertyName", name.getValue());
				}
				if (propertyNames.contains(name.getValue())) {
					error(name, "Duplicate property name '" + name + "' specified for @Bean properties definition");
				} else {
					propertyNames.add((String) name.getValue());
				}

				if (bound != null) {
					if (isStatic!= null) {
						error(bound, "Static properties cannot be declared bound");
						error(isStatic, "Static properties cannot be declared bound");
					} else {
						atLeastOneBound = true;
					}
				}

				if ("double".equals(property.get("type"))) {
					data.put("atLeastOneDouble", true);
				}
				property.put("kind", propertyKind);
				property.put("omitFromToString", b(omitFromToString));
				data.add("properties", property);

				// evil hack to get the type, which is a Class

				boolean isPrimitive = BeanAnnotationProcessor.PRIMITIVE_TYPES.contains(property.get("type"));
				property.put("primitive", isPrimitive);

				if (!isPrimitive) {
					data.put("atLeastOneObject", true);
				}

				property.put("bound", b(bound));
				if (writer == null) {
					property.put("writerAccess", Access.PUBLIC.getModifier());
					property.put("writeable", true);
				} else {
					EnumConstantDeclaration writerValue = (EnumConstantDeclaration) writer.getValue();
					Access writerAccess = Access.valueOf(writerValue.toString());
					property.put("writerAccess", writerAccess.getModifier());
					property.put("writeable", writerAccess != Access.NONE);
				}
				if (reader == null) {
					property.put("readerAccess", Access.PUBLIC.getModifier());
					property.put("readable", true);
				} else {
					EnumConstantDeclaration readerValue = (EnumConstantDeclaration) reader.getValue();
					Access readerAccess = Access.valueOf(readerValue.toString());
					property.put("readerAccess", readerAccess.getModifier());
					property.put("readable", readerAccess != Access.NONE);
				}

				boolean bNotNull = b(notNull);
				boolean bIsStatic = b(isStatic);
				boolean bIsSynchronized = b(isSynchronized);
				property.put("notNull", bNotNull);

				if (bNotNull && isPrimitive) {
					error(notNull, "Cannot specify notNull for primitive-typed property " + name.getValue() + " in @Property");
				}
				String extraFieldKeywords = "";
				String extraMethodKeywords = "";
				if (bIsStatic) {
					extraFieldKeywords = "static ";
					extraMethodKeywords = "static ";
				}
				if (bIsSynchronized) {
					if (bIsStatic) {
						extraMethodKeywords += "synchronized ";
					} else {
						extraMethodKeywords = "synchronized ";
					}
				}
				property.put("extraFieldKeywords", extraFieldKeywords);
				property.put("extraMethodKeywords", extraMethodKeywords);
				property.checkAllValuesSet(propertiesValue, this);
			}
		}
		data.put("definePropertyChangeSupport", !((Boolean) data.get("getPropertyChangeSupportInherited")) && atLeastOneBound);
		data.put("atLeastOneDouble", atLeastOneDouble);
		data.put("atLeastOneObject", atLeastOneObject);
		data.put("firstPropertyName", firstPropertyName);
	}


	private void processDelegates(Thing data, Map<String, AnnotationValue> beanValues) {
		AnnotationValue delegatesValue = beanValues.get("delegates");
		if (delegatesValue == null) {
			data.setEmpty("delegates");
		} else {
//		if (bean.delegates() != null) {
//		for (Delegate delegate : bean.delegates()) {
//			if (delegate == null) {
//				continue;
//			}
//			try {
//				DelegateSpec delegateSpec = new DelegateSpec();
//				delegateSpec.set("accessor", delegate.accessor());
//				data.addDelegate(delegateSpec);
//				String type = selectType(declaration, "@Delegate", delegate, "type", "typeString", null, true);
//				if (type == null) {
//					return;
//				}
//				delegateSpec.set("name", type);
//				defineListenerOrDelegate(false, delegateSpec, packageDeclaration, "delegate type", "delegates", packageName);
//
//				// TODO if property doesn't exist, define it
//			} catch (NoSuchElementException e) {
//				error(declaration, "Invalid delegate specification; must be type,varName,type,varName...");
//			}
//		}
	}
	}

	private void processNullObjects(Thing data, Map<String, AnnotationValue> beanValues) {
		AnnotationValue nullObjectsValue = beanValues.get("nullObjects");
		if (nullObjectsValue == null) {
			data.setEmpty("nullObjects");
		} else {
//		if (bean.nullObjects().length > 0 && !"".equals(bean.nullObjects()[0])) {
//		for (NullObject nullObject : bean.nullObjects()) {
//			if (nullObject == null) {
//				continue;
//			}
//			Type listener = new Type();
//			String type = selectType(declaration, "@NullObject", nullObject, "type", "typeString", null, true);
//			if (type == null) {
//				return;
//			}
//			listener.set("name", type);
//			data.addNullObject(listener);
//			defineListenerOrDelegate(true, listener, classDeclaration, "null implementation class/interface", "nullImplementationName", packageName);
//		}
	}
	}

	private void processObservers(Thing data, Map<String, AnnotationValue> beanValues) {
		AnnotationValue observersValue = beanValues.get("observers");
		if (observersValue == null) {
			data.setEmpty("observers");
		} else {
//		for (Observer observer : bean.observers()) {
//			if (observer == null) {
//				continue;
//			}
//			Type type = new Type();
//			data.addObserver(type);
//			String typeName = selectType(declaration, "@Observer", observer, "type", "typeString", null, true);
//			if (typeName == null) {
//				return;
//			}
//			type.set("name", typeName);
//			defineListenerOrDelegate(false, type, packageDeclaration, "listener interface", "eventsets", packageName);
//		}
		}
	}


	private void processDefaultMethods(Thing data, ClassDeclaration classDeclaration) {
		// find any methods that have default parameters
		boolean error = false;
		for (ConstructorDeclaration constructorDeclaration : classDeclaration.getConstructors()) {
			Collection<ParameterDeclaration> parameters = constructorDeclaration.getParameters();
			for (ParameterDeclaration parameterDeclaration : parameters) {
				Default annotation = parameterDeclaration.getAnnotation(Default.class);
				if (annotation != null) {
					error(parameterDeclaration, "@Default is not legal in constructor parameters");
					error = true;
				}
			}
		}
		if (error) {
			return;
		}

		boolean atLeastOneDefault = false;
		methods: for (MethodDeclaration methodDeclaration : classDeclaration.getMethods()) {
			Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
			boolean seenDefault = false;
			String[] names    = new String[parameters.size()];
			String[] types    = new String[parameters.size()];
			String[] defaults = new String[parameters.size()];
			int n = 0;
			for (ParameterDeclaration parameterDeclaration : parameters) {
				Default annotation = parameterDeclaration.getAnnotation(Default.class);
				names[n] = parameterDeclaration.getSimpleName();
				types[n] = parameterDeclaration.getType().toString();
				if (annotation != null) {
					seenDefault = true;
					if ("java.lang.String".equals(types[n])) {
						defaults[n] = '"' + annotation.value() + '"';
					} else {
						defaults[n] = annotation.value();
					}
				} else if (seenDefault) {
					error(parameterDeclaration, "All parameters after a parameter annotated with @Default must be annotated with @Default");
					continue methods;
				}
				n++;
			}

			if (seenDefault) {
				atLeastOneDefault = true;
				if (methodDeclaration.getModifiers().contains(Modifier.PRIVATE)) {
					error(methodDeclaration, "Private methods cannot use @Default parameters");
				}
				if (methodDeclaration.getModifiers().contains(Modifier.STATIC)) {
					error(methodDeclaration, "Static methods cannot use @Default parameters");
				}
				String modifiers3 = "";
				if (methodDeclaration.getModifiers().contains(Modifier.PUBLIC)) {
					modifiers3 = "public ";
				} else if (methodDeclaration.getModifiers().contains(Modifier.PROTECTED)) {
					modifiers3 = "protected ";
				}
				String throwsClause = getThrowsClause(methodDeclaration);
				String returnType = methodDeclaration.getReturnType().toString();
				String methodName = methodDeclaration.getSimpleName();
				String argDecl = "";
				String callArgs = "";
				for (int i = 0; i < n; i++) {
					if (defaults[i] != null) {
						String callArgsWithDefaults = callArgs;
						for (int j = i; j < n; j++) {
							if (j > 0) {
								callArgsWithDefaults += ", ";
							}
							callArgsWithDefaults += defaults[j];
						}
						Thing method = new Thing("method");
						method.put("name", methodName);
						method.put("returnType", returnType);
						method.put("throwsClause", throwsClause);
						method.put("argDecls", argDecl);
						method.put("modifiers", modifiers3);
						method.put("args", callArgsWithDefaults);
						data.add("defaultMethods", method);
					}
					if (i > 0) {
						argDecl += ", ";
						callArgs += ", ";
					}
					argDecl += types[i] + ' ' + names[i];
					callArgs += names[i];
				}
				Thing method = new Thing("method");
				method.put("name", methodName);
				method.put("returnType", returnType);
				method.put("throwsClause", throwsClause);
				method.put("modifiers", modifiers3);
				method.put("abstract", true);
				method.put("argDecls", argDecl);
				data.add("defaultMethods", method);

			}
		}
		data.put("atLeastOneDefault", atLeastOneDefault);
		if (!atLeastOneDefault) {
			data.setEmpty("defaultMethods");
		}
	}

	private void checkExtends(ClassDeclaration classDeclaration, PackageDeclaration packageDeclaration) {
		String scName = classDeclaration.getSuperclass().toString();
		int lt = scName.indexOf('<');
		if (lt != -1) {
			scName = scName.substring(0, lt);
		}
		int dot = scName.lastIndexOf('.');
		if (dot != -1) {
			String superClassPackageName = scName.substring(0, dot);
			if (!superClassPackageName.equals(packageDeclaration.getQualifiedName())) {
				scName = ""; // force error below
			} else {
				scName = scName.substring(dot + 1);
			}
		}
		if (!scName.equals(classDeclaration.getSimpleName() + "Gen")) {
			error(classDeclaration, classDeclaration.getQualifiedName() + " must extend " +
					classDeclaration.getQualifiedName() + "Gen for @Bean to work properly");
		}
	}

	private interface InheritCheck {
		boolean isInherited(Thing data, ClassDeclaration classDeclaration);
	}
	private boolean checkInheritedMethod(Thing data, String methodName, String returnType, ClassDeclaration superclass, boolean finalOk, InheritCheck inheritCheck) {
		if (inheritCheck != null) {
			if (inheritCheck.isInherited(data, superclass)) {
				return true;
			}
		}
		for (MethodDeclaration methodDeclaration : superclass.getMethods()) {
			if (methodName.equals(methodDeclaration.getSimpleName()) &&
					methodDeclaration.getParameters().isEmpty() &&
					returnType.equals(methodDeclaration.getReturnType().toString())) {
				data.put(methodName + "Inherited", true);
				Collection<Modifier> modifiers = methodDeclaration.getModifiers();
				if ((modifiers.contains(Modifier.FINAL) || modifiers.contains(Modifier.PRIVATE)) && !finalOk) {
					// TBD TBD TBD - ERROR!!! cannot extend class with superclass method like this - how to report?
				} else if (modifiers.contains(Modifier.PROTECTED)) {
					data.put(methodName + "Modifiers", "protected");
				} else if (modifiers.contains(Modifier.PUBLIC)) {
					data.put(methodName + "Modifiers", "public");
				} else {
					data.put(methodName + "Modifiers", "");
				}
				return true;
			}
		}
		if (superclass.getSuperclass() != null) {
			if (superclass.getSuperclass().getDeclaration() != null) {
				if (checkInheritedMethod(data, methodName, returnType, superclass.getSuperclass().getDeclaration(), finalOk, inheritCheck)) {
					return true;
				}
			}
		}
		return false;
	}
//	private TypeDeclaration getType(Declaration declaration, String name, String packageName, String notFoundMessage) {
//		TypeDeclaration typeDeclaration = env_.getTypeDeclaration(name);
//		if (typeDeclaration != null) {
//			return typeDeclaration;
//		}
//
//		// try it with the package name prepended
//		typeDeclaration = env_.getTypeDeclaration(packageName + '.' + name);
//		if (typeDeclaration != null) {
//			return typeDeclaration;
//		}
//
//		error(declaration, notFoundMessage);
//		return null;
//	}

//	private void defineListenerOrDelegate(boolean abstractOnly, Thing type, Declaration declaration, String typeOfThing, String partOfBean, String packageName) {
//		TypeDeclaration typeDeclaration = getType(declaration, (String) type.get("name"), packageName, "Cannot find " + typeOfThing + " " + type.get("name") + " defined as an " + partOfBean + " in @Bean (you probably need to fully-qualify it)");
//		if (typeDeclaration == null) {
//			return;
//		}
//		Collection<? extends MethodDeclaration> methods = typeDeclaration.getMethods();
//		for (MethodDeclaration methodDeclaration : methods) {
//			if (methodDeclaration.getModifiers().contains(Modifier.STATIC)) {
//				continue;
//			}
//			if (abstractOnly) {
//				if (!methodDeclaration.getModifiers().contains(Modifier.ABSTRACT)) {
//					continue;
//				}
//			} else {
//				if (!methodDeclaration.getModifiers().contains(Modifier.PUBLIC)) {
//					continue;
//				}
//			}
//			if (BeanAnnotationProcessor.METHODS_TO_SKIP.contains(methodDeclaration.getSimpleName())) {
//				continue;
//			}
//
//			type.add("methods", setupMethod(methodDeclaration, true));
//		}
//	}
//
	private String getThrowsClause(MethodDeclaration methodDeclaration) {
		Collection<ReferenceType> thrownTypes = methodDeclaration.getThrownTypes();
		boolean first = true;
		if (!thrownTypes.isEmpty()) {
			String throwsClause = " throws ";
			for (ReferenceType thrownType : thrownTypes) {
				if (first) {
					first = false;
				} else {
					throwsClause += ", ";
				}
				throwsClause += Utils.getTypeName(thrownType);
			}
			return throwsClause;
		} else {
			return "";
		}
	}
}
