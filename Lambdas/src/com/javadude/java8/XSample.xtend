package com.javadude.java8

import java.util.Map
import com.javadude.java8.Person.Sex
import java.util.List
import java.util.HashMap

class XSample {
//	def foo(Map<String, String> map) {
//		
//	}
//	def foo() {
//		var map = newHashMap;
//		map.foo()
//		val stuff = newArrayList(1,2,3)
//		stuff.filter[it < 3].forEach[System.out.println(it)]
//	}

	def static <K, V> Map<K, List<V>> groupBy(Iterable<? extends V> list, (V)=>K indexFn) {
		val map = new HashMap<K, List<V>>
		list.forEach [
			val index = indexFn.apply(it)
			if (map.containsKey(index)) {
				val values = map.get(index)
				values.add(it)
			} else {
				val values = newLinkedList(it)
				map.put(index, values)
			}
		]
		map
	}

	def static void main(String[] args) {
		val people = newArrayList(
				new Person(null, "Scott", 47, Sex.Male),
				new Person(null, "Steve", 46, Sex.Male),
				new Person(null, "Mary", 50, Sex.Female),
				new Person(null, "Claire", 17, Sex.Female),
				new Person(null, "Mia", 5, Sex.Female),
				new Person(null, "Tombo", 7, Sex.Male)
			)
		
		println("--------------------")
		people.forEach[println(it)]
		println("--------------------")
		people.forEach[println(name)]
		println("--------------------")
		people.sort.forEach[println(name)]
		println("--------------------")
		people.sortBy[age].forEach[println(name)]
		
		if (people.exists[age == 47]) {
			System.out.println("MATCH 47!")
		}
		if (people.exists[age == 48]) {
			System.out.println("MATCH 48!")
		}
		if (!people.exists[age == 47]) {
			System.out.println("NONE MATCH 47!")
		}
		if (!people.exists[age == 48]) {
			System.out.println("NONE MATCH 48!");
		}
		if (!people.exists[name.length <= 3]) {
			System.out.println("ALL MATCH NAME > 3!");
		}
		if (!people.exists[name.length <= 2]) {
			System.out.println("ALL MATCH NAME > 2!");
		}
		println(people.size)
		println("Male: " + people.filter[sex == Sex.Male].size)
		println("Female: " + people.filter[sex == Sex.Female].size)
		
		println("Average age: " + people.map[age].reduce[i,j | i + j] / people.size)
		println("First: " + people.head.name)
		println("Third: " + people.tail.tail.tail.head.name)
		println("Oldest age: " + people.map[age].reduce[i,j | if (i > j) i else j])

		val firstThree = people.subList(0,2)
		println("--------------------")
		val peopleByName1 = people.toMap[name]
		peopleByName1.entrySet.forEach[println(key + ": " + value)]
		println("--------------------")
		
		val peopleBySex = people.groupBy[sex]
		peopleBySex.entrySet.forEach[println(key + ": " + value.map[name].reduce[n1,n2 | n1 + ", " + n2])]

	}
	
}