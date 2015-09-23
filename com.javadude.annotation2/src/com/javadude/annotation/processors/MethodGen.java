// CODE GENERATED BY JAVADUDE BEAN ANNOTATION PROCESSOR
// -- DO NOT EDIT  -  THIS CODE WILL BE REGENERATED! --
package com.javadude.annotation.processors;

import java.util.Map;

public abstract class MethodGen extends BasePushable {
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

	private java.lang.String genericDecls_;
	public java.lang.String getGenericDecls() {
		return genericDecls_;
	}
	public void setGenericDecls(java.lang.String value)  {
		genericDecls_ = value;
	}

	private java.lang.String throwsClause_;
	public java.lang.String getThrowsClause() {
		return throwsClause_;
	}
	public void setThrowsClause(java.lang.String value)  {
		throwsClause_ = value;
	}

	private java.lang.String modifiers_;
	public java.lang.String getModifiers() {
		return modifiers_;
	}
	public void setModifiers(java.lang.String value)  {
		modifiers_ = value;
	}

	private java.lang.String nullBody_;
	public java.lang.String getNullBody() {
		return nullBody_;
	}
	public void setNullBody(java.lang.String value)  {
		nullBody_ = value;
	}

	private boolean abstract_;
	public boolean isAbstract() {
		return abstract_;
	}
	public void setAbstract(boolean value)  {
		abstract_ = value;
	}
	public boolean isReturns() {
		return !"void".equals(getReturnType());
	}

	@Override
	public java.lang.String toString() {
		return getClass().getName() + '[' + paramString() + ']';
	}
	protected java.lang.String paramString() {
		return
		"name=" + name_ +
		",args=" + args_ +
		",argDecls=" + argDecls_ +
		",returnType=" + returnType_ +
		",throwsClause=" + throwsClause_ +
		",modifiers=" + modifiers_ +
		",nullBody=" + nullBody_ +
		",abstract=" + abstract_;
	}
	@Override
	public java.util.Map<java.lang.String, java.lang.Object> createPropertyMap() {
		Map<String, Object> map = super.createPropertyMap();
		map.put("name", getName());
		map.put("args", getArgs());
		map.put("argDecls", getArgDecls());
		map.put("returnType", getReturnType());
		map.put("genericDecls", getGenericDecls());
		map.put("throwsClause", getThrowsClause());
		map.put("modifiers", getModifiers());
		map.put("nullBody", getNullBody());
		map.put("abstract", isAbstract());
		map.put("returns", isReturns());
		return map;
	}
}
