// CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR 
// -- DO NOT EDIT  -  THIS CODE WILL BE REGENERATED! --
package com.javadude.annotation.processors;

@javax.annotation.Generated(
    value = "com.javadude.annotation.processors.BeanAnnotationProcessor", 
    date = "Wed Sep 23 15:29:55 EDT 2015", 
    comments = "CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR; DO NOT EDIT! THIS CODE WILL BE REGENERATED!")
public abstract class PropertySpecGen  {
    public PropertySpecGen() {
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

    private java.lang.String writerAccess_;
    public java.lang.String getWriterAccess() {
        return writerAccess_;
    }
    public void setWriterAccess(java.lang.String value)  {
        writerAccess_ = value;
    }

    private java.lang.String readerAccess_;
    public java.lang.String getReaderAccess() {
        return readerAccess_;
    }
    public void setReaderAccess(java.lang.String value)  {
        readerAccess_ = value;
    }

    private java.lang.String type_;
    public java.lang.String getType() {
        return type_;
    }
    public void setType(java.lang.String value)  {
        type_ = value;
    }

    private java.lang.String intConversion_;
    public java.lang.String getIntConversion() {
        return intConversion_;
    }
    public void setIntConversion(java.lang.String value)  {
        intConversion_ = value;
    }

    private boolean notNull_;
    public boolean isNotNull() {
        return notNull_;
    }
    public void setNotNull(boolean value)  {
        notNull_ = value;
    }

    private boolean readable_;
    public boolean isReadable() {
        return readable_;
    }
    public void setReadable(boolean value)  {
        readable_ = value;
    }

    private boolean writeable_;
    public boolean isWriteable() {
        return writeable_;
    }
    public void setWriteable(boolean value)  {
        writeable_ = value;
    }

    private boolean bound_;
    public boolean isBound() {
        return bound_;
    }
    public void setBound(boolean value)  {
        bound_ = value;
    }

    private boolean primitive_;
    public boolean isPrimitive() {
        return primitive_;
    }
    public void setPrimitive(boolean value)  {
        primitive_ = value;
    }

    private java.lang.String pluralName_;
    public java.lang.String getPluralName() {
        return pluralName_;
    }
    public void setPluralName(java.lang.String value)  {
        pluralName_ = value;
    }

    private java.lang.String upperPluralName_;
    public java.lang.String getUpperPluralName() {
        return upperPluralName_;
    }
    public void setUpperPluralName(java.lang.String value)  {
        upperPluralName_ = value;
    }

    private java.lang.String baseType_;
    public java.lang.String getBaseType() {
        return baseType_;
    }
    public void setBaseType(java.lang.String value)  {
        baseType_ = value;
    }

    private java.lang.String keyType_;
    public java.lang.String getKeyType() {
        return keyType_;
    }
    public void setKeyType(java.lang.String value)  {
        keyType_ = value;
    }

    private java.lang.String extraMethodKeywords_;
    public java.lang.String getExtraMethodKeywords() {
        return extraMethodKeywords_;
    }
    public void setExtraMethodKeywords(java.lang.String value)  {
        extraMethodKeywords_ = value;
    }

    private java.lang.String extraFieldKeywords_;
    public java.lang.String getExtraFieldKeywords() {
        return extraFieldKeywords_;
    }
    public void setExtraFieldKeywords(java.lang.String value)  {
        extraFieldKeywords_ = value;
    }

    private boolean omitFromToString_;
    public boolean isOmitFromToString() {
        return omitFromToString_;
    }
    public void setOmitFromToString(boolean value)  {
        omitFromToString_ = value;
    }

    private com.javadude.annotation.PropertyKind kind_;
    public com.javadude.annotation.PropertyKind getKind() {
        return kind_;
    }
    public void setKind(com.javadude.annotation.PropertyKind value)  {
        kind_ = value;
    }


    @Override
    public java.lang.String toString() {
        return getClass().getName() + '[' + paramString() + ']';
    }
    protected java.lang.String paramString() {
        return 
               "name=" + name_ +
               ",upperName=" + upperName_ +
               ",writerAccess=" + writerAccess_ +
               ",readerAccess=" + readerAccess_ +
               ",type=" + type_ +
               ",intConversion=" + intConversion_ +
               ",notNull=" + notNull_ +
               ",readable=" + readable_ +
               ",writeable=" + writeable_ +
               ",bound=" + bound_ +
               ",primitive=" + primitive_ +
               ",pluralName=" + pluralName_ +
               ",upperPluralName=" + upperPluralName_ +
               ",baseType=" + baseType_ +
               ",keyType=" + keyType_ +
               ",extraMethodKeywords=" + extraMethodKeywords_ +
               ",extraFieldKeywords=" + extraFieldKeywords_ +
               ",omitFromToString=" + omitFromToString_ +
               ",kind=" + kind_;
    }
}
