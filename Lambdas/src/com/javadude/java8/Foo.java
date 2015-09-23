package com.javadude.java8;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.Predicate;

public class Foo {
	public static class Person {
		public String name;
		public String surname;
		public Person(String name, String surname) {
			this.name = name;
			this.surname = surname;
		}
		@Override
		public String toString() {
			return "Person [name=" + name + ", surname=" + surname + "]";
		}
	}
	public interface AAA {
		public void fee(int x, int y);
	}
	public interface BBB {
		public void fee();
	}
	
	// BinaryOperator  (x,y) -> z
	// UnaryOperator  (x) -> z
	// Function  (x) -> z
	// Consumer  (x) -> void
	// Supplier  () -> x
	
	public int sum(IntBinaryOperator o) {
		return o.applyAsInt(5, 6);
	}
	public Person workTogether(BinaryOperator<Person> o) {
		return o.apply(new Person("Scott", "Stanchfield"), new Person("Steve", "Stanchfield"));
	}
	public void doTest(Predicate<String> predicate) {
		if (predicate.test("Abe"))
			System.out.println("Ok!");
		else
			System.out.println("Bad!");
	}
	public void addActionListener(AAA aaa) {
		
	}
	public void addActionListener(BBB aaa) {
		
	}
	public void addActionListener(ActionListener a) {
		
	}
	public void addActionListener(ItemListener e) {
		
	}
	
	public static void main(String[] args) {
		new Foo().addActionListener((ActionEvent e) -> System.out.println("foo"));
		new Foo().addActionListener((int x, int y) -> System.out.println("foo"));
		new Foo().addActionListener(() -> System.out.println("foo"));
		new Foo().doTest(s -> s.length() > 4);
		Person cruft = new Foo().workTogether((p1,p2) -> new Person(p1.name + p2.name, p1.surname + p2.surname));
		System.out.println(cruft);
	}
}
