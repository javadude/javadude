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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import com.javadude.annotation.Access;
import com.javadude.annotation.Bean;
import com.javadude.annotation.Default;
import com.javadude.annotation.Delegate;
import com.javadude.annotation.NullObject;
import com.javadude.annotation.Observer;
import com.javadude.annotation.Property;
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
import com.sun.mirror.declaration.ExecutableDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.MirroredTypeException;
import com.sun.mirror.type.ReferenceType;

// does not support standard indexed properties
// does not support constrained properties

// TODO check delegation -- limit to methods exposed via property type if defined as property
// TODO delegation + extractInterface -> must allow superinterfaces to be specified for generated interface

// TODO set up generic type mapping for Delegates
// TODO check for addPropertyChangeListener et al - should treat it separately
//      -- if getPropertyChangeSupport() present, can still delegate to it

// TODO override paramString if defined in superclass

public class BeanAnnotationProcessor implements AnnotationProcessor {
	private static final Set<String> createSet(String... items) {
		Set<String> set = new HashSet<String>();
		for (String item : items) {
			set.add(item);
		}
		return set;
	}
	private static final Set<String> METHODS_TO_SKIP = BeanAnnotationProcessor.createSet("equals", "hashCode", "toString", "wait", "notify", "notifyAll");
	private static final Set<String> PRIMITIVE_TYPES = BeanAnnotationProcessor.createSet("byte", "short", "int", "long", "float", "double", "char", "boolean");
	private static Processor template;
	private static final boolean nestedEclipse;

	static {
		nestedEclipse = "true".equals(System.getProperty("nested.eclipse", "false"));
		if (!nestedEclipse) {
			try {
				FileReader fileReader = new FileReader("/eclipse34/javadude-workspace/com.javadude.annotation/src/$packageName$/$className$Gen.java");
				template = new TemplateReader().readTemplate(fileReader);
			} catch (FileNotFoundException e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}

	// TODO add parameters to METHODS_TO_SKIP and only skip those methods that match those parameters...
	private static final Class<?>[] EMPTY_PARAMS = {};
	private static final Object[] EMPTY_ARGS = {};
	private final AnnotationProcessorEnvironment env_;

	public BeanAnnotationProcessor(AnnotationProcessorEnvironment env) {
		env_ = env;
	}
	private String selectType(Declaration declaration, String spec, Object o, String classAttribute, String stringAttribute, String defaultValue, boolean required) {
		java.lang.reflect.Method classMethod;
		java.lang.reflect.Method stringMethod;
		try {
			classMethod = o.getClass().getMethod(classAttribute, BeanAnnotationProcessor.EMPTY_PARAMS);
			stringMethod = o.getClass().getMethod(stringAttribute, BeanAnnotationProcessor.EMPTY_PARAMS);
			String classValue = null;
			try {
				classMethod.invoke(o, BeanAnnotationProcessor.EMPTY_ARGS);
			} catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof MirroredTypeException) {
					classValue = ((MirroredTypeException) e.getTargetException()).getQualifiedName();
				} else {
					throw e;
				}
			}

			if ("java.lang.Void".equals(classValue)) {
				classValue = null;
			}

			String stringValue = (String) stringMethod.invoke(o, BeanAnnotationProcessor.EMPTY_ARGS);

			if ("java.lang.Void".equals(stringValue) || "".equals(stringValue)) {
				stringValue = null;
			}

			if (classValue == null && stringValue != null) {
				return stringValue;
			} else if (stringValue == null && classValue != null) {
				return classValue;
			}

			if (defaultValue != null) {
				return defaultValue;
			} else {
				if (required) {
					env_.getMessager().printError(declaration.getPosition(),
							"You must specify " + classAttribute + " or " + stringAttribute + " for " + spec);
				}
				return null;
			}
		} catch (Exception e1) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e1.printStackTrace(pw);
			pw.close();
			String message = "Error processing " + spec + ": ";
			env_.getMessager().printError(declaration.getPosition(), message + sw);
			return null;
		}
	}

	private void error(Declaration declaration, String message) {
		env_.getMessager().printError(declaration.getPosition(), message);
	}
	private void error(AnnotationValue value, String message) {
		env_.getMessager().printError(value.getPosition(), message);
	}
	private String commaSeparate(Collection<?> values) {
		String result = null;
		for (Object object : values) {
			result = addWithCommasBetween(result, object);
		}
		if (result == null)
			return "";
		return result;
	}
	private String addWithCommasBetween(String list, Object item) {
		if (list == null)
			return item.toString();
		else
			return list + ", " + item.toString();
	}
	private String normalizeList(String list) {
		if (list == null)
			return "";
		return list + ' ';
	}
	private Method setupMethod(ExecutableDeclaration declaration, boolean forceNonAbstract) {
		Method method = new Method();
		if (declaration instanceof ConstructorDeclaration) {
			method.setName("<init>");
			method.setReturnType(null);
			method.setAbstract(false);
			method.setNullBody(null);
		} else {
			MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
			method.setName(methodDeclaration.getSimpleName());
			method.setReturnType(methodDeclaration.getReturnType().toString());
			if (forceNonAbstract)
				method.setAbstract(false);
			else
				method.setAbstract(methodDeclaration.getModifiers().contains(Modifier.ABSTRACT));
			if ("void".equals(method.getReturnType())) {
				method.setNullBody("// null object implementation; do nothing");
			} else if ("boolean".equals(method.getReturnType())) {
				method.setNullBody("false; // null object implementation");
			} else if ("char".equals(method.getReturnType())) {
				method.setNullBody("' '; // null object implementation");
			} else if (BeanAnnotationProcessor.PRIMITIVE_TYPES.contains(method.getReturnType())) {
				method.setNullBody("0; // null object implementation");
			} else {
				method.setNullBody("null; // null object implementation");
			}
		}
		Collection<ReferenceType> thrownTypes = declaration.getThrownTypes();
		if (thrownTypes.isEmpty())
			method.setThrowsClause("");
		else
			method.setThrowsClause("throws " + commaSeparate(thrownTypes));
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
			if (!"abstract".equals(modifier.toString()))
				modifiers += modifier.toString() + ' ';
		}
		if (genericDecls == null)
			genericDecls = "";
		else
			genericDecls = '<' + genericDecls + "> ";
		method.setModifiers(modifiers); // do not normalize; already "" or has ' ' at end
		method.setArgDecls(normalizeList(argDecls));
		method.setArgs(normalizeList(args));
		method.setGenericDecls(genericDecls);
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

				// check that class is defined to extend XXXGen
				ClassDeclaration classDeclaration = (ClassDeclaration) declaration;
				PackageDeclaration packageDeclaration = classDeclaration.getPackage();

				Data data = new Data();
				data.setGenericDecls("");
				boolean superclassHasPropertyChangeSupport = false;

				String classModifiers = "";
				Collection<AnnotationMirror> annotationMirrors = classDeclaration.getAnnotationMirrors();
				for (AnnotationMirror annotationMirror : annotationMirrors) {
					Map<AnnotationTypeElementDeclaration, AnnotationValue> elementValues = annotationMirror.getElementValues();
					for (Entry<AnnotationTypeElementDeclaration, AnnotationValue> elementValue : elementValues.entrySet()) {
						AnnotationValue value = elementValue.getValue();
						AnnotationTypeElementDeclaration key = elementValue.getKey();
						if ("superclass".equals(key.getSimpleName())) {
							if (!(value.getValue() instanceof ClassDeclaration)) {
								error(value, "superclass must be a class");
								return;
							}
							ClassDeclaration superclass = (ClassDeclaration) value.getValue();
							data.setSuperclass(superclass.getQualifiedName());
							superclassHasPropertyChangeSupport = checkPropertyChangeSupport(superclass);

							Collection<TypeParameterDeclaration> formalTypeParameters2 = superclass.getFormalTypeParameters();
							String classGenericDecls = null;
							for (TypeParameterDeclaration typeParameterDeclaration : formalTypeParameters2) {
								classGenericDecls = addWithCommasBetween(classGenericDecls, typeParameterDeclaration);
							}
							if (classGenericDecls == null)
								classGenericDecls = "";
							else
								classGenericDecls = '<' + classGenericDecls + '>';
							data.setGenericDecls(classGenericDecls);
							for (Modifier modifier : superclass.getModifiers()) {
								if (!"abstract".equals(modifier.toString()))
									classModifiers += modifier.toString() + ' ';
							}
							Collection<ConstructorDeclaration> constructors = superclass.getConstructors();
							for (ConstructorDeclaration constructorDeclaration : constructors) {
								data.addSuperclassConstructor(setupMethod(constructorDeclaration, false));
							}

							// if superclass has a PropertyNameConstants interface or a Bean annotation with
							//     definePropertyNameConstants=true, we need to have our PropertyNameConstants
							//     extend it
							Collection<TypeDeclaration> nestedTypes = superclass.getNestedTypes();
							for (TypeDeclaration typeDeclaration : nestedTypes) {
								if ("PropertyNames".equals(typeDeclaration.getSimpleName()) && (typeDeclaration instanceof InterfaceDeclaration)) {
									data.setExtendPropertyNameConstants(true);
								}
							}
							if (!data.isExtendPropertyNameConstants()) {
								// check if the superclass is annotated with Bean
								Bean annotation = superclass.getAnnotation(Bean.class);
								if (annotation != null)
									data.setExtendPropertyNameConstants(annotation.definePropertyNameConstants());
							}
						}
					}
				}
				//				if (true)
				//					return;
				data.setClassModifiers(classModifiers);

				String superClassName = classDeclaration.getSuperclass().toString();
				int lt = superClassName.indexOf('<');
				if (lt != -1) {
					superClassName = superClassName.substring(0, lt);
				}
				int dot = superClassName.lastIndexOf('.');
				if (dot != -1) {
					String superClassPackageName = superClassName.substring(0, dot);
					if (!superClassPackageName.equals(packageDeclaration.getQualifiedName())) {
						superClassName = ""; // force error below
					} else {
						superClassName = superClassName.substring(dot + 1);
					}
				}

				if (!superClassName.equals(classDeclaration.getSimpleName() + "Gen")) {
					env_.getMessager().printError(declaration.getPosition(),
							classDeclaration.getQualifiedName() + " must extend " + classDeclaration.getQualifiedName() + "Gen for @Bean to work properly");
					return;
				}

				Bean bean = declaration.getAnnotation(Bean.class);
				data.setDate(new Date());
				data.setSpacesForLeadingTabs(bean.spacesForLeadingTabs());
				data.setDefinePropertyNameConstants(bean.definePropertyNameConstants());
				data.setCloneable(bean.cloneable());

				data.setYear(Calendar.getInstance().get(Calendar.YEAR));
				data.setEqualsShouldCheckSuperEquals(bean.equalsShouldCheckSuperEquals());

				data.setClassName(classDeclaration.getSimpleName());
				String packageName = packageDeclaration.getQualifiedName();
				data.setPackageName(packageName);

				// validate the property interface definition flags
				if (data.isDefinePropertyNameConstants()) {
					if (data.isExtendPropertyNameConstants()) {
						if (data.getSuperclass() == null) {
							env_.getMessager().printError(declaration.getPosition(),
									classDeclaration.getQualifiedName() + " cannot specify extendPropertyNameConstants=true without a superclass");
							return;
						}
					}
				} else if (data.isExtendPropertyNameConstants()) {
					env_.getMessager().printError(declaration.getPosition(),
							classDeclaration.getQualifiedName() + " cannot specify extendPropertyNameConstants=true if definePropertyNameConstants=false");
					return;
				}

				// find any methods that have default parameters
				Collection<MethodDeclaration> methodsToCheck = classDeclaration.getMethods();
				methods: for (MethodDeclaration methodDeclaration : methodsToCheck) {
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
							env_.getMessager().printError(parameterDeclaration.getPosition(),
							"All parameters after a parameter annotated with @Default must be annotated with @Default");
							continue methods;
						}
						n++;
					}

					if (seenDefault) {
						if (methodDeclaration.getModifiers().contains(Modifier.PRIVATE)) {
							env_.getMessager().printError(methodDeclaration.getPosition(),
							"Private methods cannot use @Default parameters");
						}
						if (methodDeclaration.getModifiers().contains(Modifier.STATIC)) {
							env_.getMessager().printError(methodDeclaration.getPosition(),
							"Static methods cannot use @Default parameters");
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
								Method method = new Method();
								method.setName(methodName);
								method.setReturnType(returnType);
								method.setThrowsClause(throwsClause);
								method.setArgDecls(argDecl);
								method.setModifiers(modifiers3);
								method.setArgs(callArgsWithDefaults);
								data.addDefaultMethod(method);
							}
							if (i > 0) {
								argDecl += ", ";
								callArgs += ", ";
							}
							argDecl += types[i] + ' ' + names[i];
							callArgs += names[i];
						}
						Method method = new Method();
						method.setName(methodName);
						method.setReturnType(returnType);
						method.setThrowsClause(throwsClause);
						method.setModifiers(modifiers3);
						method.setAbstract(true);
						method.setArgDecls(argDecl);
						data.addDefaultMethod(method);
					}
				}

				String firstPropertyName = null;
				Set<String> propertyNames = new HashSet<String>();
				boolean atLeastOneBound = false;
				boolean atLeastOneDouble = false;
				boolean atLeastOneObject = false;
				if (bean.properties() != null) {
					for (Property property : bean.properties()) {
						if (property == null) {
							continue;
						}
						if (firstPropertyName == null)
							firstPropertyName = property.name();
						if (propertyNames.contains(property.name())) {
							env_.getMessager().printError(
									declaration.getPosition(),
									"Duplicate property name '" + property.name() +
							"' specified for @Bean properties definition");
						} else {
							propertyNames.add(property.name());
						}

						String plural = null;

						switch (property.kind()) {
						case MAP:
						case UNMODIFIABLE_MAP:
						case SET:
						case UNMODIFIABLE_SET:
						case LIST:
						case UNMODIFIABLE_LIST:
							if ("".equals(property.plural())) {
								plural = property.name() + "s";
							} else {
								plural = property.plural();
							}
							break;
						default:
							if (!"".equals(property.plural())) {
								env_.getMessager().printError(declaration.getPosition(),
								"Cannot specify plural name for Simple properties in @Bean");
								return;
							}
						break;
						}

						if (property.bound()) {
							if (property.isStatic()) {
								env_.getMessager().printError(declaration.getPosition(),
								"Static properties cannot be declared bound");
								return;
							} else {
								atLeastOneBound = true;
							}
						}
						PropertySpec propertySpec = new PropertySpec();
						propertySpec.setKind(property.kind());
						propertySpec.setOmitFromToString(property.omitFromToString());
						data.addProperty(propertySpec);
						String type = selectType(declaration, "@Property", property, "type", "typeString", "java.lang.String", true);
						if (type == null) {
							return;
						}
						if ("double".equals(type))
							atLeastOneDouble = true;

						propertySpec.setType(type);

						// evil hack to get the type, which is a Class

						if (property.kind().isMap()) {
							propertySpec.setKind(property.kind());
							String keyType = selectType(declaration, "@Property", property, "keyType", "keyTypeString", "java.lang.String", false);
							if (keyType == null) {
								env_.getMessager().printError(declaration.getPosition(),
										"keytype cannot be null for map property " + property.name() + " in @Property");
								return;
							}
							propertySpec.setKeyType(keyType);
							propertySpec.setPluralName(plural);

						} else {
							String keyType = selectType(declaration, "@Property", property, "keyType", "keyTypeString", null, false);
							if (keyType != null) {
								env_.getMessager().printError(declaration.getPosition(),
										"keytype can only be specified for map properties; property " + property.name() + " is not a map");
								return;
							}
							if (property.kind().isList() || property.kind().isSet()) {
								propertySpec.setPluralName(plural);
							} else {
								propertySpec.setPrimitive(BeanAnnotationProcessor.PRIMITIVE_TYPES.contains(type));
							}
						}

						propertySpec.setName(property.name());

						if (!propertySpec.isPrimitive())
							atLeastOneObject = true;
						propertySpec.setBound(property.bound());
						Access reader = property.reader();
						Access writer = property.writer();
						if (writer == Access.NOT_SPECIFIED) {
							writer = bean.writer();
						}
						if (reader == Access.NOT_SPECIFIED) {
							reader = bean.reader();
						}

						if (writer.exists()) {
							propertySpec.setWriterAccess(writer.getModifier());
						}
						if (reader.exists()) {
							propertySpec.setReaderAccess(reader.getModifier());
						}
						propertySpec.setReadable(reader.exists());
						propertySpec.setWriteable(writer.exists());
						propertySpec.setNotNull(property.notNull());
						if (propertySpec.isNotNull() && propertySpec.isPrimitive()) {
							env_.getMessager().printError(declaration.getPosition(),
									"Cannot specify notNull for primitive-typed property " + propertySpec.getName() + " in @Property");
							return;
						}
						String extraFieldKeywords = "";
						String extraMethodKeywords = "";
						if (property.isStatic()) {
							extraFieldKeywords = "static ";
							extraMethodKeywords = "static ";
						}
						if (property.isSynchronized()) {
							if (property.isStatic()) {
								extraMethodKeywords += "synchronized ";
							} else {
								extraMethodKeywords = "synchronized ";
							}
						}
						propertySpec.setExtraFieldKeywords(extraFieldKeywords);
						propertySpec.setExtraMethodKeywords(extraMethodKeywords);
					}
					data.setFirstPropertyName(firstPropertyName);
				}

				if (bean.observers() != null) {
					for (Observer observer : bean.observers()) {
						if (observer == null) {
							continue;
						}
						Type type = new Type();
						data.addObserver(type);
						String typeName = selectType(declaration, "@Observer", observer, "type", "typeString", null, true);
						if (typeName == null) {
							return;
						}
						type.setName(typeName);
						defineListenerOrDelegate(false, type, packageDeclaration, "listener interface", "eventsets", packageName);
					}
				}

				if (bean.nullObjects().length > 0 && !"".equals(bean.nullObjects()[0])) {
					for (NullObject nullObject : bean.nullObjects()) {
						if (nullObject == null) {
							continue;
						}
						Type listener = new Type();
						String type = selectType(declaration, "@NullObject", nullObject, "type", "typeString", null, true);
						if (type == null) {
							return;
						}
						listener.setName(type);
						data.addNullObject(listener);
						defineListenerOrDelegate(true, listener, classDeclaration, "null implementation class/interface", "nullImplementationName", packageName);
					}
				}

				if (bean.delegates() != null) {
					for (Delegate delegate : bean.delegates()) {
						if (delegate == null) {
							continue;
						}
						try {
							DelegateSpec delegateSpec = new DelegateSpec();
							delegateSpec.setAccessor(delegate.accessor());
							data.addDelegate(delegateSpec);
							String type = selectType(declaration, "@Delegate", delegate, "type", "typeString", null, true);
							if (type == null) {
								return;
							}
							delegateSpec.setName(type);
							defineListenerOrDelegate(false, delegateSpec, packageDeclaration, "delegate type", "delegates", packageName);

							// TODO if property doesn't exist, define it
						} catch (NoSuchElementException e) {
							env_.getMessager().printError(declaration.getPosition(),
							"Invalid delegate specification; must be type,varName,type,varName...");
						}
					}
				}

				data.setDefinePropertyChangeSupport(!superclassHasPropertyChangeSupport && atLeastOneBound);
				data.setAtLeastOneDouble(atLeastOneDouble);
				data.setAtLeastOneObject(atLeastOneObject);
				data.setDefineSimpleEqualsAndHashCode(bean.defineEqualsAndHashCode());
				data.setCreatePropertyMap(bean.defineCreatePropertyMap());

				Filer f = env_.getFiler();
				PrintWriter pw = f.createSourceFile(classDeclaration.getQualifiedName() + "Gen");

				if (nestedEclipse) {
					// debugging in eclipse -- reread the template each time
					FileReader fileReader = new FileReader("/eclipse34/javadude-workspace/com.javadude.annotation/src/$packageName$/$className$Gen.java");
					template = new TemplateReader().readTemplate(fileReader);
				}
				template.process(new Symbols(data.createPropertyMap()), pw, -1);
				pw.close();
			} catch (ThreadDeath e) {
				throw e;
			} catch (ExpressionException e) {
				env_.getMessager().printError(declaration.getPosition(), "@Bean generator error: " + e.getMessage());
			} catch (Throwable t) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter = new PrintWriter(stringWriter);
				t.printStackTrace(printWriter);
				printWriter.close();
				env_.getMessager().printError(declaration.getPosition(), "Unexpected exception: " + stringWriter.toString());
			}
		}
	}
	private boolean checkPropertyChangeSupport(ClassDeclaration superclass) {
		Collection<MethodDeclaration> methods = superclass.getMethods();
		for (MethodDeclaration methodDeclaration : methods) {
			if ("getPropertyChangeSupport".equals(methodDeclaration.getSimpleName()) &&
					methodDeclaration.getParameters().isEmpty() &&
					"java.beans.PropertyChangeSupport".equals(methodDeclaration.getReturnType().toString()))
				return true;
		}
		if (superclass.getSuperclass() != null)
			return checkPropertyChangeSupport(superclass.getSuperclass().getDeclaration());
		return false;
	}
	private TypeDeclaration getType(Declaration declaration, String name, String packageName, String notFoundMessage) {
		TypeDeclaration typeDeclaration = env_.getTypeDeclaration(name);
		if (typeDeclaration != null) {
			return typeDeclaration;
		}

		// try it with the package name prepended
		typeDeclaration = env_.getTypeDeclaration(packageName + '.' + name);
		if (typeDeclaration != null) {
			return typeDeclaration;
		}

		env_.getMessager().printError(declaration.getPosition(), notFoundMessage);
		return null;
	}

	private void defineListenerOrDelegate(boolean abstractOnly, Type type, Declaration declaration, String typeOfThing, String partOfBean, String packageName) {
		TypeDeclaration typeDeclaration = getType(declaration, type.getName(), packageName, "Cannot find " + typeOfThing + " " + type.getName() + " defined as an " + partOfBean + " in @Bean (you probably need to fully-qualify it)");
		if (typeDeclaration == null) {
			return;
		}
		Collection<? extends MethodDeclaration> methods = typeDeclaration.getMethods();
		for (MethodDeclaration methodDeclaration : methods) {
			if (methodDeclaration.getModifiers().contains(Modifier.STATIC)) {
				continue;
			}
			if (abstractOnly) {
				if (!methodDeclaration.getModifiers().contains(Modifier.ABSTRACT)) {
					continue;
				}
			} else {
				if (!methodDeclaration.getModifiers().contains(Modifier.PUBLIC)) {
					continue;
				}
			}
			if (BeanAnnotationProcessor.METHODS_TO_SKIP.contains(methodDeclaration.getSimpleName())) {
				continue;
			}

			type.addMethod(setupMethod(methodDeclaration, true));
		}
	}

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
