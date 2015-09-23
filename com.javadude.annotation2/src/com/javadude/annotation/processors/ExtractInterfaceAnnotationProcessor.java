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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import com.javadude.annotation.Expose;
import com.javadude.annotation.ExtractInterface;
import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.ReferenceType;

// TODO copy javadoc to generated interface
// TODO superinterfaces -- allows bean.extractInterface + this extract interface
public class ExtractInterfaceAnnotationProcessor implements AnnotationProcessor {
    private AnnotationProcessorEnvironment env_;

    public ExtractInterfaceAnnotationProcessor(AnnotationProcessorEnvironment env) {
        env_ = env;
    }

    public void process() {
        AnnotationTypeDeclaration extractInterfaceAnnotationDeclaration = (AnnotationTypeDeclaration) env_.getTypeDeclaration(ExtractInterface.class.getName());
        for (Declaration extractInterfaceDeclaration : env_.getDeclarationsAnnotatedWith(extractInterfaceAnnotationDeclaration)) {
            try {
                if (!(extractInterfaceDeclaration instanceof ClassDeclaration)) {
                    env_.getMessager().printError(extractInterfaceDeclaration.getPosition(),
                            "You can only annotate class declarations with @ExtractInterface");
                    return;
                }

                ClassDeclaration classDeclaration = (ClassDeclaration) extractInterfaceDeclaration;
                PackageDeclaration packageDeclaration = classDeclaration.getPackage();

                ExtractInterface extractInterfaceAnnotation = extractInterfaceDeclaration.getAnnotation(ExtractInterface.class);

                PrintWriter interfaceFile = null;
                try {
                    interfaceFile = env_.getFiler().createSourceFile(packageDeclaration.getQualifiedName() + "." + extractInterfaceAnnotation.name());

                    interfaceFile.println("package " + packageDeclaration.getQualifiedName() + ';');
                    interfaceFile.println();
                    if (!extractInterfaceAnnotation.nonPublic()) {
                        interfaceFile.print("public ");
                    }
                    interfaceFile.print("interface " + extractInterfaceAnnotation.name());
                    String[] superinterfaces = extractInterfaceAnnotation.superinterfaces();
                    if (superinterfaces.length > 0) {
                    	interfaceFile.print(" extends ");
                    	boolean first = true;
                    	for (String superinterface : superinterfaces) {
                    		if (first)
                    			first = false;
                    		else
                    			interfaceFile.print(",");
                    		interfaceFile.print(superinterface);
						}
                    }
                    interfaceFile.println(" {");

                    Collection<Declaration> exposedMethods;

                    AnnotationTypeDeclaration exposeAnnotationDeclaration = (AnnotationTypeDeclaration) env_.getTypeDeclaration(Expose.class.getName());
                    exposedMethods = env_.getDeclarationsAnnotatedWith(exposeAnnotationDeclaration);
                    if (extractInterfaceAnnotation.exposeAllPublicMethods()) {
                        if (!exposedMethods.isEmpty()) {
                            for (Declaration declaration : exposedMethods) {
                                // TODO set error message on @Expose annotation, not method name
                                env_.getMessager().printError(declaration.getPosition(), "If @ExtractInterface.exposeAllPublicMethods==true, you cannot specify @Expose on individual methods");
                            }
                            env_.getMessager().printError(extractInterfaceDeclaration.getPosition(), "If @ExtractInterface.exposeAllPublicMethods==true, you cannot specify @Expose on individual methods");
                            return;
                        }
                        // all non-static public methods should be exposed
                        exposedMethods = new ArrayList<Declaration>();
                        Collection<MethodDeclaration> methods = classDeclaration.getMethods();
                        for (MethodDeclaration methodDeclaration : methods) {
                            if (methodDeclaration.getModifiers().contains(Modifier.PUBLIC) && !methodDeclaration.getModifiers().contains(Modifier.STATIC)) {
                                exposedMethods.add(methodDeclaration);
                            }
                        }
                    }

                    for (Declaration exposedDeclaration : exposedMethods) {
                        // @Target forces these to only be on methods -- no extra check needed
                        MethodDeclaration exposedMethod = (MethodDeclaration) exposedDeclaration;

                        if (!exposedMethod.getModifiers().contains(Modifier.PUBLIC)) {
                            env_.getMessager().printError(extractInterfaceDeclaration.getPosition(), "You can only @Expose public methods");
                            return;
                        }
                        interfaceFile.print("    " + Utils.getTypeName(exposedMethod.getReturnType()) + " " + exposedMethod.getSimpleName() + '(');
                        boolean first = true;
                        Collection<ParameterDeclaration> parameters = exposedMethod.getParameters();
                        for (ParameterDeclaration parameterDeclaration : parameters) {
                            if (first) {
                                first = false;
                            } else {
                                interfaceFile.print(", ");
                            }
                            interfaceFile.print(Utils.getTypeName(parameterDeclaration.getType()) + ' ' + parameterDeclaration.getSimpleName());
                        }

                        interfaceFile.print(")");

                        Collection<ReferenceType> thrownTypes = exposedMethod.getThrownTypes();
                        first = true;
                        if (!thrownTypes.isEmpty()) {
                            interfaceFile.print(" throws ");
                            for (ReferenceType thrownType : thrownTypes) {
                                if (first) {
                                    first = false;
                                } else {
                                    interfaceFile.print(", ");
                                }
                                interfaceFile.print(Utils.getTypeName(thrownType));
                            }
                        }

                        interfaceFile.println(";");
                    }
                    interfaceFile.println("}");
                } finally {
                    if (interfaceFile != null) {
                        interfaceFile.close();
                    }
                }
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable t) {
                env_.getMessager().printError(extractInterfaceDeclaration.getPosition(), "Unexpected exception: " + t.getMessage());
            }
        }
    }
}
