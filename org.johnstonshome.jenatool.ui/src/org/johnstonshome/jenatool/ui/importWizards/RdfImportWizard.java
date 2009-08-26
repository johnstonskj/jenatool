/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.importWizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.johnstonshome.jenatool.internal.Connections;
import org.johnstonshome.jenatool.internal.Importer;
import org.johnstonshome.jenatool.ui.Activator;

public class RdfImportWizard extends Wizard {
	
	private IFile initialFile;
	private RdfImportWizardPage mainPage;

	public RdfImportWizard(IFile initialFile) {
		super();
		this.initialFile = initialFile;
		setDefaultPageImageDescriptor(Activator.getImageDescriptor("icons/import_wiz.png"));
	}

	@Override
	public boolean performFinish() {
		Importer importer = new Importer();
		return importer.importResource(
				mainPage.getImportUrl(), 
				mainPage.getFormat(),
				Connections.getConnection(mainPage.getDataset()), 
				mainPage.getGraphNameUrl(), 
				mainPage.getBaseUrl());
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("RDF Import Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new RdfImportWizardPage(initialFile);
	}
	
    public void addPages() {
        super.addPages(); 
        addPage(mainPage);    
    }
}
