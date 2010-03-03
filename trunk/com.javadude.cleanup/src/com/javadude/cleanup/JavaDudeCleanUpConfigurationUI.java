package com.javadude.cleanup;

import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpConfigurationUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class JavaDudeCleanUpConfigurationUI implements
		ICleanUpConfigurationUI {

	private CleanUpOptions options;

	public JavaDudeCleanUpConfigurationUI() {
	}

	public Composite createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 3;
		final Button convertButton = new Button(composite, SWT.CHECK);
		convertButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 3, 1));
		convertButton.setText("Convert Tabs to Spaces");
		convertButton.setSelection(options.isEnabled(JavaDudeCleanUpOptionsInitializer.CONVERT_TABS_TO_SPACES));
		convertButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				options.setOption(JavaDudeCleanUpOptionsInitializer.CONVERT_TABS_TO_SPACES, convertButton.getSelection() ? CleanUpOptions.TRUE : CleanUpOptions.FALSE);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				options.setOption(JavaDudeCleanUpOptionsInitializer.CONVERT_TABS_TO_SPACES, convertButton.getSelection() ? CleanUpOptions.TRUE : CleanUpOptions.FALSE);
			}});
		Label label = new Label(composite, SWT.NONE);
		label.setText("      ");
		label = new Label(composite, SWT.NONE);
		label.setText("Tab Size: ");
		final Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		text.setText(options.getValue(JavaDudeCleanUpOptionsInitializer.TAB_SIZE));
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				options.setOption(JavaDudeCleanUpOptionsInitializer.TAB_SIZE, text.getText());
			}});

		return composite;
	}

	public int getCleanUpCount() {
		return 1;
	}

	public String getPreview() {
		return "TBD\nTBD\nTBD\nTBD\nTBD\nTBD\nTBD\nTBD\nTBD\nTBD\n";
	}

	public int getSelectedCleanUpCount() {
		return options.isEnabled(JavaDudeCleanUpOptionsInitializer.CONVERT_TABS_TO_SPACES) ? 1 : 0;
	}

	public void setOptions(CleanUpOptions options) {
		this.options = options;
	}
}
