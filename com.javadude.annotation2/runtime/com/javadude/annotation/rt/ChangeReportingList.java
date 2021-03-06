package com.javadude.annotation.rt;

import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// TODO add propagation support
public class ChangeReportingList<E> implements List<E> {
	private final List<E> list;
	private final String propertyName;
	private final PropertyChangeSupport pcs;
	
	public ChangeReportingList(List<E> list, String propertyName, PropertyChangeSupport pcs) {
		this.list = list;
		this.propertyName = propertyName;
		this.pcs = pcs;
	}

	// methods that require property change reporting
	@Override
	public boolean add(E o) {
		boolean result = list.add(o);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}

	@Override
	public void add(int index, E element) {
		list.add(index, element);
		pcs.firePropertyChange(propertyName, null, null);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean result = list.addAll(c);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean result = list.addAll(index, c);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}

	@Override
	public void clear() {
		list.clear();
		pcs.firePropertyChange(propertyName, null, null);
	}
	
	@Override
	public Iterator<E> iterator() {
		return new ChangeReportingIterator<E>(list.iterator(), propertyName, pcs);
	}
	
	@Override
	public ListIterator<E> listIterator() {
		return new ChangeReportingListIterator<E>(list.listIterator(), propertyName, pcs);
	}
	
	@Override
	public ListIterator<E> listIterator(int index) {
		return new ChangeReportingListIterator<E>(list.listIterator(index), propertyName, pcs);
	}
	
	@Override
	public E remove(int index) {
		E result = list.remove(index);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}
	
	@Override
	public boolean remove(Object o) {
		boolean result = list.remove(o);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean result = list.removeAll(c);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean result = list.retainAll(c);
		if (result) {
			pcs.firePropertyChange(propertyName, null, null);
		}
		return result;
	}
	
	@Override
	public E set(int index, E element) {
		E result = list.set(index, element);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}
	
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return new ChangeReportingList<E>(list.subList(fromIndex, toIndex), propertyName, pcs);
	}
	
	
	
	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override public boolean equals(Object o) {
		return list.equals(o);
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override public int hashCode() {
		return list.hashCode();
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}
	
}
