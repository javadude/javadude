package com.javadude.cleanup;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.text.edits.ReplaceEdit;

public class JavaDudeCleanUpFix implements ICleanUpFix {
    private ICompilationUnit compilationUnit;
    private final CleanUpOptions options;

    public JavaDudeCleanUpFix(ICompilationUnit compilationUnit, CleanUpOptions options) {
        this.compilationUnit = compilationUnit;
        this.options = options;
    }

    public CompilationUnitChange createChange(IProgressMonitor progressMonitor)
            throws CoreException {
        if (options.isEnabled(JavaDudeCleanUpOptionsInitializer.CONVERT_TABS_TO_SPACES)) {
            String source = compilationUnit.getSource();
            ReplaceEdit edit = new ReplaceEdit(0, source.length(), fixTabs(source, Integer.parseInt(options.getValue(JavaDudeCleanUpOptionsInitializer.TAB_SIZE))));
            CompilationUnitChange change = new CompilationUnitChange("Convert Tabs to Spaces", compilationUnit);
            change.setEdit(edit);
            return change;
        }
        return null;
    }

    // grrrr... I hate that I have to explicitly replace tabs with spaces to get anything other than 8-space tabs...
    private String fixTabs(String string, int tabSize) {
        String[] pads = new String[tabSize+1];
        for (int i = 0; i <= tabSize; i++) {
			pads[i] = "";
			for (int j = 0; j < i; j++) {
				pads[i] += ' ';
			}
		}

		int len = string.length();
		int column = 1; // position in line
		StringBuilder b = new StringBuilder();
		for (int n = 0; n < len; n++) {
			char c = string.charAt(n);
			switch (c) {
				case '\t':
					int pad = tabSize - (column % tabSize);
					b.append(pads[pad]);
					column += pad;
					break;
				case '\n':
				case '\r':
					column = 0;
					b.append(c);
					break;
				default:
					column++;
					b.append(c);
					break;
			}
		}
		return b.toString();
	}
}
