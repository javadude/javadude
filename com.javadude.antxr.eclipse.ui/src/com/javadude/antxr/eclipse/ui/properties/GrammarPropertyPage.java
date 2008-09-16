/*******************************************************************************
 * Copyright (c) 2008 Scott Stanchfield, based on ANTLR-Eclipse plugin
 *   by Torsten Juergeleit.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors
 *    Torsten Juergeleit - original ANTLR Eclipse plugin
 *    Scott Stanchfield - modifications for ANTXR
 *******************************************************************************/
package com.javadude.antxr.eclipse.ui.properties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.javadude.antxr.eclipse.core.AntxrCorePlugin;
import com.javadude.antxr.eclipse.core.properties.SettingsPersister;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;

/**
 * Properties page for ANTXR grammar files.
 * It appends the name of the according grammar file to the file name.
 */
public class GrammarPropertyPage extends PropertyPage {
    // TODO cleanup the layout of this pref page!
    // TODO figure out how to force a rebuild when the user changes options!
    // TODO store prefs in an xml file called .antxr so they can be shared

    private static final String PREFIX = "Properties.Grammar.";
    private Map<String, Map<String, String>> map;
    private Text fOutputText;
    private Text fGrammarText;
    private Map<String, Button> booleanControls = new HashMap<String, Button>();
    private Map<String, Boolean> booleanDefaults = new HashMap<String, Boolean>();

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite aParent) {
        Composite composite = setupGridLayout(new Composite(aParent, SWT.NONE));

        // TODO remove the following if we can force a rebuild like jdt does when java options change
        Label note = new Label(composite, SWT.NONE);
        note.setText("Note: You must recompile your grammar for these changes to take place");

        // read the settings for this grammar
        map = SettingsPersister.readSettings(((IResource)getElement()).getProject());
        AntxrCorePlugin.getDefault().upgradeOldSettings((IResource)getElement(), map);

        addOutputProperty(composite);
        addGrammarProperty(composite);
        addBooleanProperty(SWT.CHECK, SettingsPersister.SMAP_PROPERTY, getString("smap.label"), composite, true);
        addBooleanProperty(SWT.CHECK, SettingsPersister.DEBUG_PROPERTY, getString("debug.label"), composite, false);

        Group outputType = (Group)setupGridLayout(new Group(composite, SWT.SHADOW_ETCHED_IN));
        outputType.setText(getString("output.types.label"));
        addBooleanProperty(SWT.RADIO, SettingsPersister.NORMAL_PROPERTY, getString("normal.label"), outputType, true);
        addBooleanProperty(SWT.RADIO, SettingsPersister.HTML_PROPERTY, getString("html.label"), outputType, false);
        addBooleanProperty(SWT.RADIO, SettingsPersister.DOCBOOK_PROPERTY, getString("docbook.label"), outputType, false);
        addBooleanProperty(SWT.RADIO, SettingsPersister.DIAGNOSTIC_PROPERTY, getString("diagnostic.label"), outputType, false);

        Group traceType = (Group)setupGridLayout(new Group(composite, SWT.SHADOW_ETCHED_IN));
        traceType.setText(getString("trace.types.label"));
        addBooleanProperty(SWT.RADIO, SettingsPersister.NOTRACE_PROPERTY, getString("notrace.label"), traceType, true);
        addBooleanProperty(SWT.RADIO, SettingsPersister.TRACE_PROPERTY, getString("trace.label"), traceType, false);
        addBooleanProperty(SWT.RADIO, SettingsPersister.TRACE_PARSER_PROPERTY, getString("traceParser.label"), traceType, false);
        addBooleanProperty(SWT.RADIO, SettingsPersister.TRACE_LEXER_PROPERTY, getString("traceLexer.label"), traceType, false);
        addBooleanProperty(SWT.RADIO, SettingsPersister.TRACE_TREE_PARSER_PROPERTY, getString("traceTreeParser.label"), traceType, false);

        return composite;
    }

    private Composite setupGridLayout(Composite composite) {
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        GridData data = new GridData(GridData.FILL);
        data.grabExcessHorizontalSpace = true;
        composite.setLayoutData(data);
        return composite;
    }
    private void addOutputProperty(Composite aParent) {
        Composite composite = createDefaultComposite(aParent);

        // Label for output field
        Label label = new Label(composite, SWT.NONE);
        label.setText(getString("output.label"));
//		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

        // Output text field
        fOutputText = new Text(composite, SWT.READ_ONLY | SWT.SINGLE | SWT.BORDER);
        String output = SettingsPersister.get(map, (IResource)getElement(), SettingsPersister.OUTPUT_PROPERTY);
        if (output != null) {
            fOutputText.setText(output);
        }
        GridData gd = new GridData();
        gd.horizontalAlignment= GridData.FILL_HORIZONTAL;
        gd.widthHint = convertWidthInCharsToPixels(50);
//		gd.grabExcessHorizontalSpace = true;
        fOutputText.setLayoutData(gd);

        // Choose folder button
        Button button = new Button(composite, SWT.PUSH);
        button.setText(getString("output.button"));
        button.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent anEvent) {
                chooseOutputFolder();
            }
            public void widgetSelected(SelectionEvent anEvent) {
                chooseOutputFolder();
            }
        });
    }

    private void addGrammarProperty(Composite aParent) {
        Composite composite = createDefaultComposite(aParent);

        // Label for grammar field
        Label label = new Label(composite, SWT.NONE);
        label.setText(getString("grammar.label"));

        // Grammar text field
        fGrammarText = new Text(composite, SWT.READ_ONLY | SWT.SINGLE | SWT.BORDER);
        String grammar;
        grammar = SettingsPersister.get(map, (IResource)getElement(), SettingsPersister.SUPER_GRAMMARS_PROPERTY);
        if (grammar != null) {
            fGrammarText.setText(grammar);
        }
        GridData gd = new GridData();
        gd.horizontalAlignment= GridData.FILL_HORIZONTAL;
        gd.widthHint = convertWidthInCharsToPixels(50);
//		gd.grabExcessHorizontalSpace = true;
        fGrammarText.setLayoutData(gd);

        // Choose folder button
        Button button = new Button(composite, SWT.PUSH);
        button.setText(getString("grammar.button"));
        button.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent anEvent) {
                chooseGrammarFile();
            }
            public void widgetSelected(SelectionEvent anEvent) {
                chooseGrammarFile();
            }
        });
    }

    private void addBooleanProperty(int type, final String propertyName, String label, Composite aParent, boolean defaultValue) {
        // Label for grammar field
        final Button button = new Button(aParent, type);
        booleanControls.put(propertyName, button);
        booleanDefaults.put(propertyName, Boolean.valueOf(defaultValue));
        button.setText(label);
        String value = SettingsPersister.get(map, (IResource)getElement(), propertyName);
        boolean booleanValue;
        if (value == null) {
            booleanValue = defaultValue;
            SettingsPersister.set(map, (IResource)getElement(), propertyName,defaultValue?"true":"false");
        } else {
	        booleanValue = ("true".equalsIgnoreCase(value));
        }

        button.setSelection(booleanValue);
    }

    private Composite createDefaultComposite(Composite aParent) {
        Composite composite = new Composite(aParent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);

        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        composite.setLayoutData(data);

        return composite;
    }

    /** {@inheritDoc} */
    public boolean performOk() {
        SettingsPersister.set(map, (IResource)getElement(), SettingsPersister.OUTPUT_PROPERTY, fOutputText.getText());
        SettingsPersister.set(map, (IResource)getElement(), SettingsPersister.SUPER_GRAMMARS_PROPERTY, fGrammarText.getText());

        for (Iterator i = booleanControls.keySet().iterator(); i.hasNext();) {
            String propertyName = (String)i.next();
            Button b = booleanControls.get(propertyName);
            SettingsPersister.set(map, (IResource)getElement(), propertyName, String.valueOf(b.getSelection()));
        }

        SettingsPersister.writeSettings(((IResource)getElement()).getProject(), map);
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults() {
        fOutputText.setText("");
        fGrammarText.setText("");
        for (Iterator i = booleanControls.keySet().iterator(); i.hasNext();) {
            QualifiedName propertyName = (QualifiedName)i.next();
            Button b = booleanControls.get(propertyName);
            Boolean defaultValue = booleanDefaults.get(propertyName);
            b.setSelection(defaultValue.booleanValue());
        }
    }

    private String getString(String aKey) {
        return AntxrUIPlugin.getMessage(GrammarPropertyPage.PREFIX + aKey);
    }

    private void chooseOutputFolder() {
        ISelectionStatusValidator validator = new ISelectionStatusValidator() {
            public IStatus validate(Object[] aSelection) {
                for (int i= 0; i < aSelection.length; i++) {
                    if (!(aSelection[i] instanceof IFolder ||
                                         aSelection[i] instanceof IProject)) {
                        return new Status(IStatus.ERROR,
                               AntxrUIPlugin.getUniqueIdentifier(), IStatus.OK,
                               getString("output.choose.select"), null);
                    }
                }
                return new Status(IStatus.OK,
                               AntxrUIPlugin.getUniqueIdentifier(), IStatus.OK,
                               "", null);
            }
        };
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
                                      getShell(), new WorkbenchLabelProvider(),
                                      new WorkbenchContentProvider());
        dialog.setValidator(validator);
        dialog.setTitle(getString("output.choose.title"));
        dialog.setMessage(getString("output.choose.message"));
        dialog.addFilter(new FolderFilter());
        dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
        dialog.setAllowMultiple(false);

        if (dialog.open() == Window.OK) {
            Object[] folders = dialog.getResult();
            String folder;
            if (folders.length > 0) {
                folder = ((IResource)folders[0]).getFullPath().toString();
            } else {
                folder = "";
            }
            fOutputText.setText(folder);
        }
    }

    private static class FolderFilter extends ViewerFilter {
        /** {@inheritDoc} */
        public boolean select(Viewer aViewer, Object aParent,
                                Object anElement) {
            return ((anElement instanceof IProject &&
                      ((IProject)anElement).isOpen()) ||
                     anElement instanceof IFolder);
        }
    }

    private void chooseGrammarFile() {
        ISelectionStatusValidator validator = new ISelectionStatusValidator() {
            public IStatus validate(Object[] aSelection) {
                for (int i= 0; i < aSelection.length; i++) {
                    if (!(aSelection[i] instanceof IFile)) {
                        return new Status(IStatus.ERROR,
                              AntxrUIPlugin.getUniqueIdentifier(), IStatus.OK,
                              getString("grammar.choose.select"), null);
                    }
                }
                return new Status(IStatus.OK,
                               AntxrUIPlugin.getUniqueIdentifier(), IStatus.OK,
                               "", null);
            }
        };
        ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
                                      getShell(), new WorkbenchLabelProvider(),
                                      new WorkbenchContentProvider());
        dialog.setValidator(validator);
        dialog.setTitle(getString("grammar.choose.title"));
        dialog.setMessage(getString("grammar.choose.message"));
        dialog.addFilter(new GrammarFileFilter());
        dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
        dialog.setAllowMultiple(true);

        if (dialog.open() == Window.OK) {
            Object[] files = dialog.getResult();

            // Create list of grammars delimited by ';'
            StringBuffer grammars = new StringBuffer();
            for (int i = 0; i < files.length; i++) {
                grammars.append(((IFile)files[i]).getFullPath().toString());
                if (i < (files.length - 1)) {
                    grammars.append(';');
                }
            }
            fGrammarText.setText(grammars.toString());
        }
    }

    private static class GrammarFileFilter extends ViewerFilter {
        /** {@inheritDoc} */
        public boolean select(Viewer aViewer, Object aParent,
                                Object anElement) {
            boolean select = false;
            if (anElement instanceof IProject ||
                     anElement instanceof IFolder) {
                select = true;
            } else if (anElement instanceof IFile) {
                String extension = ((IFile)anElement).getFileExtension();
                if (extension != null && extension.equals("g")) {
                    select = true;
                }
            }
            return select;
        }
    }
}