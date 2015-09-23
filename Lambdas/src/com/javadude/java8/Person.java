package com.javadude.java8;

public class Person implements Comparable<Person> {
	public enum Sex {
		Male, Female;
	}
	private String name;
	private int age;
	private Sex sex;
	private Person father;
	public Person(Person father, String name, int age, Sex sex) {
		this.father = father;
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
	public Person getFather() {
		return father;
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
	public int compareTo(Person o) {
		return name.compareTo(o.name);
	}
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", sex=" + sex + "]";
	}
}