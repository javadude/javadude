package com.javadude.line.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Role {
	private String name;
	private List<Line> lines = new ArrayList<Line>();

	public Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void add(Line line) {
		lines.add(line);
	}
	public List<Line> getLines() {
		return Collections.unmodifiableList(lines);
	}
}