package com.javadude.listenerlist;

import java.util.Collections;
import java.util.List;

public class ListenerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private List<Throwable> causes;

	public ListenerException(List<Throwable> causes) {
		super("Error notifying listeners");
		this.causes = causes;
	}

	public List<Throwable> getCauses() {
		return Collections.unmodifiableList(causes);
	}

	// TODO add causes to stack trace
}
