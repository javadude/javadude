package com.javadude.annotation.processors.template;

import junit.framework.Assert;

import org.junit.Test;

public class TestConditionParser {
	private ConditionParser conditionParser = new ConditionParser();
	@Test public void t1() {
		try {
			conditionParser.parse("", 0);
		} catch(ExpressionException e) {
			Assert.assertEquals("Missing expression on line 0", e.getMessage());
		}
	}
	@Test public void t2() {
		try {
			conditionParser.parse(")", 0);
		} catch(ExpressionException e) {
			Assert.assertEquals("Expected '!', '(' or variable; found ')' in expression ')' on line 0", e.getMessage());
		}
	}
	@Test public void t3() {
		try {
			conditionParser.parse("&", 0);
		} catch(ExpressionException e) {
			Assert.assertEquals("Expected '!', '(' or variable; found '&' in expression '&' on line 0", e.getMessage());
		}
	}
	@Test public void t4() {
		try {
			conditionParser.parse("|", 0);
		} catch(ExpressionException e) {
			Assert.assertEquals("Expected '!', '(' or variable; found '|' in expression '|' on line 0", e.getMessage());
		}
	}
	@Test public void t5() {
		Condition condition = conditionParser.parse("x", 0);
		Assert.assertEquals(VariableTest.class, condition.getClass());
		Assert.assertEquals("VariableTest[variable=x]", condition.toString());
	}
	@Test public void t5a() {
		Condition condition = conditionParser.parse("(x)", 0);
		Assert.assertEquals(VariableTest.class, condition.getClass());
		Assert.assertEquals("VariableTest[variable=x]", condition.toString());
	}
	@Test public void t6() {
		Condition condition = conditionParser.parse("    x    ", 0);
		Assert.assertEquals(VariableTest.class, condition.getClass());
		Assert.assertEquals("VariableTest[variable=x]", condition.toString());
	}
	@Test public void t6a() {
		Condition condition = conditionParser.parse(" (   x   ) ", 0);
		Assert.assertEquals(VariableTest.class, condition.getClass());
		Assert.assertEquals("VariableTest[variable=x]", condition.toString());
	}
	@Test public void t7() {
		Condition condition = conditionParser.parse("x & y", 0);
		Assert.assertEquals(And.class, condition.getClass());
		Assert.assertEquals("And[op1=VariableTest[variable=x],op2=VariableTest[variable=y]]", condition.toString());
	}
	@Test public void t7a() {
		Condition condition = conditionParser.parse("(x & y)", 0);
		Assert.assertEquals(And.class, condition.getClass());
		Assert.assertEquals("And[op1=VariableTest[variable=x],op2=VariableTest[variable=y]]", condition.toString());
	}
	@Test public void t8() {
		Condition condition = conditionParser.parse("x | y", 0);
		Assert.assertEquals(Or.class, condition.getClass());
		Assert.assertEquals("Or[op1=VariableTest[variable=x],op2=VariableTest[variable=y]]", condition.toString());
	}
	@Test public void t8a() {
		Condition condition = conditionParser.parse("(x | y)", 0);
		Assert.assertEquals(Or.class, condition.getClass());
		Assert.assertEquals("Or[op1=VariableTest[variable=x],op2=VariableTest[variable=y]]", condition.toString());
	}
	@Test public void t9() {
		Condition condition = conditionParser.parse("(x | y) & z | a", 0);
		Assert.assertEquals(Or.class, condition.getClass());
		Assert.assertEquals("Or[op1=And[op1=Or[op1=VariableTest[variable=x],op2=VariableTest[variable=y]],op2=VariableTest[variable=z]],op2=VariableTest[variable=a]]", condition.toString());
	}
	@Test public void t9a() {
		Condition condition = conditionParser.parse("(((x | y) & z | a))", 0);
		Assert.assertEquals(Or.class, condition.getClass());
		Assert.assertEquals("Or[op1=And[op1=Or[op1=VariableTest[variable=x],op2=VariableTest[variable=y]],op2=VariableTest[variable=z]],op2=VariableTest[variable=a]]", condition.toString());
	}
	@Test public void t9b() {
		Condition condition = conditionParser.parse("((x | y) & z) | a", 0);
		Assert.assertEquals(Or.class, condition.getClass());
		Assert.assertEquals("Or[op1=And[op1=Or[op1=VariableTest[variable=x],op2=VariableTest[variable=y]],op2=VariableTest[variable=z]],op2=VariableTest[variable=a]]", condition.toString());
	}
	@Test public void t10() {
		try {
			conditionParser.parse("((x | y) & z | a", 0);
		} catch (ExpressionException e) {
			Assert.assertEquals("End-of-expression found when expecting ')' in expression '((x | y) & z | a' on line 0", e.getMessage());
		}
	}
	@Test public void t11() {
		Condition condition = conditionParser.parse("(!(x | y) & z) | a", 0);
		Assert.assertEquals(Or.class, condition.getClass());
		Assert.assertEquals("Or[op1=And[op1=Not[op=Or[op1=VariableTest[variable=x],op2=VariableTest[variable=y]]],op2=VariableTest[variable=z]],op2=VariableTest[variable=a]]", condition.toString());
	}
}
