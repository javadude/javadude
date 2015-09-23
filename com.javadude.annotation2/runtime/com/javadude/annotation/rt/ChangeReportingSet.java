package com.javadude.annotation.rt;

import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ChangeReportingSet<E> implements Set<E> {
	private final Set<E> set;
	private final String propertyName;
	private final PropertyChangeSupport pcs;
	
	public ChangeReportingSet(Set<E> set, String propertyName, PropertyChangeSupport pcs) {
		this.set = set;
		this.propertyName = propertyName;
		this.pcs = pcs;
	}
	@Override
	public boolean add(E o) {
		boolean result = set.add(o);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = set.addAll(c);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}
	@Override
	public void clear() {
		set.clear();
		pcs.firePropertyChange(propertyName, null, null);
	}
	@Override
	public Iterator<E> iterator() {
		return new ChangeReportingIterator<E>(set.iterator(), propertyName, pcs);
	}
	@Override
	public boolean remove(Object o) {
		boolean result = set.remove(o);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = set.removeAll(c);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean result = set.retainAll(c);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}

	
	@Override
	public boolean contains(Object o) {
		return set.contains(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return set.containsAll(c);
	}
	@Override public boolean equals(Object o) {
		return set.equals(o);
	}
	@Override public int hashCode() {
		return set.hashCode();
	}
	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}
	@Override
	public int size() {
		return set.size();
	}
	@Override
	public Object[] toArray() {
		return set.toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return set.toArray(a);
	}
}
