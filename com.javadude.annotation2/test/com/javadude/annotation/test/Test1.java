package com.javadude.annotation.test;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

import com.javadude.annotation.Access;
import com.javadude.annotation.Property;
import com.javadude.annotation.PropertyKind;
import com.javadude.annotation.generators.PropertyGenerator;
import com.javadude.annotation.generators.model.TargetClass;

public class Test1 {
	private String generate(String testName, Property property) {
		TargetClass targetClass = new TargetClass();
		targetClass.setName("Person" + testName);
		targetClass.setPackage("test1");
		PropertyGenerator.generate(targetClass, property);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		targetClass.generate(pw, "\t", "");
		pw.close();
		return sw.toString();
	}
	@Test public void test1() {
		String code = generate("1", new MockProperty(false, false, false, false, false, Void.class, "", Void.class, "", "name", Access.PUBLIC, Access.PUBLIC, PropertyKind.SIMPLE));
		System.out.println(code);
	}
	@Test public void test2() {
		String code = generate("2", new MockProperty(false, true, false, false, false, Void.class, "", Void.class, "", "name", Access.PUBLIC, Access.PUBLIC, PropertyKind.SIMPLE));
		System.out.println(code);
	}
	@Test public void test2a() {
		String code = generate("2a", new MockProperty(true, true, false, false, false, Void.class, "", Void.class, "", "name", Access.PUBLIC, Access.PUBLIC, PropertyKind.SIMPLE));
		System.out.println(code);
	}
	@Test public void test3() {
		String code = generate("3", new MockProperty(false, false, true, false, false, Void.class, "", Void.class, "", "name", Access.PUBLIC, Access.PUBLIC, PropertyKind.SIMPLE));
		System.out.println(code);
	}
	@Test public void test4() {
		String code = generate("4", new MockProperty(false, false, false, true, false, Void.class, "", Void.class, "", "name", Access.PUBLIC, Access.PUBLIC, PropertyKind.SIMPLE));
		System.out.println(code);
	}
	@Test public void test5() {
		String code = generate("5", new MockProperty(false, false, false, false, true, Void.class, "", Void.class, "", "name", Access.PUBLIC, Access.PUBLIC, PropertyKind.SIMPLE));
		System.out.println(code);
	}
	@Test public void test6() {
		String code = generate("6", new MockProperty(true, false, false, false, false, Void.class, "", Void.class, "", "name", Access.PUBLIC, Access.PUBLIC, PropertyKind.SIMPLE));
		System.out.println(code);
	}
}
