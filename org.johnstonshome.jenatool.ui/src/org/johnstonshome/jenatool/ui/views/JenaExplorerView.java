/*
 * Licensed Materials - Property of Simon Johnston (simon@johnstonshome.org)
 * (c) Copyright Simon Johnston 2009. All rights reserved.
 * 
 * For full license details, see the file LICENSE inncluded in the
 * distribution of this code.
 * 
 */
package org.johnstonshome.jenatool.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.johnstonshome.jenatool.internal.Connection;
import org.johnstonshome.jenatool.internal.Connections;
import org.johnstonshome.jenatool.internal.ImageCache;
import org.johnstonshome.jenatool.ui.Activator;
import org.johnstonshome.jenatool.ui.wizards.NewConnectionWizard;


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

public class JenaExplorerView extends ViewPart {
	private TreeViewer viewer;
	private Action addConnectionAction;
	private Action removeConnectionAction;
	private Action defaultConnectionAction;
	private Action doubleClickAction;
	
	private TreeParent connectionFolder = null;
	private TreeObject selected = null;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		@SuppressWarnings("unchecked")
		public Object getAdapter(Class key) {
			return null;
		}
	}
	
	class ConnectionTreeObject extends TreeObject {
		private Connection connection = null;
		public ConnectionTreeObject(Connection connection) {
			super(connection.toString());
			this.connection = connection;
		}
		public Connection getConnection() {
			return this.connection;
		}
	}
	
	class TreeParent extends TreeObject {
		private List<TreeObject> children;
		public TreeParent(String name) {
			super(name);
			children = new ArrayList<TreeObject>();
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private TreeParent invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
		
		private void initialize() {			
			connectionFolder = new TreeParent("Jena Connections");
			for (Connection connection : Connections.getConnections()) {
				TreeObject obj = new ConnectionTreeObject(connection);
				connectionFolder.addChild(obj);
			}
			
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(connectionFolder);
		}
	}
	
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			if (obj instanceof TreeParent) {
				return ImageCache.getImage("icons/connection_folder.gif");
			} else {
				ConnectionTreeObject connObj = (ConnectionTreeObject)obj;
				if (connObj.getConnection().equals(Connections.getDefaultConnection())) {
					return ImageCache.getImage("icons/connection_edit.gif");
				} else {
					if (connObj.getConnection().isConnected()) {
						return ImageCache.getImage("icons/connection.gif");
					} else {
						return ImageCache.getImage("icons/connection_closed.gif");
					}
				}
			}
		}
	}
	
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public JenaExplorerView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
//		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		viewer.addSelectionChangedListener(new ISelectionChangedListener()
	    {
		      public void selectionChanged(SelectionChangedEvent event)
		      {
		    	  IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		    	  selected = (TreeObject) selection.getFirstElement();
		    	  if (selected instanceof TreeParent) {
		    		  removeConnectionAction.setEnabled(false);
		    		  defaultConnectionAction.setEnabled(false);
		    	  } else {
		    		  removeConnectionAction.setEnabled(true);
		    		  defaultConnectionAction.setEnabled(true);
		    	  }
		      }
		});
		
		/*
		 * This is enough to show all the contents of the connection folder.
		 */
		viewer.expandToLevel(4);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "org.johnstonshome.jenatool.ui.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				JenaExplorerView.this.fillContextMenu(manager);
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
		manager.add(addConnectionAction);
		manager.add(removeConnectionAction);
		manager.add(defaultConnectionAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addConnectionAction);
		manager.add(removeConnectionAction);
		manager.add(defaultConnectionAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addConnectionAction);
		manager.add(removeConnectionAction);
		manager.add(defaultConnectionAction);
	}

	private void makeActions() {
		addConnectionAction = new Action() {
			public void run() {
		        NewConnectionWizard wizard = new NewConnectionWizard(null);
		        WizardDialog dialog = new WizardDialog(viewer.getControl().getShell(), wizard);
		        try {
		        	wizard.init(Activator.getDefault().getWorkbench(), (IStructuredSelection)null);
		        	dialog.create();
		        	dialog.open();
		        	
	        		Connection conn = wizard.getConnection();
		        	if (conn != null) {
		        		conn.connect();
		        		Connections.add(conn);
						TreeObject obj = new ConnectionTreeObject(conn);
						connectionFolder.addChild(obj);
						viewer.refresh();
		        	}
		        } catch (Throwable t) {
		        	t.printStackTrace();
		        }
			}
		};
		addConnectionAction.setText("Add");
		addConnectionAction.setToolTipText("Add New Connection");
		addConnectionAction.setImageDescriptor(Activator.getImageDescriptor("icons/connection_new.gif"));
		
		removeConnectionAction = new Action() {
			public void run() {
				boolean yes = MessageDialog.openQuestion(
						viewer.getControl().getShell(), 
						"Delete Connection", 
						"Are you sure you want to delete the connection '" + selected.toString() + "'?");
				if (yes) {
					Connections.remove(((ConnectionTreeObject)selected).getConnection());
					selected.getParent().removeChild(selected);
					viewer.remove(selected);
					viewer.refresh();
				}
			}
		};
		removeConnectionAction.setText("Delete");
		removeConnectionAction.setToolTipText("Delete Connection");
		removeConnectionAction.setImageDescriptor(Activator.getImageDescriptor("icons/object_delete.gif"));
		removeConnectionAction.setEnabled(false);

		defaultConnectionAction = new Action() {
			public void run() {
				ConnectionTreeObject obj = (ConnectionTreeObject)selected;
				Connections.setDefaultConnection(obj.getConnection());
        		obj.getConnection().connect();
				viewer.refresh(true);
			}
		};
		defaultConnectionAction.setText("Default");
		defaultConnectionAction.setToolTipText("Make Default Connection");
		defaultConnectionAction.setImageDescriptor(Activator.getImageDescriptor("icons/connection_edit.gif"));
		defaultConnectionAction.setEnabled(false);

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				ConnectionTreeObject obj = (ConnectionTreeObject) ((IStructuredSelection)selection).getFirstElement();
				
		        NewConnectionWizard wizard = new NewConnectionWizard(obj.getConnection());
		        WizardDialog dialog = new WizardDialog(viewer.getControl().getShell(), wizard);
		        try {
		        	wizard.init(Activator.getDefault().getWorkbench(), (IStructuredSelection)null);
		        	dialog.create();
		        	dialog.open();
		        	
	        		viewer.refresh(true);
		        } catch (Throwable t) {
		        	t.printStackTrace();
		        }
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}