package com.javadude.annotation.processors;

import java.util.Map;

public class BasePushable implements Pushable {
	@Override
	public Map<String, Object> createPropertyMap() {
		return new NullHoldingMap();
	}
}
