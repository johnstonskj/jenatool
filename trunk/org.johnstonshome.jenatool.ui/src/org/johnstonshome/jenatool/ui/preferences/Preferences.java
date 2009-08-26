package org.johnstonshome.jenatool.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.johnstonshome.jenatool.ui.Activator;

public class Preferences {
	
	private IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	public void setUseDefaultGraph(boolean useDefaultGraph) {
		store.setValue(PreferenceConstants.P_USE_DEFAULT_UNION, useDefaultGraph);
	}
	
	public boolean isUseDefaultGraph() {
		return store.getBoolean(PreferenceConstants.P_USE_DEFAULT_UNION);
	}
	
	public void setDefaultRdfForm(String defaultRdfForm) {
		store.setValue(PreferenceConstants.P_RDF_RESULT_FORM, defaultRdfForm);
	}
	
	public String getDefaultRdfForm() {
		return store.getString(PreferenceConstants.P_RDF_RESULT_FORM);
	}

}
