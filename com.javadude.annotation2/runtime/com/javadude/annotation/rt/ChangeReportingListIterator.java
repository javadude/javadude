/**
 * 
 */
package com.javadude.annotation.rt;

import java.beans.PropertyChangeSupport;
import java.util.ListIterator;

public final class ChangeReportingListIterator<E> implements ListIterator<E> {
	private final ListIterator<E> baseIterator;
	private final PropertyChangeSupport pcs;
	private final String propertyName;
	
	public ChangeReportingListIterator(ListIterator<E> baseIterator, String propertyName, PropertyChangeSupport pcs) {
		this.baseIterator = baseIterator;
		this.propertyName = propertyName;
		this.pcs = pcs;
	}

	@Override
	public void add(E o) {
		baseIterator.add(o);
		pcs.firePropertyChange(propertyName, null, null);
	}

	@Override
	public boolean hasNext() {
		return baseIterator.hasNext();
	}

	@Override
	public boolean hasPrevious() {
		return baseIterator.hasPrevious();
	}

	@Override
	public E next() {
		return baseIterator.next();
	}

	@Override
	public int nextIndex() {
		return baseIterator.nextIndex();
	}

	@Override
	public E previous() {
		return baseIterator.previous();
	}

	@Override
	public int previousIndex() {
		return baseIterator.previousIndex();
	}

	@Override
	public void remove() {
		baseIterator.remove();
		pcs.firePropertyChange(propertyName, null, null);
	}

	@Override
	public void set(E o) {
		baseIterator.set(o);
		pcs.firePropertyChange(propertyName, null, null);
	}
}