/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.importWizards;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.johnstonshome.jenatool.internal.Connection;
import org.johnstonshome.jenatool.internal.Connections;

class RdfImportWizardPage extends WizardPage {
	
	private String initialUrl;
	private Text   importUrl;
	private Combo  format;
	private Combo  datasets;
	private Text   graphNameUrl;
	private Text   baseUrl;

	public RdfImportWizardPage(IFile file) {
		super("Import RDF");
		setTitle("Import RDF");
		setDescription("Import RDF into a Jena Dataset.");
		if (file != null) {
			initialUrl = file.getLocationURI().toString();
		} else {
			initialUrl = "http://";
		}
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		Label label = new Label(container, SWT.NULL);
		label.setText("&Import URL:");
		importUrl = new Text(container, SWT.BORDER | SWT.SINGLE);
		importUrl.setText(initialUrl);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		importUrl.setLayoutData(gd);
		importUrl.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(container, SWT.NULL);
		label.setText("&Format:");
		format = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		format.add("RDF/XML", 0);
		format.add("N-Triples", 1);
		format.add("Turtle", 2);
		format.add("N3", 3);
		selectFormat(format, importUrl);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		format.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Target Dataset:");
		datasets = new Combo(container, SWT.BORDER | SWT.SINGLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		for (Connection connection : Connections.getConnections()) {
			datasets.add(connection.toString(), 0);
		}
		datasets.select(0);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		datasets.setLayoutData(gd);

		label = new Label(container, SWT.NULL);
		label.setText("&Graph Name URL:");
		graphNameUrl = new Text(container, SWT.BORDER | SWT.SINGLE);
		graphNameUrl.setToolTipText("Enter a URL of a named graph to add this RDF data to.");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		graphNameUrl.setLayoutData(gd);
		graphNameUrl.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		label = new Label(container, SWT.NULL);
		label.setText("&Base URL:");
		baseUrl = new Text(container, SWT.BORDER | SWT.SINGLE);
		baseUrl.setToolTipText("Enter a URL to act as the base for any relative URLs in the RDF data.");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		baseUrl.setLayoutData(gd);
		baseUrl.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});	
		
		setControl(container);
	}

	private void selectFormat(Combo format, Text importUrl) {
		int selection = 0;
		final String url = importUrl.getText();
		if (!url.equals("")) {
			int dot = url.lastIndexOf('.');
			if (dot >= 0) {
				String extension = url.substring(dot+1);
				if (extension.toLowerCase().equals("rdf")) {
					selection = 0;
				} else if (extension.toLowerCase().equals("xml")) {
					selection = 0;
				} else if (extension.toLowerCase().equals("nt")) {
					selection = 1;
				} else if (extension.toLowerCase().equals("ttl")) {
					selection = 2;
				} else if (extension.toLowerCase().equals("n3")) {
					selection = 3;
				}
			}
		}
		format.select(selection);
	}

	private void dialogChanged() {
		if (!validateUrl(getImportUrl())) {
			updateStatus("Import URL is not a valid or absolute URI.");
			return;
		}
		selectFormat(format, importUrl);
		if (!validateUrl(getGraphNameUrl())) {
			updateStatus("Graph name URL is not a valid or absolute URI.");
			return;
		}
		if (!validateUrl(getBaseUrl())) {
			updateStatus("Base URL is not a valid or absolute URI.");
			return;
		}
		updateStatus(null);
	}

	private boolean validateUrl(String url) {
		if (url.length() > 0) {
			try {
				URI uri = new URI(url);
				return uri.isAbsolute();
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getImportUrl() {
		return importUrl.getText();
	}

	public String getFormat() {
		return format.getText();
	}

	public String getDataset() {
		return datasets.getText();
	}

	public String getGraphNameUrl() {
		return graphNameUrl.getText();
	}

	public String getBaseUrl() {
		return baseUrl.getText();
	}
}
