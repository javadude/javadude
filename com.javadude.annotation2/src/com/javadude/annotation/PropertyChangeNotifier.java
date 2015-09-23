package com.javadude.annotation;

import java.beans.PropertyChangeListener;

public interface PropertyChangeNotifier {
	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);
}
