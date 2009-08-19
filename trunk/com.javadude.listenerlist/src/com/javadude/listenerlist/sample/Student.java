/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.listenerlist.sample;

import java.util.Date;

public class Student implements SunListener {
	private String name;

	public Student(String name) {
		this.name = name;
	}
	@Override public void sunRose(Date date) {
		System.out.println(name + ": sun rose at " + date);
	}
	@Override public void sunSet(Date date) {
		System.out.println(name + ": sun set at " + date);
	}
}
