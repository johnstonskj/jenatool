/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.wizards;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.johnstonshome.jenatool.internal.Connection;
import org.johnstonshome.jenatool.internal.ConnectionType;
import org.johnstonshome.jenatool.ui.Activator;

public class NewConnectionWizard extends Wizard {
	
	private NewConnectionWizardPage mainPage = null;
	private Connection connection = null;

	public NewConnectionWizard(Connection connection) {
		super();
		this.connection = connection;
		setDefaultPageImageDescriptor(Activator.getImageDescriptor("icons/jena-logo-small.png"));
	}
	
	@Override
	public boolean performFinish() {
		String location = mainPage.getLocation();
		boolean ok = true;
		
		File dir = new File(location);
		if (dir.exists() && !dir.isDirectory()) {
			MessageDialog.openError(
					getShell(), 
					"Location is not a directory", 
					"The specified location exists, but is not a directory. Please select a location that is either an existing directory or does not exist.");
			ok = false;
		} else if (!dir.exists()) {
			ok = MessageDialog.openQuestion(
					getShell(), 
					"Location does not exist", 
					"The specified location does not exist, continuing will create a new dataset at this location. Would you like to continue?");
		}
		
		if (ok) {
			ConnectionType type = ConnectionType.valueOf(ConnectionType.class, mainPage.getType());
			boolean union = mainPage.isUnion();
			if (connection == null) {
				connection = new Connection(type, location);
			} else {
				connection.setType(type);
				connection.setUrl(location);
			}
			connection.setUnion(union);
		}
		
		return ok;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if (connection == null) {
			setWindowTitle("New Jena Connection Wizard"); 
		} else {
			setWindowTitle("Edit Jena Connection"); 
		}
		setNeedsProgressMonitor(true);
		mainPage = new NewConnectionWizardPage(connection);
	}
	
    public void addPages() {
        super.addPages(); 
        addPage(mainPage);    
    }
    
    public Connection getConnection() {
    	return this.connection;
    }
}
