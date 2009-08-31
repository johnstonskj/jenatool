/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.views;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.johnstonshome.jenatool.internal.HelpUtils;
import org.johnstonshome.jenatool.internal.SparqlRunner;
import org.johnstonshome.jenatool.ui.Activator;
import org.johnstonshome.jenatool.ui.preferences.PluginPreferences;
import org.johnstonshome.jenatool.ui.preferences.PreferenceConstants;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SparqlResultsView extends ViewPart implements IPropertyChangeListener {
	private TextViewer viewer;
	private Action terminateAction;
	private Action clearTextAction;
	private Action saveTextAction;

	IDocument contents = new Document();
	
	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	/**
	 * The constructor.
	 */
	public SparqlResultsView() {
		super();
		SparqlRunner.setResultsView(this);
	}

	public IDocument getDocument() {
		return this.contents;
	}
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TextViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setInput(getViewSite());
		viewer.setEditable(false);
		viewer.setDocument(contents);
		updateFont(false);

		HelpUtils.setHelp(viewer.getControl(), "resultsView");

		makeActions();
		hookContextMenu();
		contributeToActionBars();
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SparqlResultsView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(clearTextAction);
		manager.add(saveTextAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(clearTextAction);
		manager.add(saveTextAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(terminateAction);
		manager.add(new Separator());
		manager.add(clearTextAction);
		manager.add(saveTextAction);
	}

	private void makeActions() {		
		terminateAction = new Action() {
			public void run() {
				showMessage("Terminate executed");
			}
		};
		terminateAction.setText("Terminate");
		terminateAction.setToolTipText("Terminate Running Query");
		terminateAction.setImageDescriptor(Activator.getImageDescriptor("icons/task_stop_dis.gif"));
		
		clearTextAction = new Action() {
			public void run() {
				contents.set("");
			}
		};
		clearTextAction.setText("Clear");
		clearTextAction.setToolTipText("Clear All text");
		clearTextAction.setImageDescriptor(Activator.getImageDescriptor("icons/clear.gif"));
		
		saveTextAction = new Action() {
			public void run() {
				showMessage("Save executed");
			}
		};
		saveTextAction.setText("Save");
		saveTextAction.setToolTipText("Save Text");
		saveTextAction.setImageDescriptor(Activator.getImageDescriptor("icons/save_dis.gif"));
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"SPARQL Results",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();
		if (property.equals(PreferenceConstants.P_RESULTS_VIEW_FONT)) {
			updateFont(true);
		}
	}
	
	private void updateFont(boolean dispose) {
		PluginPreferences prefs = new PluginPreferences();
		FontData defaultFontData = prefs.getResultsViewFont();
		Font mono = new Font(viewer.getControl().getShell().getDisplay(), defaultFontData);
		viewer.getControl().setFont(mono);
	}
}