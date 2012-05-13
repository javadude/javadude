package com.javadude.line.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Role {
	private String name;
	private Set<Integer> pagesThisRoleAppears = new HashSet<Integer>();
	private List<Line> lines = new ArrayList<Line>();

	public Role(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public Set<Integer> getPagesThisRoleAppears() {
		return pagesThisRoleAppears;
	}
	public void add(Line line) {
		lines.add(line);
		pagesThisRoleAppears.add(line.getPage());
	}
	public List<Line> getLines() {
		return Collections.unmodifiableList(lines);
	}
}