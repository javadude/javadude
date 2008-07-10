/*******************************************************************************
 *  Copyright 2008 Scott Stanchfield.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *******************************************************************************/
package com.javadude.dependencies.editparts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.javadude.dependencies.Dependency;

import org.eclipse.jdt.core.IJavaProject;

public class DependencyManager {
    private static Map<IJavaProject, Set<Dependency>> sourceDependencies = new HashMap<IJavaProject, Set<Dependency>>();
    private static Map<IJavaProject, Set<Dependency>> targetDependencies = new HashMap<IJavaProject, Set<Dependency>>();

    public static void add(Dependency dependency) {
        add(dependency.getSource(), dependency, sourceDependencies);
        add(dependency.getTarget(), dependency, targetDependencies);
    }

    public static boolean indirectPathExists(Dependency directPath, IJavaProject source, IJavaProject target) {
//        System.out.print("checking " + source.getElementName() + "->" + target.getElementName());
    	boolean result = indirectPathExists(new HashSet<IJavaProject>(), directPath, source, target);
//    	System.out.println(": " + result);
    	return result;
    }
   	private static boolean indirectPathExists(Set<IJavaProject> visited, Dependency directPath, IJavaProject source, IJavaProject target) {
    	if (visited.contains(source)) {
    		// CYCLE!!! stop walking or we're doomed!
    		return false;
    	}
    	visited.add(source);
    	Set<Dependency> sourceDeps = sourceDependencies.get(source);
    	if (sourceDeps != null) {
	    	for (Dependency dependency : sourceDeps) {
	    		if (!dependency.isExported() || dependency.equals(directPath)) {
		        	continue;
		        }
		        if (dependency.getTarget().equals(target)) {
		        	return true;
		        } else {
		        	// recurse to try to find an indirect path
		        	if (indirectPathExists(visited, directPath, dependency.getTarget(), target)) {
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
        return get(source, sourceDependencies);
    }
    public static List<Dependency> findTargetDependencies(IJavaProject target) {
        return get(target, targetDependencies);
    }
    private static List<Dependency> get(IJavaProject source, Map<IJavaProject, Set<Dependency>> map) {
        Set<Dependency> set = map.get(source);
        if (set == null || set.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<Dependency>(set);
    }

    public static void removeDependency(IJavaProject source, IJavaProject target) {
        remove(source, target, sourceDependencies);
        remove(target, source, targetDependencies);
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
        sourceDependencies.clear();
        targetDependencies.clear();
    }
}
