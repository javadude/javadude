package com.javadude.annotation.rt;

import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Iterator;

public class ChangeReportingCollection<E> implements Collection<E> {
	private Collection<E> collection;
	private final String propertyName;
	private final PropertyChangeSupport pcs;

	public ChangeReportingCollection(Collection<E> collection, String propertyName, PropertyChangeSupport pcs) {
		this.collection = collection;
		this.propertyName = propertyName;
		this.pcs = pcs;
	}
	@Override
	public boolean add(E o) {
		boolean result = collection.add(o);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = collection.addAll(c);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}
	@Override
	public void clear() {
		collection.clear();
		pcs.firePropertyChange(propertyName, null, null);
	}
	@Override
	public boolean contains(Object o) {
		return collection.contains(o);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return collection.containsAll(c);
	}
	@Override public boolean equals(Object o) {
		return collection.equals(o);
	}
	@Override public int hashCode() {
		return collection.hashCode();
	}
	@Override
	public boolean isEmpty() {
		return collection.isEmpty();
	}
	@Override
	public Iterator<E> iterator() {
		return new ChangeReportingIterator<E>(collection.iterator(), propertyName, pcs);
	}
	@Override
	public boolean remove(Object o) {
		boolean result = collection.remove(o);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = collection.removeAll(c);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean result = collection.retainAll(c);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}
	@Override
	public int size() {
		return collection.size();
	}
	@Override
	public Object[] toArray() {
		return collection.toArray();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return collection.toArray(a);
	}
}
