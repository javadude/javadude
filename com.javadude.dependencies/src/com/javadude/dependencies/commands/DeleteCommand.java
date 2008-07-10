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
package com.javadude.dependencies.commands;

import java.util.ArrayList;
import java.util.List;

import com.javadude.annotation.Bean;
import com.javadude.annotation.Property;
import com.javadude.dependencies.Dependency;

import org.eclipse.gef.commands.Command;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaModelException;

@Bean(superclass=Command.class,
      properties={
		@Property(name="dependency", type=Dependency.class),
		@Property(name="deletedEntry", type=IClasspathEntry.class),
		@Property(name="deletedPosition", type=int.class),
})
public class DeleteCommand extends DeleteCommandGen {

    public DeleteCommand(Dependency dependency) {
        setDependency(dependency);
    }

    @Override
    public void execute() {
        // delete the target dep from the project
        try {
            IClasspathEntry[] rawClasspath = getDependency().getSource().getRawClasspath();
            List<IClasspathEntry> newClasspath = new ArrayList<IClasspathEntry>();
            for (IClasspathEntry entry : rawClasspath) {
                if (entry.getEntryKind() != IClasspathEntry.CPE_PROJECT ||
                    !entry.getPath().equals(getDependency().getTarget().getPath())) {
                    newClasspath.add(entry);
                } else {
                    setDeletedPosition(newClasspath.size());
                    setDeletedEntry(entry);
                }
            }
            rawClasspath = newClasspath.toArray(new IClasspathEntry[rawClasspath.length-1]);
            getDependency().getSource().setRawClasspath(rawClasspath, null);
        } catch (JavaModelException e) {
            throw new RuntimeException("Trouble deleting!", e);
        }
    }

    @Override
    public void undo() {
        // re-add the target dependency from the project
        try {
            IClasspathEntry[] rawClasspath = getDependency().getSource().getRawClasspath();
            IClasspathEntry[] newClasspath = new IClasspathEntry[rawClasspath.length + 1];
            System.arraycopy(rawClasspath, 0, newClasspath, 0, getDeletedPosition());
            newClasspath[getDeletedPosition()] = getDeletedEntry();
            System.arraycopy(rawClasspath, getDeletedPosition(), newClasspath, getDeletedPosition() + 1, rawClasspath.length - getDeletedPosition());
            getDependency().getSource().setRawClasspath(newClasspath, null);
        } catch (JavaModelException e) {
            throw new RuntimeException("Trouble deleting!", e);
        }
    }
}
