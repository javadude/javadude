package com.javadude.java8;

import com.google.common.base.Objects;
import com.javadude.java8.Person;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class XSample {
  public static <K extends Object, V extends Object> Map<K, List<V>> groupBy(final Iterable<? extends V> list, final Function1<? super V, ? extends K> indexFn) {
    HashMap<K, List<V>> _xblockexpression = null;
    {
      final HashMap<K, List<V>> map = new HashMap<K, List<V>>();
      final Consumer<V> _function = (V it) -> {
        final K index = indexFn.apply(it);
        boolean _containsKey = map.containsKey(index);
        if (_containsKey) {
          final List<V> values = map.get(index);
          values.add(it);
        } else {
          final LinkedList<V> values_1 = CollectionLiterals.<V>newLinkedList(it);
          map.put(index, values_1);
        }
      };
      list.forEach(_function);
      _xblockexpression = map;
    }
    return _xblockexpression;
  }
  
  public static void main(final String[] args) {
    Person _person = new Person(null, "Scott", 47, Person.Sex.Male);
    Person _person_1 = new Person(null, "Steve", 46, Person.Sex.Male);
    Person _person_2 = new Person(null, "Mary", 50, Person.Sex.Female);
    Person _person_3 = new Person(null, "Claire", 17, Person.Sex.Female);
    Person _person_4 = new Person(null, "Mia", 5, Person.Sex.Female);
    Person _person_5 = new Person(null, "Tombo", 7, Person.Sex.Male);
    final ArrayList<Person> people = CollectionLiterals.<Person>newArrayList(_person, _person_1, _person_2, _person_3, _person_4, _person_5);
    InputOutput.<String>println("--------------------");
    final Consumer<Person> _function = (Person it) -> {
      InputOutput.<Person>println(it);
    };
    people.forEach(_function);
    InputOutput.<String>println("--------------------");
    final Consumer<Person> _function_1 = (Person it) -> {
      String _name = it.getName();
      InputOutput.<String>println(_name);
    };
    people.forEach(_function_1);
    InputOutput.<String>println("--------------------");
    List<Person> _sort = IterableExtensions.<Person>sort(people);
    final Consumer<Person> _function_2 = (Person it) -> {
      String _name = it.getName();
      InputOutput.<String>println(_name);
    };
    _sort.forEach(_function_2);
    InputOutput.<String>println("--------------------");
    final Function1<Person, Integer> _function_3 = (Person it) -> {
      return Integer.valueOf(it.getAge());
    };
    List<Person> _sortBy = IterableExtensions.<Person, Integer>sortBy(people, _function_3);
    final Consumer<Person> _function_4 = (Person it) -> {
      String _name = it.getName();
      InputOutput.<String>println(_name);
    };
    _sortBy.forEach(_function_4);
    final Function1<Person, Boolean> _function_5 = (Person it) -> {
      int _age = it.getAge();
      return Boolean.valueOf((_age == 47));
    };
    boolean _exists = IterableExtensions.<Person>exists(people, _function_5);
    if (_exists) {
      System.out.println("MATCH 47!");
    }
    final Function1<Person, Boolean> _function_6 = (Person it) -> {
      int _age = it.getAge();
      return Boolean.valueOf((_age == 48));
    };
    boolean _exists_1 = IterableExtensions.<Person>exists(people, _function_6);
    if (_exists_1) {
      System.out.println("MATCH 48!");
    }
    final Function1<Person, Boolean> _function_7 = (Person it) -> {
      int _age = it.getAge();
      return Boolean.valueOf((_age == 47));
    };
    boolean _exists_2 = IterableExtensions.<Person>exists(people, _function_7);
    boolean _not = (!_exists_2);
    if (_not) {
      System.out.println("NONE MATCH 47!");
    }
    final Function1<Person, Boolean> _function_8 = (Person it) -> {
      int _age = it.getAge();
      return Boolean.valueOf((_age == 48));
    };
    boolean _exists_3 = IterableExtensions.<Person>exists(people, _function_8);
    boolean _not_1 = (!_exists_3);
    if (_not_1) {
      System.out.println("NONE MATCH 48!");
    }
    final Function1<Person, Boolean> _function_9 = (Person it) -> {
      String _name = it.getName();
      int _length = _name.length();
      return Boolean.valueOf((_length <= 3));
    };
    boolean _exists_4 = IterableExtensions.<Person>exists(people, _function_9);
    boolean _not_2 = (!_exists_4);
    if (_not_2) {
      System.out.println("ALL MATCH NAME > 3!");
    }
    final Function1<Person, Boolean> _function_10 = (Person it) -> {
      String _name = it.getName();
      int _length = _name.length();
      return Boolean.valueOf((_length <= 2));
    };
    boolean _exists_5 = IterableExtensions.<Person>exists(people, _function_10);
    boolean _not_3 = (!_exists_5);
    if (_not_3) {
      System.out.println("ALL MATCH NAME > 2!");
    }
    int _size = people.size();
    InputOutput.<Integer>println(Integer.valueOf(_size));
    final Function1<Person, Boolean> _function_11 = (Person it) -> {
      Person.Sex _sex = it.getSex();
      return Boolean.valueOf(Objects.equal(_sex, Person.Sex.Male));
    };
    Iterable<Person> _filter = IterableExtensions.<Person>filter(people, _function_11);
    int _size_1 = IterableExtensions.size(_filter);
    String _plus = ("Male: " + Integer.valueOf(_size_1));
    InputOutput.<String>println(_plus);
    final Function1<Person, Boolean> _function_12 = (Person it) -> {
      Person.Sex _sex = it.getSex();
      return Boolean.valueOf(Objects.equal(_sex, Person.Sex.Female));
    };
    Iterable<Person> _filter_1 = IterableExtensions.<Person>filter(people, _function_12);
    int _size_2 = IterableExtensions.size(_filter_1);
    String _plus_1 = ("Female: " + Integer.valueOf(_size_2));
    InputOutput.<String>println(_plus_1);
    final Function1<Person, Integer> _function_13 = (Person it) -> {
      return Integer.valueOf(it.getAge());
    };
    List<Integer> _map = ListExtensions.<Person, Integer>map(people, _function_13);
    final Function2<Integer, Integer, Integer> _function_14 = (Integer i, Integer j) -> {
      return Integer.valueOf(((i).intValue() + (j).intValue()));
    };
    Integer _reduce = IterableExtensions.<Integer>reduce(_map, _function_14);
    int _size_3 = people.size();
    int _divide = ((_reduce).intValue() / _size_3);
    String _plus_2 = ("Average age: " + Integer.valueOf(_divide));
    InputOutput.<String>println(_plus_2);
    Person _head = IterableExtensions.<Person>head(people);
    String _name = _head.getName();
    String _plus_3 = ("First: " + _name);
    InputOutput.<String>println(_plus_3);
    Iterable<Person> _tail = IterableExtensions.<Person>tail(people);
    Iterable<Person> _tail_1 = IterableExtensions.<Person>tail(_tail);
    Iterable<Person> _tail_2 = IterableExtensions.<Person>tail(_tail_1);
    Person _head_1 = IterableExtensions.<Person>head(_tail_2);
    String _name_1 = _head_1.getName();
    String _plus_4 = ("Third: " + _name_1);
    InputOutput.<String>println(_plus_4);
    final Function1<Person, Integer> _function_15 = (Person it) -> {
      return Integer.valueOf(it.getAge());
    };
    List<Integer> _map_1 = ListExtensions.<Person, Integer>map(people, _function_15);
    final Function2<Integer, Integer, Integer> _function_16 = (Integer i, Integer j) -> {
      Integer _xifexpression = null;
      boolean _greaterThan = (i.compareTo(j) > 0);
      if (_greaterThan) {
        _xifexpression = i;
      } else {
        _xifexpression = j;
      }
      return _xifexpression;
    };
    Integer _reduce_1 = IterableExtensions.<Integer>reduce(_map_1, _function_16);
    String _plus_5 = ("Oldest age: " + _reduce_1);
    InputOutput.<String>println(_plus_5);
    InputOutput.<String>println("--------------------");
    final Function1<Person, String> _function_17 = (Person it) -> {
      return it.getName();
    };
    final Map<String, Person> peopleByName1 = IterableExtensions.<String, Person>toMap(people, _function_17);
    Set<Map.Entry<String, Person>> _entrySet = peopleByName1.entrySet();
    final Consumer<Map.Entry<String, Person>> _function_18 = (Map.Entry<String, Person> it) -> {
      String _key = it.getKey();
      String _plus_6 = (_key + ": ");
      Person _value = it.getValue();
      String _plus_7 = (_plus_6 + _value);
      InputOutput.<String>println(_plus_7);
    };
    _entrySet.forEach(_function_18);
    InputOutput.<String>println("--------------------");
    final Function1<Person, Person.Sex> _function_19 = (Person it) -> {
      return it.getSex();
    };
    final Map<Person.Sex, List<Person>> peopleBySex = XSample.<Person.Sex, Person>groupBy(people, _function_19);
    Set<Map.Entry<Person.Sex, List<Person>>> _entrySet_1 = peopleBySex.entrySet();
    final Consumer<Map.Entry<Person.Sex, List<Person>>> _function_20 = (Map.Entry<Person.Sex, List<Person>> it) -> {
      Person.Sex _key = it.getKey();
      String _plus_6 = (_key + ": ");
      List<Person> _value = it.getValue();
      final Function1<Person, String> _function_21 = (Person it_1) -> {
        return it_1.getName();
      };
      List<String> _map_2 = ListExtensions.<Person, String>map(_value, _function_21);
      final Function2<String, String, String> _function_22 = (String n1, String n2) -> {
        return ((n1 + ", ") + n2);
      };
      String _reduce_2 = IterableExtensions.<String>reduce(_map_2, _function_22);
      String _plus_7 = (_plus_6 + _reduce_2);
      InputOutput.<String>println(_plus_7);
    };
    _entrySet_1.forEach(_function_20);
  }
}
