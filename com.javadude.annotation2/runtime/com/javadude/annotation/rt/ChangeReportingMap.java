package com.javadude.annotation.rt;

import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ChangeReportingMap<K, V> implements Map<K, V> {
	private Map<K, V> map;
	private final String propertyName;
	private final PropertyChangeSupport pcs;

	public ChangeReportingMap(Map<K, V> map, String propertyName, PropertyChangeSupport pcs) {
		this.map = map;
		this.propertyName = propertyName;
		this.pcs = pcs;
	}

	@Override
	public void clear() {
		map.clear();
		pcs.firePropertyChange(propertyName, null, null);
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
		// TODO WHAT TO REPORT HERE? CAN ENTRIES BE CHANGED?
	}

	@Override public boolean equals(Object o) {
		return map.equals(o);
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override public int hashCode() {
		return map.hashCode();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return new ChangeReportingSet<K>(map.keySet(), propertyName, pcs);
	}

	@Override
	public V put(K key, V value) {
		V result = map.put(key, value);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> t) {
		map.putAll(t);
		pcs.firePropertyChange(propertyName, null, null);
	}

	@Override
	public V remove(Object key) {
		V result = map.remove(key);
		pcs.firePropertyChange(propertyName, null, null);
		return result;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<V> values() {
		return new ChangeReportingCollection<V>(map.values(), propertyName, pcs);
	}
}
