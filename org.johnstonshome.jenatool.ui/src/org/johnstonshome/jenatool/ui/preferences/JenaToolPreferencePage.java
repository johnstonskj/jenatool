package org.johnstonshome.jenatool.ui.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.johnstonshome.jenatool.ui.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class JenaToolPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public JenaToolPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Preferences for the JenaTool plugin.");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new BooleanFieldEditor(
				PreferenceConstants.P_USE_DEFAULT_UNION,
				"&Use union-default-graph by default",
				getFieldEditorParent()));

		addField(new RadioGroupFieldEditor(
				PreferenceConstants.P_RDF_RESULT_FORM,
				"Default RDF representation for DESCRIBE and CONSTRUCT queries.",
				1,
				new String[][] { 
						{ "N-Triples", "N-TRIPLES" }, 
						{ "Turtle", "TURTLE" }, 
						{ "N3", "N3" }, 
						{ "RDF/XML", "RDF/XML-ABBREV" } }, 
				getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}