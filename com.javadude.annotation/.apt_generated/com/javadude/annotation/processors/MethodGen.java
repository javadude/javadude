// CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR 
// -- DO NOT EDIT  -  THIS CODE WILL BE REGENERATED! --
package com.javadude.annotation.processors;

@javax.annotation.Generated(
    value = "com.javadude.annotation.processors.BeanAnnotationProcessor", 
    date = "Wed Sep 23 15:29:55 EDT 2015", 
    comments = "CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR; DO NOT EDIT! THIS CODE WILL BE REGENERATED!")
public abstract class MethodGen  {
    public MethodGen() {
        ;
    }

    private java.lang.String name_;
    public java.lang.String getName() {
        return name_;
    }
    public void setName(java.lang.String value)  {
        name_ = value;
    }

    private java.lang.String upperName_;
    public java.lang.String getUpperName() {
        return upperName_;
    }
    public void setUpperName(java.lang.String value)  {
        upperName_ = value;
    }

    private java.lang.String args_;
    public java.lang.String getArgs() {
        return args_;
    }
    public void setArgs(java.lang.String value)  {
        args_ = value;
    }

    private java.lang.String argDecls_;
    public java.lang.String getArgDecls() {
        return argDecls_;
    }
    public void setArgDecls(java.lang.String value)  {
        argDecls_ = value;
    }

    private java.lang.String returnType_;
    public java.lang.String getReturnType() {
        return returnType_;
    }
    public void setReturnType(java.lang.String value)  {
        returnType_ = value;
    }

    private java.lang.String returnOrNot_;
    public java.lang.String getReturnOrNot() {
        return returnOrNot_;
    }
    public void setReturnOrNot(java.lang.String value)  {
        returnOrNot_ = value;
    }

    private java.lang.String throwsClause_;
    public java.lang.String getThrowsClause() {
        return throwsClause_;
    }
    public void setThrowsClause(java.lang.String value)  {
        throwsClause_ = value;
    }

    private java.lang.String access_;
    public java.lang.String getAccess() {
        return access_;
    }
    public void setAccess(java.lang.String value)  {
        access_ = value;
    }

    private boolean abstract_;
    public boolean isAbstract() {
        return abstract_;
    }
    public void setAbstract(boolean value)  {
        abstract_ = value;
    }


    @Override
    public java.lang.String toString() {
        return getClass().getName() + '[' + paramString() + ']';
    }
    protected java.lang.String paramString() {
        return 
               "name=" + name_ +
               ",upperName=" + upperName_ +
               ",args=" + args_ +
               ",argDecls=" + argDecls_ +
               ",returnType=" + returnType_ +
               ",returnOrNot=" + returnOrNot_ +
               ",throwsClause=" + throwsClause_ +
               ",access=" + access_ +
               ",abstract=" + abstract_;
    }
}
