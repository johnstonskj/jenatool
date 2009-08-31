/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.johnstonshome.jenatool.internal.FileUtils;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "rdf". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewJenaProjectWizard extends BasicNewProjectResourceWizard {

	/**
	 * Constructor for NewJenaProjectWizard.
	 */
	public NewJenaProjectWizard() {
		super();
	}
	
	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		boolean ok = super.performFinish();
		if (ok == true) {
			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					try {
						doFinish(monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}
			};
			try {
				getContainer().run(true, false, op);
			} catch (InterruptedException e) {
				return false;
			} catch (InvocationTargetException e) {
				Throwable realException = e.getTargetException();
				MessageDialog.openError(getShell(), "Error", realException.getMessage());
				return false;
			}
		}
		return ok;
	}
	
	private void doFinish(IProgressMonitor monitor) throws CoreException {
		IProject project = getNewProject();
		FileUtils.createFolder(project, "connections", monitor);
		FileUtils.createFolder(project, "content", monitor);
		FileUtils.createFolder(project, "queries", monitor);
		FileUtils.createFolder(project, "vocabularies", monitor);
	}
}