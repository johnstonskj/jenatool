/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.importWizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.johnstonshome.jenatool.internal.Connections;
import org.johnstonshome.jenatool.internal.Importer;
import org.johnstonshome.jenatool.ui.Activator;

public class RdfImportImportWizard extends Wizard implements IImportWizard {
	
	RdfImportWizardPage mainPage;

	public RdfImportImportWizard() {
		super();
		setDefaultPageImageDescriptor(Activator.getImageDescriptor("icons/import_wiz.png"));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {
		Importer importer = new Importer();
		return importer.importResource(
				mainPage.getImportUrl(), 
				mainPage.getFormat(),
				Connections.getConnection(mainPage.getDataset()), 
				mainPage.getGraphNameUrl(), 
				mainPage.getBaseUrl());
	}
	 
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("RDF Import Wizard"); //NON-NLS-1
		setNeedsProgressMonitor(true);
		mainPage = new RdfImportWizardPage(null);
	}
	
	/* (non-Javadoc)
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        super.addPages(); 
        addPage(mainPage);    
    }

}
