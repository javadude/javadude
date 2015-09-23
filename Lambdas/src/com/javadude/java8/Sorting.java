package com.javadude.java8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sorting {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("Scott");
		list.add("Claire");
		list.add("Trevor");
		list.add("Alex");
		list.add("Nicole");
//		Collections.sort(list, new Comparator<String>() {
//			@Override public int compare(String o1, String o2) {
//				return o2.compareTo(o1);
//			}});
		Collections.sort(list, (s1, s2) -> s2.compareTo(s1) );
		for (String name : list) {
			System.out.println(name);
		}
		
		list.stream().filter(name -> name.length() <=5).forEach(System.out::println);
	}
}
