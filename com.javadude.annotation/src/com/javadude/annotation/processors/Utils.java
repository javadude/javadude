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

import com.sun.mirror.type.AnnotationType;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.EnumType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.TypeVariable;
import com.sun.mirror.type.VoidType;
import com.sun.mirror.type.WildcardType;
import com.sun.mirror.util.TypeVisitor;

/**
 * TODO Description of .
 */
public class Utils {
    private Utils() {
        // do nothing
    }
    public static String upperFirstChar(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
    public static String lowerFirstChar(String name) {
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
    public static String getTypeName(TypeMirror type) {
        final StringBuffer typeName = new StringBuffer();
        TypeVisitor v = new TypeVisitor() {
            public void visitWildcardType(WildcardType t) { typeName.append(t); }
            public void visitVoidType(VoidType t) { typeName.append(t); }
            public void visitTypeVariable(TypeVariable t) { typeName.append(t); }
            public void visitTypeMirror(TypeMirror t) { typeName.append(t); }
            public void visitReferenceType(ReferenceType t) { typeName.append(t); }
            public void visitPrimitiveType(PrimitiveType t) { typeName.append(t); }
            public void visitInterfaceType(InterfaceType t) { typeName.append(t); }
            public void visitEnumType(EnumType t) { typeName.append(t); }
            public void visitDeclaredType(DeclaredType t) { typeName.append(t); }
            public void visitClassType(ClassType t) { typeName.append(t); }
            public void visitArrayType(ArrayType t) { typeName.append(t); }
            public void visitAnnotationType(AnnotationType t) { typeName.append(t); }
        };
        type.accept(v);
        return typeName.toString();
    }
}
