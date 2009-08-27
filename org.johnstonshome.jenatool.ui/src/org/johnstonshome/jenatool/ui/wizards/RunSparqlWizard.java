/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.johnstonshome.jenatool.ui.Activator;

public class RunSparqlWizard extends Wizard {
	
	private IFile initialFile;
	private RunSparqlWizardPage mainPage;

	private String resource;
	private String dataset; 
	private String format;
	private boolean isUnion;

	public RunSparqlWizard(IFile initialFile) {
		super();
		this.initialFile = initialFile;
		setDefaultPageImageDescriptor(Activator.getImageDescriptor("icons/run_wiz.png"));
	}

	@Override
	public boolean performFinish() {
		this.resource = mainPage.getResource();
		this.dataset = mainPage.getDataset();
		this.format = mainPage.getFormat();
		this.isUnion = mainPage.isUnion();
		return true;
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Run SPARQL Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new RunSparqlWizardPage(initialFile);
	}
	
    public void addPages() {
        super.addPages(); 
        addPage(mainPage);    
    }

	public String getResource() {
		return this.resource;
	}

	public String getDataset() {
		return this.dataset;
	}
	
	public String getFormat() {
		return this.format;
	}

	public boolean isUnion() {
		return this.isUnion;
	}
}
