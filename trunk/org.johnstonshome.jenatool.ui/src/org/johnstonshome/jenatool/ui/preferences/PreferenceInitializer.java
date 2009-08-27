package org.johnstonshome.jenatool.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import org.johnstonshome.jenatool.ui.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_USE_DEFAULT_UNION, true);
		store.setDefault(PreferenceConstants.P_RDF_RESULT_FORM, "N3");
		PreferenceConverter.setDefault(store, PreferenceConstants.P_RESULTS_VIEW_FONT, new FontData("Courier New", 10, SWT.NORMAL));
	}

}
