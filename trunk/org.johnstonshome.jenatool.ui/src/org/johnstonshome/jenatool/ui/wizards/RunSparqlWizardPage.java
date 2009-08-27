/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.wizards;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.johnstonshome.jenatool.internal.Connection;
import org.johnstonshome.jenatool.internal.Connections;
import org.johnstonshome.jenatool.ui.preferences.PluginPreferences;

class RunSparqlWizardPage extends WizardPage {
	
	private IFile  file;
	private Text   resource;
	private Combo  format;
	private Combo  datasets;
	private Button defaultModelContext;
	private Button unionModelContext;

	public RunSparqlWizardPage(IFile file) {
		super("Run SPARQL Query");
		setTitle("Run SPARQL Query");
		setDescription("Run a SPARQL query over a Jena store.");
		this.file = file; 
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		Label label = null;
		GridData gd = null;
		
		if (file != null) {
			label = new Label(container, SWT.NULL);
			label.setText("&Query Resource:");
			resource = new Text(container, SWT.BORDER | SWT.SINGLE);
			resource.setText(file.getProjectRelativePath().toString());
			gd = new GridData(GridData.FILL_HORIZONTAL);
			resource.setLayoutData(gd);
			resource.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					dialogChanged();
				}
			});
		}
		
		label = new Label(container, SWT.NULL);
		label.setText("&Target Dataset:");
		datasets = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		int select = 0;
		int curr = 0;
		for (Connection connection : Connections.getConnections()) {
			datasets.add(connection.toString());
			if (connection.toString().equals(Connections.getDefaultConnection().toString())) {
				select = curr; 
			}
			curr++;
		}
		datasets.select(select);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		datasets.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Output Format:");
		format = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		PluginPreferences prefs = new PluginPreferences();
		List<String> formats = prefs.getRdfForms();
		select = 0;
		for (int i = 0; i < formats.size(); i++) {
			format.add(formats.get(i), i);
			if (formats.get(i).equals(prefs.getDefaultRdfForm())) {
				select = i;
			}
		}
		format.select(select);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		format.setLayoutData(gd);

		Group group = new Group(container, SWT.NONE);
		GridLayout groupLayout = new GridLayout();
	    group.setLayout(groupLayout);
	    groupLayout.numColumns = 1;
	    groupLayout.verticalSpacing = 6;
	    group.setText("Query Context");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		
		defaultModelContext = new Button(group,SWT.RADIO);
		defaultModelContext.setText("Context is the store's default model.");
		defaultModelContext.setSelection(!prefs.isUseDefaultGraph());
		
		unionModelContext = new Button(group, SWT.RADIO);
		unionModelContext.setText("Context is the union of all named models in the store.");
		unionModelContext.setSelection(prefs.isUseDefaultGraph());
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		defaultModelContext.setLayoutData(gd);
		unionModelContext.setLayoutData(gd);


		setControl(container);
	}

	private void dialogChanged() {
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getResource() {
		return resource.getText();
	}

	public String getDataset() {
		return datasets.getText();
	}
	
	public String getFormat() {
		return format.getText();
	}

	public boolean isUnion() {
		return unionModelContext.getSelection();
	}
}
