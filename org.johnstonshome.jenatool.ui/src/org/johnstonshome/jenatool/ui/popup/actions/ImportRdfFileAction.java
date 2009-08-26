/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.johnstonshome.jenatool.ui.Activator;
import org.johnstonshome.jenatool.ui.importWizards.RdfImportWizard;

public class ImportRdfFileAction implements IObjectActionDelegate {

	private Shell shell;
	private ISelection selection;
	private IFile selectedFile;
	
	/**
	 * Constructor for Action1.
	 */
	public ImportRdfFileAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
        RdfImportWizard wizard = new RdfImportWizard(selectedFile);
        WizardDialog dialog = new WizardDialog(shell, wizard);
        try {
        	wizard.init(Activator.getDefault().getWorkbench(), (IStructuredSelection)selection);
        	dialog.create();
        	dialog.open();
        } catch (Throwable t) {
        	t.printStackTrace();
        }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection selection2 = (IStructuredSelection)selection;
			if (!selection.isEmpty()) {
				Object selection3 = selection2.getFirstElement();
				if (selection3 instanceof IFile) {
					selectedFile = (IFile)selection3;
				}
			}
		}
	}

}
