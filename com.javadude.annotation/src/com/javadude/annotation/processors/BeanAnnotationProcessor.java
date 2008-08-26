/*******************************************************************************
 *  Copyright 2008 Scott Stanchfield.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package com.javadude.annotation.processors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.javadude.annotation.Access;
import com.javadude.annotation.Bean;
import com.javadude.annotation.Default;
import com.javadude.annotation.Delegate;
import com.javadude.annotation.NullObject;
import com.javadude.annotation.Observer;
import com.javadude.annotation.Property;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.MirroredTypeException;
import com.sun.mirror.type.ReferenceType;

// does not support standard indexed properties
// does not support constrained properties

// TODO check delegation -- limit to methods exposed via property type if defined as property
// TODO delegation + extractInterface -> must allow superinterfaces to be specified for generated interface

public class BeanAnnotationProcessor implements AnnotationProcessor {
    private static final Set<String> METHODS_TO_SKIP = new HashSet<String>();
    private static final Map<String, String> PRIMITIVE_TYPE_INT_CONVERSIONS = new HashMap<String, String>();
    static {
        BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.put("char", "");
        BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.put("byte", "");
        BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.put("int", "");
        BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.put("short", "");
        BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.put("long", "(int) ");
        BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.put("float", "(int) ");
        BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.put("double", "(int) ");
        BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.put("boolean", "X");
        // TODO add parameters...
        BeanAnnotationProcessor.METHODS_TO_SKIP.add("equals");
        BeanAnnotationProcessor.METHODS_TO_SKIP.add("hashCode");
        BeanAnnotationProcessor.METHODS_TO_SKIP.add("toString");
        BeanAnnotationProcessor.METHODS_TO_SKIP.add("wait");
        BeanAnnotationProcessor.METHODS_TO_SKIP.add("notify");
        BeanAnnotationProcessor.METHODS_TO_SKIP.add("notifyAll");
    }
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

    public void process() {
    	final AnnotationTypeDeclaration beanAnn = (AnnotationTypeDeclaration) env_.getTypeDeclaration(Bean.class.getName());
    	for (Declaration declaration : env_.getDeclarationsAnnotatedWith(beanAnn)) {
            try {
                if (!(declaration instanceof ClassDeclaration)) {
                    env_.getMessager().printError(declaration.getPosition(),
                            "You can only annotate class declarations with @Bean");
                    return;
                }

                // check that class is defined to extend XXXGen - possible???
                ClassDeclaration classDeclaration = (ClassDeclaration) declaration;
                PackageDeclaration packageDeclaration = classDeclaration.getPackage();

// TAKEN OUT FOR NOW - For some reason this causes every other build to not generate a class...
//                ClassType superclass = classDeclaration.getSuperclass();
//                String superClassName = superclass.toString();
//                if (!superClassName.equals(classDeclaration.getSimpleName() + "Gen")) {
//                	env_.getMessager().printError(declaration.getPosition(),
//                								  classDeclaration.getSimpleName() + " must extend " + classDeclaration.getSimpleName() + "Gen for @Bean to work properly");
//                	return;
//                }

                Bean bean = declaration.getAnnotation(Bean.class);
                Data data = new Data();
                data.setBean(bean);
                data.setSuperClass(selectType(declaration, "@Bean", bean, "superclass", "superclassString", null, false));

                data.setParamStringOverridden(bean.overrideParamString());
                data.setClassAccess(classDeclaration.getModifiers().contains(Modifier.PUBLIC) ? "public " : "");
                data.setClassName(classDeclaration.getSimpleName());
                String packageName = packageDeclaration.getQualifiedName();
                data.setPackageName(packageName);

                // find any methods that have default parameters
                Collection<MethodDeclaration> methodsToCheck = classDeclaration.getMethods();
                for (MethodDeclaration methodDeclaration : methodsToCheck) {
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
						}
						n++;
					}

					if (seenDefault) {
			            if (methodDeclaration.getModifiers().contains(Modifier.PRIVATE)) {
			            	env_.getMessager().printError(methodDeclaration.getPosition(),
			            								  "Private methods cannot use @Default parameters");
			            }
			            String access = "";
			            if (methodDeclaration.getModifiers().contains(Modifier.PUBLIC)) {
			            	access = "public ";
			            } else if (methodDeclaration.getModifiers().contains(Modifier.PROTECTED)) {
			            	access = "protected ";
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
								method.setAccess(access);
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
						method.setAccess(access);
						method.setAbstract(true);
						method.setArgDecls(argDecl);
						data.addDefaultMethod(method);
					}
				}


                Set<String> propertyNames = new HashSet<String>();
                boolean atLeastOneBound = false;
                if (bean.properties() != null) {
                    for (Property property : bean.properties()) {
                        if (property == null) {
                            continue;
                        }
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
                        propertySpec.setType(type);

                        // evil hack to get the type, which is a Class

                        if (property.kind().isMap()) {
                        	propertySpec.setKind(property.kind());
                        	String keyType = selectType(declaration, "@Property", property, "keyType", "keyTypeString", "java.lang.String", false);
                            if (keyType == null) {
	                            return;
                            }
                            propertySpec.setKeyType(keyType);
                            propertySpec.setPluralName(plural);
                        } else if (property.kind().isList() || property.kind().isSet()) {
                            propertySpec.setPluralName(plural);
                        } else {
                            String intConversion = BeanAnnotationProcessor.PRIMITIVE_TYPE_INT_CONVERSIONS.get(type);
                            propertySpec.setPrimitive(intConversion != null);
                            if ("X".equals(intConversion)) {
                                intConversion = '(' + property.name() + "_ ? 1 : 0)";
                            } else if (intConversion != null) {
                                intConversion = intConversion + property.name() + '_';
                            } else {
                                intConversion = property.name() + "_.hashCode()";
                            }
                            propertySpec.setIntConversion(intConversion);
                        }

                        propertySpec.setName(property.name());
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
                }

                if (bean.observers() != null) {
                    for (Observer observer : bean.observers()) {
                        if (observer == null) {
                            continue;
                        }
                        Listener listener = new Listener();
                        listener.setOverriding(observer.addOverrides());
                        data.addListener(listener);
                    	String type = selectType(declaration, "@Observer", observer, "type", "typeString", null, true);
                        if (type == null) {
                            return;
                        }
						listener.setName(type);
                        defineListenerOrDelegate(false, listener, packageDeclaration, "listener interface", "eventsets", packageName);
                    }
                }

                if (bean.nullObjectImplementations().length > 0 && !"".equals(bean.nullObjectImplementations()[0])) {
                    for (NullObject nullObject : bean.nullObjectImplementations()) {
                    	if (nullObject == null) {
                    		continue;
                    	}
                        Listener listener = new Listener();
                        String type = selectType(declaration, "@NullObject", nullObject, "type", "typeString", null, true);
                        if (type == null) {
                        	return;
                        }
                        listener.setName(type);
                        listener.setOverriding(nullObject.addOverrides());
                        data.addNullImplementation(listener);
                        defineListenerOrDelegate(true, listener, classDeclaration, "null implementation class/interface", "nullImplementationName", packageName);
                    }
                }

                if (bean.delegates() != null) {
                    for (Delegate delegate : bean.delegates()) {
                        if (delegate == null) {
                            continue;
                        }
                        try {
                            String accessor = null;
                            if (!"".equals(delegate.property())) {
                                accessor = delegate.property() + "_";
                            }
                            if (!"".equals(delegate.accessor())) {
                                if (accessor != null) {
                                    env_.getMessager().printError(declaration.getPosition(),
                                        "Cannot specify both accessor and property for @Delegate");
                                } else {
                                    accessor = delegate.accessor();
                                }
                            }
                            if (accessor == null) {
                                env_.getMessager().printError(declaration.getPosition(),
                                        "Must specify either accessor or property for @Delegate");
                            }
                            DelegateSpec delegateSpec = new DelegateSpec();
                            delegateSpec.setOverriding(delegate.addOverrides());
                            delegateSpec.setAccessor(accessor);
                            data.addDelegate(delegateSpec);
                        	String type = selectType(declaration, "@Delegate", delegate, "type", "typeString", null, true);
                            if (type == null) {
                                return;
                            }
                            delegateSpec.setName(type);

                        	String instantiateType = selectType(declaration, "@Observer", delegate, "instantiateAs", "instantiateAsString", null, false);
                            if (instantiateType != null && "".equals(delegate.property())) {
                                env_.getMessager().printError(declaration.getPosition(),
                                    "Must specify property for @Delegate if instantiateAs is specified");
                            }
                            delegateSpec.setInstantiateType(instantiateType);
                            if (!"".equals(delegate.property()) && !propertyNames.contains(delegate.property())) {
                                delegateSpec.setNeedToDefine(true);
                            }
                            defineListenerOrDelegate(false, delegateSpec, packageDeclaration, "delegate type", "delegates", packageName);

                            // TODO if property doesn't exist, define it
                        } catch (NoSuchElementException e) {
                            env_.getMessager().printError(declaration.getPosition(),
                                    "Invalid delegate specification; must be type,varName,type,varName...");
                        }
                    }
                }

                data.setAtLeastOneBound(atLeastOneBound);
                data.setDefineSimpleEqualsAndHashCode(bean.defineSimpleEqualsAndHashCode());
                data.setCreatePropertyMap(bean.createPropertyMap());

                Velocity.setProperty("resource.loader", "class");
                Velocity.setProperty("class.resource.loader.description", "Velocity Classpath Resource Loader");
                Velocity.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//              Velocity.setProperty("class.resource.loader.modificationCheckInterval", "1");
//              Velocity.setProperty("class.resource.loader.cache", "false");
                Velocity.init();

                VelocityContext context = new VelocityContext();
                context.put("data", data);
                context.put("date", new Date().toString());

                Template template = null;

                try {
                    template = Velocity.getTemplate("bean.vm");
                } catch (ResourceNotFoundException e) {
                    env_.getMessager().printError(declaration.getPosition(),
                            "Could not find template: " + e.getMessage());
                    return;
                } catch (ParseErrorException e) {
                    env_.getMessager().printError(declaration.getPosition(),
                            "Error parsing template: " + e.getMessage());
                    return;
                } catch (MethodInvocationException e) {
                    env_.getMessager().printError(declaration.getPosition(),
                            "Error invoking something during template processing: " + e.getMessage());
                    return;
                } catch (Exception e) {
                    env_.getMessager().printError(declaration.getPosition(),
                            "Error during template processing: " + e.getMessage());
                    return;
                }

                Filer f = env_.getFiler();
                PrintWriter pw = f.createSourceFile(classDeclaration.getQualifiedName() + "Gen");
                template.merge(context, pw);
                pw.close();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable t) {
            	StringWriter stringWriter = new StringWriter();
            	PrintWriter printWriter = new PrintWriter(stringWriter);
            	t.printStackTrace(printWriter);
            	printWriter.close();
                env_.getMessager().printError(declaration.getPosition(), "Unexpected exception: " + stringWriter.toString());
            }
        }
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

    private void defineListenerOrDelegate(boolean abstractOnly, Listener listener, Declaration declaration, String typeOfThing, String partOfBean, String packageName) {
        TypeDeclaration typeDeclaration = getType(declaration, listener.getName(), packageName, "Cannot find " + typeOfThing + " " + listener.getName() + " defined as an " + partOfBean + " in @Bean (you probably need to fully-qualify it)");
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
            Method method = new Method();
            listener.getMethods().add(method);
            method.setName(methodDeclaration.getSimpleName());
            method.setReturnType(Utils.getTypeName(methodDeclaration.getReturnType()));
            String argDecls = "";
            String args = "";

            Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
            for (ParameterDeclaration parameterDeclaration : parameters) {
                if (!"".equals(argDecls)) {
                    argDecls += ",";
                    args += ", ";
                }
                argDecls += Utils.getTypeName(parameterDeclaration.getType()) + ' ' + parameterDeclaration.getSimpleName();
                args += parameterDeclaration.getSimpleName();
            }
            method.setArgDecls(argDecls.replaceAll(",", ", "));
            method.setArgs(args);
            method.setThrowsClause(getThrowsClause(methodDeclaration));
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
