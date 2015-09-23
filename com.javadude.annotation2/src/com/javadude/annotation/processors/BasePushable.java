package com.javadude.annotation.processors;

import java.util.Map;

public class BasePushable implements Pushable {
	public Map<String, Object> createPropertyMap() {
		return new NullHoldingMap();
	}
}
