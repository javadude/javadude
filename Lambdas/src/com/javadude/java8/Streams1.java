package com.javadude.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.javadude.java8.Person.Sex;

public class Streams1 {
	private static List<Person> list(Person... people) {
		return Arrays.asList(people);
	}
	
	private static void optionals() {
		Person oliver1 = new Person(null, "Oliver", 74, Person.Sex.Male);
		Person scott1 = new Person(oliver1, "Scott", 47, Person.Sex.Male);
		List<Person> people = new ArrayList<Person>();
		people.add(oliver1);
		people.add(scott1);

		for (Person person : people) {
			Person father = person.getFather();
			if (father != null) {
				System.out.println(person.getName() + " is child of " + father.getName());
			}
		}

		Person2 oliver2 = new Person2(null, "Oliver", 74, Person2.Sex.Male);
		Person2 scott2 = new Person2(oliver2, "Scott", 47, Person2.Sex.Male);
		Stream.of(oliver2, scott2).forEach(p -> p.getFather().ifPresent(f -> System.out.println(p.getName() + " is child of " + f.getName())));
	}
	
	public static void main(String[] args) {
		optionals();
		Person[] peopleArray = {
				new Person(null, "Scott", 47, Sex.Male),
				new Person(null, "Steve", 46, Sex.Male),
				new Person(null, "Mary", 50, Sex.Female),
				new Person(null, "Claire", 17, Sex.Female),
				new Person(null, "Mia", 5, Sex.Female),
				new Person(null, "Tombo", 7, Sex.Male)
		};
		
		List<Person> people = list(peopleArray);
		Stream<Person> stream1 = people.stream();
		Stream<Person> stream2 = Stream.of(peopleArray);
		
		
		System.out.println("--------------------");
		people.stream().forEach(System.out::println);
		System.out.println("--------------------");
		people.stream().forEach(person -> System.out.println(person.getName()));
		System.out.println("--------------------");
		people.stream().sorted().forEach(person -> System.out.println(person.getName()));
		System.out.println("--------------------");
		people.stream().sorted((p1,p2) -> p1.getAge() - p2.getAge()).forEach(person -> System.out.println(person.getName()));
		
		if (people.stream().anyMatch(person -> person.getAge() == 47)) {
			System.out.println("MATCH 47!");
		}
		if (people.stream().anyMatch(person -> person.getAge() == 48)) {
			System.out.println("MATCH 48!");
		}
		if (people.stream().noneMatch(person -> person.getAge() == 47)) {
			System.out.println("NONE MATCH 47!");
		}
		if (people.stream().noneMatch(person -> person.getAge() == 48)) {
			System.out.println("NONE MATCH 48!");
		}
		if (people.stream().allMatch(person -> person.getName().length() > 3)) {
			System.out.println("ALL MATCH NAME > 3!");
		}
		if (people.stream().allMatch(person -> person.getName().length() > 2)) {
			System.out.println("ALL MATCH NAME > 2!");
		}
		System.out.println(people.stream().count());
		System.out.println("Male: " + people.stream().filter(person -> person.getSex() == Sex.Male).count());
		System.out.println("Female: " + people.stream().filter(person -> person.getSex() == Sex.Female).count());
		
		System.out.println("Average age: " + people.stream().mapToInt(person -> person.getAge()).average().getAsDouble());
		System.out.println("Average age: " + people.stream().mapToInt(person -> person.getAge()).average().orElse(-1));
		System.out.println("Average age: " + people.stream().mapToInt(person -> person.getAge()).average().orElseThrow(()->{throw new RuntimeException("Average doesn't make sense for an empty list");}));
		people.stream().mapToInt(person -> person.getAge()).average().ifPresent(avg -> System.out.println("Average age: " + avg));// ()->{throw new RuntimeException("Average doesn't make sense for an empty list");}));

		System.out.println("First: " + people.stream().findFirst().get().getName());
		System.out.println("Third: " + people.stream().skip(2).findFirst().get().getName());
		System.out.println("Oldest age: " + people.stream().mapToInt(Person::getAge).max().getAsInt());
		IntSummaryStatistics summaryStatistics = people.stream().mapToInt(Person::getAge).summaryStatistics();
		System.out.println("Average age: " + summaryStatistics.getAverage());
		System.out.println("Count: " + summaryStatistics.getCount());
		System.out.println("Max age: " + summaryStatistics.getMax());
		System.out.println("Min age: " + summaryStatistics.getMin());
		System.out.println("Sum age: " + summaryStatistics.getSum());

		List<Person> firstThree = people.stream().limit(3).collect(Collectors.toList());
		Map<String, Person> peopleByName1 = people.stream().collect(Collectors.toMap(p -> p.getName(), p -> p));
		peopleByName1.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
		Map<String, Person> peopleByName2 = people.stream().collect(Collectors.toMap(Person::getName, Function.identity()));
		
		Map<Sex, List<Person>> peopleBySex = people.stream().collect(Collectors.groupingBy(Person::getSex));
		peopleBySex.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ": " + e.getValue().stream().map(Person::getName).reduce((s1,s2) -> s1 + ", " + s2).get()));
		
		
		
		// parallel stuff - totals
		long start = System.currentTimeMillis();
		System.out.println(people.stream().mapToInt(p -> {sleep(); return p.getAge();} ).sum());
		long time = System.currentTimeMillis() - start;
		System.out.println("non-parallel w/ sleep(500): " + time + "ms");
		start = System.currentTimeMillis();
		System.out.println(people.parallelStream().mapToInt(p -> {sleep(); return p.getAge();} ).sum());
		time = System.currentTimeMillis() - start;
		System.out.println("parallel w/ sleep(500): " + time + "ms");
	}
	private static void sleep() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
