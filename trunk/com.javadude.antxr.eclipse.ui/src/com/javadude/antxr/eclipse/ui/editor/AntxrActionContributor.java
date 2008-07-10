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
package com.javadude.antxr.eclipse.ui.editor;

import com.javadude.antxr.eclipse.ui.AntxrUIPlugin;
import com.javadude.antxr.eclipse.ui.actions.GotoErrorAction;
import com.javadude.antxr.eclipse.ui.actions.IAntxrActionConstants;
import com.javadude.antxr.eclipse.ui.actions.IAntxrActionDefinitionIds;
import com.javadude.antxr.eclipse.ui.actions.TogglePresentationAction;

import org.eclipse.jdt.ui.actions.IJavaEditorActionDefinitionIds;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;

/**
 * Contributes interesting ANTXR actions to the desktop's edit menu and the
 * toolbar.
 */
public class AntxrActionContributor extends
                                          MultiPageEditorActionBarContributor {
    private static final String PREFIX = "Editor.";

    /** The global actions to be connected with editor actions */
    private final static String[] ACTIONS = {
        ITextEditorActionConstants.UNDO,
        ITextEditorActionConstants.REDO,
        ITextEditorActionConstants.CUT,
        ITextEditorActionConstants.COPY,
        ITextEditorActionConstants.PASTE,
        ITextEditorActionConstants.DELETE,
        ITextEditorActionConstants.SELECT_ALL,
        ITextEditorActionConstants.FIND,
        IDEActionFactory.BOOKMARK.getId(),
        IDEActionFactory.ADD_TASK.getId(),
        ITextEditorActionConstants.PRINT,
        ITextEditorActionConstants.REVERT,
    };

    private BasicTextEditorActionContributor fSourceContributor;
    private SubMenuManager fSubMenuManager;
    private SubStatusLineManager fSubStatusLineManager;
    private SubToolBarManager fSubToolbarManager;

    private TogglePresentationAction fTogglePresentation;
    private GotoErrorAction fNextError;
    private GotoErrorAction fPreviousError;
    private RetargetTextEditorAction fGotoRule;
    private RetargetTextEditorAction fContentAssist;
    private RetargetTextEditorAction fComment;
    private RetargetTextEditorAction fUncomment;

    /**
     * Action contributor
     */
    public AntxrActionContributor() {
        fSourceContributor = new BasicTextEditorActionContributor();
        createActions();
    }

    protected void createActions() {

        // Define toolbar actions
        fTogglePresentation = new TogglePresentationAction();
        fNextError = new GotoErrorAction(true);
        fPreviousError = new GotoErrorAction(false);

        // Define text editor actions
        fGotoRule = new RetargetTextEditorAction(
                                AntxrUIPlugin.getDefault().getResourceBundle(),
                                PREFIX + "GotoRule.");
        fGotoRule.setActionDefinitionId(IAntxrActionDefinitionIds.GOTO_RULE);
        fContentAssist = new RetargetTextEditorAction(
                                AntxrUIPlugin.getDefault().getResourceBundle(),
                                PREFIX + "ContentAssist.");
        fContentAssist.setActionDefinitionId(
                      ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        fComment = new RetargetTextEditorAction(
                                AntxrUIPlugin.getDefault().getResourceBundle(),
                                PREFIX + "Comment.");
        fComment.setActionDefinitionId(IJavaEditorActionDefinitionIds.COMMENT);
        fUncomment = new RetargetTextEditorAction(
                                AntxrUIPlugin.getDefault().getResourceBundle(),
                                PREFIX + "Uncomment.");
        fUncomment.setActionDefinitionId(
                                     IJavaEditorActionDefinitionIds.UNCOMMENT);
    }

    /** {@inheritDoc} */
    public void setActivePage(IEditorPart anEditor) {
        doSetActiveEditor(anEditor);
    }

    /**
     * The method installs the global action handlers for the given text editor.
     *
     * @param aPart the editor
     */
    private void doSetActiveEditor(IEditorPart aPart) {
        IStatusLineManager manager = getActionBars().getStatusLineManager();
        manager.setMessage(null);
        manager.setErrorMessage(null);

        ITextEditor editor = null;
        if (aPart instanceof AntxrEditor) {
            editor = (AntxrEditor)aPart;
        }
        fSourceContributor.init(getActionBars());
        fSourceContributor.setActiveEditor(editor);

        // Enable/disable menus, status line and tool bars according to the
        // given editor
        fSubMenuManager.setVisible(editor != null);
        fSubMenuManager.update(false);
        fSubStatusLineManager.setVisible(editor != null);
        fSubStatusLineManager.update(false);
        fSubToolbarManager.setVisible(editor != null);
        fSubToolbarManager.update(false);

        // Set the underlying action (registered by the related editor) in
        // the action handlers
        fTogglePresentation.setEditor(editor);
        fNextError.setEditor(editor);
        fPreviousError.setEditor(editor);
        fGotoRule.setAction(getAction(editor,
                                      IAntxrActionConstants.GOTO_RULE));
        fContentAssist.setAction(getAction(editor,
                                        IAntxrActionConstants.CONTENT_ASSIST));
        fComment.setAction(getAction(editor, IAntxrActionConstants.COMMENT));
        fUncomment.setAction(getAction(editor,
                                       IAntxrActionConstants.UNCOMMENT));
        // Set global action handlers according to the given editor
        IActionBars actionBars = getActionBars();
        if (actionBars != null) {
            actionBars.clearGlobalActionHandlers();
            for (int i = 0; i < ACTIONS.length; i++) {
                actionBars.setGlobalActionHandler(ACTIONS[i],
                                                getAction(editor, ACTIONS[i]));
            }
            actionBars.setGlobalActionHandler(ITextEditorActionConstants.GOTO_LINE,
                getAction(editor, ITextEditorActionDefinitionIds.LINE_GOTO));
            actionBars.setGlobalActionHandler(ActionFactory.NEXT.getId(),
                                              fNextError);
            actionBars.setGlobalActionHandler(ActionFactory.PREVIOUS.getId(),
                                              fPreviousError);

            actionBars.setGlobalActionHandler(IJavaEditorActionDefinitionIds.COMMENT,
                getAction(editor, IAntxrActionConstants.COMMENT));
            actionBars.setGlobalActionHandler(IJavaEditorActionDefinitionIds.UNCOMMENT,
                getAction(editor, IAntxrActionConstants.UNCOMMENT));

            actionBars.setGlobalActionHandler(IAntxrActionDefinitionIds.GOTO_RULE,
                getAction(editor, IAntxrActionConstants.GOTO_RULE));

            actionBars.updateActionBars();
        }
    }

    /**
     * Returns the action registed with the given text editor.
     * @param anEditor the editor
     * @param anActionID the action
     * @return IAction or null if editor is null.
     */
    protected IAction getAction(ITextEditor anEditor, String anActionID) {
        return (anEditor != null ? anEditor.getAction(anActionID) : null);
    }

    /** {@inheritDoc} */
    public void contributeToMenu(IMenuManager aMenuManager) {
        fSubMenuManager = new SubMenuManager(aMenuManager);

        // Add standard text editor menu contributions
        fSourceContributor.contributeToMenu(fSubMenuManager);

        // Add actions to desktop's edit menu
        IMenuManager menu = fSubMenuManager.findMenuUsingPath(
                                             IWorkbenchActionConstants.M_EDIT);
        if (menu != null) {
            menu.add(fContentAssist);
            menu.add(fComment);
            menu.add(fUncomment);
        }

        // Add actions to desktop's navigate menu
        menu = fSubMenuManager.findMenuUsingPath(
                                         IWorkbenchActionConstants.M_NAVIGATE);
        if (menu != null) {
            menu.appendToGroup(IWorkbenchActionConstants.MB_ADDITIONS,
                               fGotoRule);
        }
    }

    /** {@inheritDoc} */
    public void contributeToStatusLine(IStatusLineManager aStatusLineManager) {
        fSubStatusLineManager = new SubStatusLineManager(aStatusLineManager);
        fSourceContributor.contributeToStatusLine(fSubStatusLineManager);
    }

    /** {@inheritDoc} */
    public void contributeToToolBar(IToolBarManager aToolBarManager) {
        fSubToolbarManager = new SubToolBarManager(aToolBarManager);

        // Add standard text editor tool bar contributions
        fSourceContributor.contributeToToolBar(fSubToolbarManager);

        // Add our own tool bar contributions
        fSubToolbarManager.add(new Separator());
        fSubToolbarManager.add(fTogglePresentation);
        fSubToolbarManager.add(fPreviousError);
        fSubToolbarManager.add(fNextError);
    }

    /** {@inheritDoc} */
    public void dispose() {
        doSetActiveEditor(null);
        super.dispose();
    }
}
