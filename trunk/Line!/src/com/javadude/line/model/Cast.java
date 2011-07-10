package com.javadude.line.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cast {
	private final Map<String, Role> roles;
	private final Map<String, List<Role>> singletons;
	private final Map<String, List<String>> singletonNames;
	private final List<Role> all;
	private final List<Role> roleList;
	private final List<Role> STAGE_DIRECTION = Collections.singletonList(new Role("(STAGE)"));
	private final List<String> STAGE_DIRECTION_NAME = Collections.singletonList("(STAGE)");
	private final List<String> ALL_THE_OTHERS = Collections.singletonList("ALL THE OTHERS");

	public Cast(List<Role> roles) {
		all = Collections.unmodifiableList(roles);
		this.roles = new HashMap<String, Role>(roles.size());
		this.singletons = new HashMap<String, List<Role>>(roles.size());
		this.singletonNames = new HashMap<String, List<String>>(roles.size());
		List<Role> roleList = new ArrayList<Role>(roles);
		Collections.sort(roleList, new Comparator<Role>() {
			@Override public int compare(Role r1, Role r2) {
				return r1.getName().compareTo(r2.getName());
			}});
		this.roleList = Collections.unmodifiableList(roleList);
		for (Role role : roles) {
			this.roles.put(role.getName(), role);
			this.singletons.put(role.getName(), Collections.singletonList(role));
			this.singletonNames.put(role.getName(), Collections.singletonList(role.getName()));
		}
		this.singletonNames.put("ALL", Collections.singletonList("ALL"));
	}
	public List<String> getAllTheOthersName() {
		return ALL_THE_OTHERS;
	}
	public List<Role> getStageDirection() {
		return STAGE_DIRECTION;
	}
	public List<String> getStageDirectionName() {
		return STAGE_DIRECTION_NAME;
	}
	public List<Role> getRoles(String name) {
		// split the string into separate roles
		if ("ALL".equals(name))
			return all;

		int space = name.indexOf(' ');
		int comma = name.indexOf(',');
		int amp = name.indexOf('&');
		
		if (space != -1 || comma != -1 || amp != -1) {
			List<Role> roles = new ArrayList<Role>();
			// break the name up
			for (String n : name.split("[ ,&]+")) {
				Role role = this.roles.get(n);
				if (role == null)
					throw new IllegalArgumentException("Role " + n + " was not defined");
				roles.add(role);
			}
			return roles;
		}

		if (name.startsWith("^")){
			List<Role> roles = new ArrayList<Role>();
			name = name.substring(1);
			for (Role role : all) {
				if (!role.getName().equals(name))
					roles.add(role);
			}
			return roles;
		}
		
		return this.singletons.get(name);
	}
	public List<String> getOriginalRoleNames(String name) {
		// split the string into separate roles
		int space = name.indexOf(' ');
		int comma = name.indexOf(',');
		int amp = name.indexOf('&');
		
		if (space != -1 || comma != -1 || amp != -1) {
			List<String> roles = new ArrayList<String>();
			// break the name up
			for (String n : name.split("[ ,&]+")) {
				roles.add(n);
			}
			return roles;
		}
		
		if (name.startsWith("^")){
			List<String> roles = new ArrayList<String>();
			name = name.substring(1);
			for (Role role : all) {
				if (!role.getName().equals(name))
					roles.add(role.getName());
			}
			return roles;
		}

		return singletonNames.get(name);
	}
	public List<Role> getRoles() {
		return roleList;
	}
	
}