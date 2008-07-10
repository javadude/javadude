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
package com.javadude.antxr.eclipse.core.builder;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.javadude.antxr.eclipse.core.AntxrCorePlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

/**
 * Builder that removes warning from derived resources.
 *
 * @author <a href="mailto:Scott.Stanchfield@hcrest.com">Scott Stanchfield</a>
 * @version 1.0
 */
public class WarningCleanerBuilder extends IncrementalProjectBuilder {
    /** the builder id for the smap installer */
    public static final String BUILDER_ID = "com.javadude.antxr.eclipse.core.warningcleanerbuilder";

    private Map sourceLineCharMappings = new HashMap();

    /** {@inheritDoc} */
    protected IProject[] build(int aKind, Map anArgs,
                             IProgressMonitor aMonitor) throws CoreException {
        try {
        IResourceDelta delta = (aKind != FULL_BUILD ? getDelta(getProject()) :
                                                      null);
        if (delta == null || aKind == FULL_BUILD) {
            IProject project = getProject();
            project.accept(new Visitor(aMonitor));
        } else {
            delta.accept(new DeltaVisitor(aMonitor));
        }
        } finally {
            sourceLineCharMappings.clear();
        }
        return null;
       }

    @SuppressWarnings("unchecked")
    private void removeWarningMarkers(IResource resource) {
        if (!resource.isDerived()) {
	        return;
        }
        try {
            String currentGrammarFileName = resource.getPersistentProperty(AntxrBuilder.GRAMMAR_ECLIPSE_PROPERTY);
            if (currentGrammarFileName == null) {
	            return;
            }

            String cleanWarningsString = resource.getPersistentProperty(AntxrBuilder.CLEAN_WARNINGS);
            boolean cleanWarnings = ("true".equalsIgnoreCase(cleanWarningsString));


            // get the source line mappings from the generated file

            Map<Integer, Integer> sourceMap = new HashMap<Integer, Integer>();
            Integer highestLine = new Integer(0);
            IMarker[] sourceMappingMarkers = resource.findMarkers(AntxrCorePlugin.SOURCE_MAPPING_MARKER, false, IResource.DEPTH_ZERO);
            for (int i = 0; i < sourceMappingMarkers.length; i++) {
                Integer sourceLine = (Integer) sourceMappingMarkers[i].getAttribute(AntxrCorePlugin.GRAMMAR_LINE_ATTRIBUTE);
                Integer targetLine = (Integer) sourceMappingMarkers[i].getAttribute(AntxrCorePlugin.GENERATED_LINE_ATTRIBUTE);
                sourceMap.put(targetLine, sourceLine);
                if (highestLine.compareTo(sourceLine) < 0) {
	                highestLine = sourceLine;
                }
            }

            IFile grammarFile = resource.getProject().getFile(new Path(currentGrammarFileName));
            int[] grammarFirstChar = (int[]) sourceLineCharMappings.get(currentGrammarFileName);
            if (grammarFirstChar == null) {
                grammarFirstChar = getFirstCharMapping(highestLine, grammarFile.getContents(true));
            }
            int[] generatedFirstChar = getFirstCharMapping(new Integer(1000), ((IFile) resource).getContents(true));

            Integer zero = new Integer(0);
            IMarker[] markers = resource.findMarkers("org.eclipse.jdt.core.problem", false, IResource.DEPTH_ZERO);
            for (int i = 0; i < markers.length; i++) {
                Integer severity = (Integer) markers[i].getAttribute(IMarker.SEVERITY);
                if (!sourceMap.isEmpty()) {
                    if (severity != null && severity.intValue() == IMarker.SEVERITY_ERROR) {
                        IMarker markerCopy = grammarFile.createMarker(markers[i].getType());
                        Map attributes = new HashMap(markers[i].getAttributes());
                        markerCopy.setAttributes(attributes);
                        Integer targetLine = (Integer) markerCopy.getAttribute("lineNumber");
                        Integer charEnd = (Integer) markerCopy.getAttribute("charEnd");
                        Integer charStart = (Integer) markerCopy.getAttribute("charStart");
                        int targetFirstChar = generatedFirstChar[targetLine.intValue()];
                        int startOffset = charStart.intValue() - targetFirstChar;
                        int endOffset = charEnd.intValue() - targetFirstChar;

                        Integer sourceLine = sourceMap.get(targetLine);
                        if (sourceLine == null) {
	                        sourceLine = zero;
                        }
                        markerCopy.setAttribute("lineNumber", sourceLine);
                        int sourceChar = grammarFirstChar[sourceLine.intValue()];
                        markerCopy.setAttribute("charEnd", new Integer(sourceChar + startOffset));
                        markerCopy.setAttribute("charStart", new Integer(sourceChar + endOffset));
                    }
                }
//                if (cleanWarnings) {
//                if (severity != null && severity.intValue() == IMarker.SEVERITY_WARNING) {
//                    markers[i].delete();
//                }
//            }
            }
        } catch (CoreException e) {
            AntxrCorePlugin.log(e);
        }
    }

    private int[] getFirstCharMapping(Integer highestLine, InputStream contents) {
        int[] firstChar = new int[highestLine.intValue()];

        // read the grammar file and create a map of lines to character positions
        BufferedInputStream bi = new BufferedInputStream(contents);
        try {
            int lineNumber = 1;
            int charNumber = 0;
            int c;
            int lastChar = -1;
            firstChar = ensureCapacity(firstChar, 1);
            firstChar[1] = 1;
            boolean seenNonSpaceForThisLine = false;
            while((c = bi.read()) != -1) {
                charNumber++;
                if (lastChar == '\r') {
                    if (c != '\n') {
                        lineNumber++;
                        firstChar = ensureCapacity(firstChar, lineNumber);
                        firstChar[lineNumber] = charNumber;
                        seenNonSpaceForThisLine = false;
                    }
                }
                if (c == '\n') {
                    lineNumber++;
                    firstChar = ensureCapacity(firstChar, lineNumber);
                    firstChar[lineNumber] = charNumber + 1;
                    seenNonSpaceForThisLine = false;
                }
                if (!seenNonSpaceForThisLine && !Character.isWhitespace((char) c)) {
                    firstChar[lineNumber] = charNumber;
                    seenNonSpaceForThisLine = true;
                }
                lastChar = c;
            }
            bi.close();
        }
        catch (IOException ignoreMe) { /* do nothing */ }
        return firstChar;
    }

    private int[] ensureCapacity(int[] data, int index) {
        if (index >= data.length) {
            int[] newData = new int[data.length + 10];
            System.arraycopy(data, 0, newData, 0, data.length);
            data = newData;
        }
        return data;
    }
    private class Visitor implements IResourceVisitor {
        /**
         * Create a full-build visitor
         * @param aMonitor a progress monitor
         */
        public Visitor(IProgressMonitor aMonitor) {
            // do nothing
        }

        /** {@inheritDoc} */
        public boolean visit(IResource aResource) {
            removeWarningMarkers(aResource);
            return true;
        }
    }

    private class DeltaVisitor implements IResourceDeltaVisitor {
        /**
         * Create a delta visitor
         * @param aMonitor A progress monitor
         */
        public DeltaVisitor(IProgressMonitor aMonitor) {
            // do nothing
        }

        /** {@inheritDoc} */
        public boolean visit(IResourceDelta aDelta) {
            removeWarningMarkers(aDelta.getResource());
            return true;
        }
    }
}
