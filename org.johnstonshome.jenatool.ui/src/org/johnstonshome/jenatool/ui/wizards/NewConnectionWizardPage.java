/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.johnstonshome.jenatool.ui.preferences.Preferences;

public class NewConnectionWizardPage extends WizardPage {
	
	private Combo type;
	private Text  location;
	private Button union;

	public NewConnectionWizardPage() {
		super("Jena Connection");
		setTitle("Jena Connection");
		setDescription("Create a new Jena connection object.");
	}
	
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		Label label = new Label(container, SWT.NULL);
		label.setText("&Connection Type:");
		type = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		type.add("TDB", 0);
		type.select(0);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		type.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Dataset Location:");
		location = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		location.setLayoutData(gd);
		location.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Preferences prefs = new Preferences();
		label = new Label(container, SWT.NULL);
		label.setText("");
		union = new Button(container, SWT.CHECK);
		union.setText("Use union-default-graph setting");
		union.setSelection(prefs.isUseDefaultGraph());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		union.setLayoutData(gd);
		
		setControl(container);
	}

	private void dialogChanged() {
		if (getLocation().equals("")) {
			updateStatus("You must specify a valid file system location.");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getType() {
		return type.getText();
	}

	public String getLocation() {
		return location.getText();
	}

	public boolean isUnion() {
		return union.getSelection();
	}

}
