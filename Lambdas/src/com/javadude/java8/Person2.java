package com.javadude.java8;

import java.util.Optional;

public class Person2 implements Comparable<Person2> {
	public enum Sex {
		Male, Female;
	}
	private String name;
	private int age;
	private Sex sex;
	private Person2 father;
	public Person2(Person2 father, String name, int age, Sex sex) {
		this.father = father;
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
	public Optional<Person2> getFather() {
		return Optional.ofNullable(father);
	}
	public Sex getSex() {
		return sex;
	}
	public String getName() {
		return name;
	}
	public int getAge() {
		return age;
	}
	@Override
	public int compareTo(Person2 o) {
		return name.compareTo(o.name);
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", sex=" + sex + "]";
	}
}