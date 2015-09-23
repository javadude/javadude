package com.javadude.annotation.generators.model;

import java.io.PrintWriter;

public class TargetParameter {
	private String name;
	private String type;
	private boolean isFinal;
	
	public boolean isFinal() {
		return isFinal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void generate(PrintWriter pw) {
		if (isFinal) {
			pw.print("final ");
		}
		pw.print(type);
		pw.print(' ');
		pw.print(name);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TargetParameter other = (TargetParameter) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
