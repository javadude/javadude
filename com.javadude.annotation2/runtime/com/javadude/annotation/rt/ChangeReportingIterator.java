/**
 * 
 */
package com.javadude.annotation.rt;

import java.beans.PropertyChangeSupport;
import java.util.Iterator;

public final class ChangeReportingIterator<E> implements Iterator<E> {
	private final Iterator<E> baseIterator;
	private final PropertyChangeSupport pcs;
	private final String propertyName;
	
	public ChangeReportingIterator(Iterator<E> baseIterator, String propertyName, PropertyChangeSupport pcs) {
		this.baseIterator = baseIterator;
		this.propertyName = propertyName;
		this.pcs = pcs;
	}

	public boolean hasNext() {
		return baseIterator.hasNext();
	}

	public E next() {
		return baseIterator.next();
	}

	public void remove() {
		baseIterator.remove();
		pcs.firePropertyChange(propertyName, null, null);
	}
}