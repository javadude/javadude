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
package com.javadude.antxr.eclipse.ui.editor;

import java.util.Iterator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.MarkerAnnotation;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.tasklist.TaskList;

import com.javadude.antxr.eclipse.core.parser.ISegment;
import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.actions.GotoRuleAction;
import com.javadude.antxr.eclipse.ui.actions.IAntxrActionConstants;
import com.javadude.antxr.eclipse.ui.actions.IAntxrActionDefinitionIds;
import com.javadude.antxr.eclipse.ui.editor.outline.AntxrOutlinePage;

/**
 * An editor for an ANTXR grammar
 */
public class AntxrEditor extends AbstractDecoratedTextEditor implements IGotoMarker {

	private static final String PREFIX = "Editor.";

	private AntxrMultiPageEditor fMultiPageEditor;
	private ModelTools fModelTools;
	private AntxrReconcilingStrategy fReconcilingStrategy;

	/** The outline page */
	private AntxrOutlinePage fOutlinePage;

	/** The status line clearer */
	private ISelectionChangedListener fStatusLineClearer;

	/** Last cursor position (line) handled in
	 * <code>handleCursorPositionChanged()</code> */
	private int fLastCursorLine;

	/**
	 * Create the editor
	 * @param aMultiPageEditor the multi-page editor we're in
	 */
	public AntxrEditor(AntxrMultiPageEditor aMultiPageEditor) {
		fMultiPageEditor = aMultiPageEditor;
		fModelTools = new ModelTools(this);
		fReconcilingStrategy = new AntxrReconcilingStrategy(this);

		setEditorContextMenuId("#AntxrGrammarFilePopupContext"); //$NON-NLS-1$
		setRulerContextMenuId("#AntxrGrammarFileRulerContext"); //$NON-NLS-1$
//		setOutlinerContextMenuId("#ClassFileOutlinerContext"); //$NON-NLS-1$

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#initializeEditor()
	 */
	protected void initializeEditor() {
		super.initializeEditor();

		EditorEnvironment.connect();

		setDocumentProvider(new AntxrDocumentProvider());
		setSourceViewerConfiguration(new AntxrConfiguration(this));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.editors.text.TextEditor#initializeKeyBindingScopes()
	 */
	protected void initializeKeyBindingScopes() {
		setKeyBindingScopes(new String[] { "com.javadude.antxr.ui.antxrEditorScope" });
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#createActions()
	 */
	protected void createActions() {
		super.createActions();

		// Add goto rule action
		IAction action = new GotoRuleAction(
								AntxrUIPlugin.getDefault().getResourceBundle(),
								AntxrEditor.PREFIX + "GotoRule.", this);
		action.setActionDefinitionId(IAntxrActionDefinitionIds.GOTO_RULE);
		setAction(IAntxrActionConstants.GOTO_RULE, action);

		// Add content assist propsal action
		action = new ContentAssistAction(
								AntxrUIPlugin.getDefault().getResourceBundle(),
								AntxrEditor.PREFIX + "ContentAssist.", this);
		action.setActionDefinitionId(
					  ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		setAction(IAntxrActionConstants.CONTENT_ASSIST, action);

		// Add comment action
		action = new TextOperationAction(
					  AntxrUIPlugin.getDefault().getResourceBundle(),
					  AntxrEditor.PREFIX + "Comment.", this, ITextOperationTarget.PREFIX);
		action.setActionDefinitionId(IJavaEditorActionDefinitionIds.COMMENT);
		setAction(IAntxrActionConstants.COMMENT, action);

		// Add uncomment action
		action = new TextOperationAction(
			  AntxrUIPlugin.getDefault().getResourceBundle(),
			  AntxrEditor.PREFIX + "Uncomment.", this, ITextOperationTarget.STRIP_PREFIX);
		action.setActionDefinitionId(IJavaEditorActionDefinitionIds.UNCOMMENT);
		setAction(IAntxrActionConstants.UNCOMMENT, action);
	}

	/**
	 * The <code>AntxrEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method gets the ANTXR content outline
	 * page if request is for a an outline page.
	 * @param aClass the type to adapt to
	 * @return the adapter
	 */
	public Object getAdapter(Class aClass) {
	    Object adapter;
		if (aClass.equals(IContentOutlinePage.class)) {
			if (fOutlinePage == null || fOutlinePage.isDisposed()) {
			    fOutlinePage = new AntxrOutlinePage(this);
				if (getEditorInput() != null) {
					fOutlinePage.setInput(getEditorInput());
				}
			}
			adapter = fOutlinePage;
		} else {
		    adapter = super.getAdapter(aClass);
		}
		return adapter;
	}

	/**
	 * The <code>AntxrEditor</code> implementation of this
	 * <code>AbstractTextEditor</code> method performs any extra
	 * disposal actions required by the ANTXR editor.
	 *
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		EditorEnvironment.disconnect();
		if (fOutlinePage != null && !fOutlinePage.isDisposed()) {
			fOutlinePage.dispose();
			fOutlinePage = null;
		}
		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#editorContextMenuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	protected void editorContextMenuAboutToShow(IMenuManager aMenu) {
		super.editorContextMenuAboutToShow(aMenu);
		addAction(aMenu, IWorkbenchActionConstants.MB_ADDITIONS,
				  IAntxrActionConstants.GOTO_RULE);
		addAction(aMenu, IWorkbenchActionConstants.MB_ADDITIONS,
				  IAntxrActionConstants.COMMENT);
		addAction(aMenu, IWorkbenchActionConstants.MB_ADDITIONS,
				  IAntxrActionConstants.UNCOMMENT);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#handleCursorPositionChanged()
	 */
	protected void handleCursorPositionChanged() {
		super.handleCursorPositionChanged();
		int line = getCursorLine();
		if (line > 0 && line != fLastCursorLine) {
			fLastCursorLine = line;
			if (fOutlinePage != null && !fOutlinePage.isDisposed()) {
				fOutlinePage.selectSegment(line, false);
			}
		}
	}

	/** {@inheritDoc} */
	public void markInNavigationHistory() {
		getEditorSite().getPage().getNavigationHistory().markLocation(
															 fMultiPageEditor);
	}

	/**
	 * Get the document we're editing
	 * @return the document
	 */
	public IDocument getDocument() {
		return getSourceViewer().getDocument();
	}

	/**
	 * Get the line containing the cursor
	 * @return the line containing the cursor
	 */
	public int getCursorLine() {
		int line = -1;

		ISourceViewer sourceViewer = getSourceViewer();
		if (sourceViewer != null) {
			StyledText styledText = sourceViewer.getTextWidget();
			int caret = AbstractTextEditor.widgetOffset2ModelOffset(sourceViewer,
												  styledText.getCaretOffset());
			IDocument document = sourceViewer.getDocument();
			if (document != null) {
				try {
					line = document.getLineOfOffset(caret) + 1;
				} catch (BadLocationException e) {
					AntxrUIPlugin.log(e);
				}
			}
		}
		return line;
	}

	/**
	 * Get the rules defined for this editor
	 * @param aPrefix The prefix
	 * @return the rules
	 */
	public String[] getRules(String aPrefix) {
		return fModelTools.getRules(aPrefix);
	}

	/**
	 * Get the segment containing the line
	 * @param aLine the line
	 * @return the segment
	 */
	public ISegment getSegment(int aLine) {
		return fModelTools.getSegment(aLine);
	}

	/**
	 * Get the named segment
	 * @param aName the name
	 * @return the segment
	 */
	public ISegment getSegment(String aName) {
		return fModelTools.getSegment(aName);
	}

	/**
	 * Highlight the segment
	 * @param aSegment the segment
	 * @param aMoveCursor should we move the cursor?
	 */
	public void highlightSegment(ISegment aSegment, boolean aMoveCursor) {
	    IDocument doc = getDocument();
		try {
			int offset = doc.getLineOffset(aSegment.getStartLine() - 1);
			IRegion endLine = doc.getLineInformation(aSegment.getEndLine() - 1);
			int length = endLine.getOffset() + endLine.getLength() - offset;
			setHighlightRange(offset, length, aMoveCursor);
		} catch (BadLocationException e) {
			resetHighlightRange();
		}
		fMultiPageEditor.activateEditor();
	}

	/**
	 * Ensure the segment is visible
	 * @param aSegment the segment
	 */
	public void revealSegment(ISegment aSegment) {
		ISourceViewer viewer = getSourceViewer();
		if (viewer != null) {
		    IDocument doc = getDocument();
			try {
				int offset = doc.getLineOffset(aSegment.getStartLine() - 1);
				IRegion endLine = doc.getLineInformation(
													aSegment.getEndLine() - 1);
				int length = endLine.getOffset() + endLine.getLength() - offset;

				// Reveal segment's text area in document
				StyledText widget = getSourceViewer().getTextWidget();
				widget.setRedraw(false);
				viewer.revealRange(offset, length);
				widget.setRedraw(true);
			} catch (BadLocationException e) {
				resetHighlightRange();
			}
		}
		fMultiPageEditor.activateEditor();
	}

	/**
	 * Jump to the named rule
	 * @param aName the rule
	 */
	public void gotoRule(String aName) {
		ISegment segment = fModelTools.getSegment(aName);
		if (segment != null) {
			markInNavigationHistory();
			highlightSegment(segment, true);
			markInNavigationHistory();
		}
	}

	/**
	 * Jumps to the next or previous error according to the given direction.
	 * @param anIsForward Move forward to next error (false = previous error)
	 */
	public void gotoError(boolean anIsForward) {
		ISelectionProvider provider = getSelectionProvider();

		if (fStatusLineClearer != null) {
			provider.removeSelectionChangedListener(fStatusLineClearer);
			fStatusLineClearer= null;
		}

		ITextSelection s = (ITextSelection)provider.getSelection();
		IMarker nextError = getNextError(s.getOffset(), anIsForward);

		if (nextError != null) {

			IGotoMarker gotoMarker = (IGotoMarker) getAdapter(IGotoMarker.class);
			if (gotoMarker != null) {
				gotoMarker.gotoMarker(nextError);
			}

			IWorkbenchPage page = getSite().getPage();

			IViewPart view = page.findView(IPageLayout.ID_TASK_LIST);
			if (view != null && view instanceof TaskList) {
				StructuredSelection ss = new StructuredSelection(nextError);
				((TaskList)view).setSelection(ss, true);
			}

			getStatusLineManager().setErrorMessage(nextError.getAttribute(
														 IMarker.MESSAGE, ""));
			fStatusLineClearer = new ISelectionChangedListener() {
				public void selectionChanged(SelectionChangedEvent event) {
					getSelectionProvider().removeSelectionChangedListener(
														   fStatusLineClearer);
					fStatusLineClearer = null;
					getStatusLineManager().setErrorMessage("");
				}
			};
			provider.addSelectionChangedListener(fStatusLineClearer);
		} else {
			getStatusLineManager().setErrorMessage("");
		}
	}

	private IMarker getNextError(int anOffset, boolean anIsForward) {

		IMarker nextError = null;

		IDocument document = getDocument();
		int endOfDocument = document.getLength();
		int distance = 0;

		IAnnotationModel model = getDocumentProvider().getAnnotationModel(
															 getEditorInput());
		Iterator iter = model.getAnnotationIterator();
		while (iter.hasNext()) {
			Annotation a = (Annotation)iter.next();
			if (a instanceof MarkerAnnotation) {
				IMarker marker = ((MarkerAnnotation)a).getMarker();

				if (MarkerUtilities.isMarkerType(marker, IMarker.PROBLEM)) {
					Position p = model.getPosition(a);
					if (!p.includes(anOffset)) {
						int currentDistance = 0;
						if (anIsForward) {
							currentDistance = p.getOffset() - anOffset;
							if (currentDistance < 0) {
								currentDistance = endOfDocument - anOffset +
												  p.getOffset();
							}
						} else {
							currentDistance = anOffset - p.getOffset();
							if (currentDistance < 0) {
								currentDistance = anOffset + endOfDocument -
												  p.getOffset();
							}
						}
						if (nextError == null || currentDistance < distance) {
							distance = currentDistance;
							nextError = marker;
						}
					}
				}

			}
		}
		return nextError;
	}

	/**
	 * Get the reconciler
	 * @return the reconciler
	 */
	public AntxrReconcilingStrategy getReconcilingStrategy() {
		return fReconcilingStrategy;
	}

	/**
	 * Get the root elements
	 * @return the root elements
	 */
	public Object[] getRootElements() {
		return fReconcilingStrategy.getRootElements();
	}

	/**
	 * Get the root segment
	 * @return the root segment
	 */
	public ISegment getRootSegment() {
		return fReconcilingStrategy.getRootSegment();
	}

	/**
	 * Update the outline page
	 */
	public void updateOutlinePage() {
		if (fOutlinePage != null) {
			fOutlinePage.update();
		}
	}

	/**
	 * Move the cursor to the specified line
	 * @param aLine the target line
	 */
	public void moveCursor(int aLine) {
		ISourceViewer sourceViewer = getSourceViewer();
		try {
			int offset = getDocument().getLineOffset(aLine - 1);
			sourceViewer.setSelectedRange(offset, 0);
			sourceViewer.revealRange(offset, 0);
		} catch (BadLocationException e) {
			AntxrUIPlugin.log(e);
		}
	}

	/**
	 * Returns the desktop's StatusLineManager.
	 * @return the statusline manager
	 */
	protected IStatusLineManager getStatusLineManager() {
		IStatusLineManager manager;
		IEditorActionBarContributor contributor =
					fMultiPageEditor.getEditorSite().getActionBarContributor();
		if (contributor != null &&
						  contributor instanceof EditorActionBarContributor) {
			manager = ((EditorActionBarContributor)contributor).
										getActionBars().getStatusLineManager();
		} else {
			manager = null;
		}
		return manager;
	}

	/**
	 * Displays an error message in editor's status line.
	 * @param aMessage the message to display
	 */
	public void displayErrorMessage(String aMessage) {
		IStatusLineManager manager = getStatusLineManager();
		if (manager != null) {
			manager.setErrorMessage(aMessage);
		}
	}
}
