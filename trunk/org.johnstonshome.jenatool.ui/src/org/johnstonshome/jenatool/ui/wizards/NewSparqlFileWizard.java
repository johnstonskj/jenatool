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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
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

public class NewSparqlFileWizard extends Wizard implements INewWizard {
	
	public static final int QUERY_FORM_SELECT = 1;
	public static final int QUERY_FORM_ASK = 2;
	public static final int QUERY_FORM_CONSTRUCT = 3;
	public static final int QUERY_FORM_DESCRIBE = 4;
	
	private NewSparqlFileWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for NewRdfFileWizard.
	 */
	public NewSparqlFileWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewSparqlFileWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		final String containerName = page.getContainerName();
		final String fileName = page.getFileName();
		final int queryForm = page.getQueryForm();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(containerName, fileName, queryForm, monitor);
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
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(String containerName, String fileName, int queryForm, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask("Creating " + fileName, 2);
		final IContainer container = FileUtils.getExistingContainerByPath(containerName);
		final IFile file = FileUtils.createFile(container, fileName, getContents(queryForm), monitor);
		monitor.worked(1);
		monitor.setTaskName("Opening file for editing...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage page =
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
				}
			}
		});
		monitor.worked(1);
	}
	
	private String getContents(int queryForm) {
		String contents = "";
		switch (queryForm) {
		case QUERY_FORM_SELECT:
			contents = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
				"SELECT ?value\n" +
				"WHERE\n" +
				"{\n" +
				"  ?s <http://example.org/v/some-predicate> ?value\n" +
				"}";
			break;
		case QUERY_FORM_ASK:
			contents = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
				"ASK\n" +
				"{\n" +
				"  ?s <http://example.org/v/some-predicate> ?value\n" +
				"}";
			break;
		case QUERY_FORM_CONSTRUCT:
			contents = 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\n" +
				"CONSTRUCT\n" +
				"{\n" +
				"  ?s <http://example.org/v/some-predicate> ?value\n" +
				"}\n" +
				"WHERE\n" +
				"{\n" +
				"  ?s <http://example.org/v/some-predicate> ?value .\n" +
				"  FILTER ( regex(str(?value), \"contains-this\" ) ." +
				"}";
			break;
		case QUERY_FORM_DESCRIBE:
			contents = "DESCRIBE <http://example.org/subject>";
			break;
		}
		return contents;
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}