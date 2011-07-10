package com.javadude.line.model;

import java.util.List;

public class Line {
	private int number;
	private Line next;
	private Line previous;
	private String act;
	private String line;
	private int page;
	private List<String> originalRoleNames;
	private List<Role> roles;
	public Line(int number, String act, Line previous, int page, String line, List<String> originalRoleNames, List<Role> roles) {
		this.number = number;
		this.previous = previous;
		this.originalRoleNames = originalRoleNames;
		if (previous != null) {
			previous.next = this;
		}
		this.act = act;
		this.page = page;
		this.line = line;
		this.roles = roles;
		for(Role role : roles) {
			role.add(this);
		}
	}
	
	public List<String> getOriginalRoleNames() { return originalRoleNames; }
	public Line getNext() { return next; }
	public void setNext(Line next) { this.next = next; }
	public Line getPrevious() { return previous; }
	public List<Role> getRoles() { return roles; }
	public String getLine() { return line; }
	public int getPage() { return page; }
	public int getNumber() { return number; }
	public String getAct() { return act; }
}
