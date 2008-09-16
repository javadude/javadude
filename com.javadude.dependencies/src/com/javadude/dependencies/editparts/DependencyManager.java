/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.javadude.dependencies.editparts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IJavaProject;

import com.javadude.dependencies.Dependency;

public class DependencyManager {
    private static Map<IJavaProject, Set<Dependency>> sourceDependencies = new HashMap<IJavaProject, Set<Dependency>>();
    private static Map<IJavaProject, Set<Dependency>> targetDependencies = new HashMap<IJavaProject, Set<Dependency>>();

    public static void add(Dependency dependency) {
        DependencyManager.add(dependency.getSource(), dependency, DependencyManager.sourceDependencies);
        DependencyManager.add(dependency.getTarget(), dependency, DependencyManager.targetDependencies);
    }

    public static boolean indirectPathExists(Dependency directPath, IJavaProject source, IJavaProject target) {
//        System.out.print("checking " + source.getElementName() + "->" + target.getElementName());
    	boolean result = DependencyManager.indirectPathExists(new HashSet<IJavaProject>(), directPath, source, target);
//    	System.out.println(": " + result);
    	return result;
    }
   	private static boolean indirectPathExists(Set<IJavaProject> visited, Dependency directPath, IJavaProject source, IJavaProject target) {
    	if (visited.contains(source)) {
    		// CYCLE!!! stop walking or we're doomed!
    		return false;
    	}
    	visited.add(source);
    	Set<Dependency> sourceDeps = DependencyManager.sourceDependencies.get(source);
    	if (sourceDeps != null) {
	    	for (Dependency dependency : sourceDeps) {
	    		if (!dependency.isExported() || dependency.equals(directPath)) {
		        	continue;
		        }
		        if (dependency.getTarget().equals(target)) {
		        	return true;
		        } else {
		        	// recurse to try to find an indirect path
		        	if (DependencyManager.indirectPathExists(visited, directPath, dependency.getTarget(), target)) {
		        		return true;
		        	}
		        }
	        }
    	}
    	return false;
    }
    private static void add(IJavaProject key, Dependency dependency, Map<IJavaProject, Set<Dependency>> map) {
        Set<Dependency> set = map.get(key);
        if (set == null) {
            set = new HashSet<Dependency>();
            map.put(key, set);
            set.add(dependency);
        } else if (!set.contains(dependency)) {
            set.add(dependency);
        }
    }

    public static List<Dependency> findSourceDependencies(IJavaProject source) {
        return DependencyManager.get(source, DependencyManager.sourceDependencies);
    }
    public static List<Dependency> findTargetDependencies(IJavaProject target) {
        return DependencyManager.get(target, DependencyManager.targetDependencies);
    }
    private static List<Dependency> get(IJavaProject source, Map<IJavaProject, Set<Dependency>> map) {
        Set<Dependency> set = map.get(source);
        if (set == null || set.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<Dependency>(set);
    }

    public static void removeDependency(IJavaProject source, IJavaProject target) {
        DependencyManager.remove(source, target, DependencyManager.sourceDependencies);
        DependencyManager.remove(target, source, DependencyManager.targetDependencies);
    }

    private static void remove(IJavaProject key, IJavaProject value, Map<IJavaProject, Set<Dependency>> map) {
        Set<Dependency> set = map.get(key);
        if (set == null) {
            return;
        }
        set.remove(value);
        if (set.isEmpty()) {
            map.remove(key);
        }
    }
    public static void clear() {
        DependencyManager.sourceDependencies.clear();
        DependencyManager.targetDependencies.clear();
    }
}
