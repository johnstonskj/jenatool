/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.popup.actions;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.johnstonshome.jenatool.internal.Connections;
import org.johnstonshome.jenatool.internal.SparqlRunner;

public class RunSparqlQueryAction implements IObjectActionDelegate {

	private Shell shell;
	private IFile selectedFile;
	
	/**
	 * Constructor for Action1.
	 */
	public RunSparqlQueryAction() {
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
		if (Connections.getDefaultConnection() == null) {
			MessageDialog.openWarning(shell, "Run SPARQL Query", "No default connection has been assigned in the Jena Explorer view.");
		} else {
			IWorkbench wb = PlatformUI.getWorkbench();
			IProgressService ps = wb.getProgressService();
			final SparqlRunner runner = new SparqlRunner();
			try {
				ps.busyCursorWhile(new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException {
						try {
							runner.runSparqlQuery(selectedFile);
						} finally {
							monitor.done();
						}
					}
				});
				
			} catch (InterruptedException e) {
				;
			} catch (InvocationTargetException e) {
				Throwable realException = e.getTargetException();
				MessageDialog.openError(shell, "Error", realException.getMessage());
			}		
			SparqlRunner.getResultsView().getDocument().set(runner.getResults());
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
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
