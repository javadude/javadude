// CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR
// -- DO NOT EDIT  -  THIS CODE WILL BE REGENERATED! --
package com.javadude.annotation.processors;

@javax.annotation.Generated(
    value = "com.javadude.annotation.processors.BeanAnnotationProcessor",
    date = "Mon Dec 22 21:31:47 EST 2008",
    comments = "CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR; DO NOT EDIT! THIS CODE WILL BE REGENERATED!")
public abstract class DelegateSpecGen extends com.javadude.annotation.processors.Type {
    public DelegateSpecGen() {
        ;
    }

    private java.lang.String accessor_;
    public java.lang.String getAccessor() {
        return accessor_;
    }
    public void setAccessor(java.lang.String value)  {
        accessor_ = value;
    }

    @Override
    public java.lang.String toString() {
        return getClass().getName() + '[' + paramString() + ']';
    }
    @Override
    protected java.lang.String paramString() {
        return
               "accessor=" + accessor_;
    }
    @Override
	public java.util.Map<java.lang.String, java.lang.Object> createPropertyMap() {
                    java.util.Map<java.lang.String, java.lang.Object> map = super.createPropertyMap();
                    map.put("accessor", getAccessor());
                return map;
    }
}
