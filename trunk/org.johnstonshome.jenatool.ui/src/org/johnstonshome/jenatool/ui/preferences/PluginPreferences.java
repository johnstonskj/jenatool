package org.johnstonshome.jenatool.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.FontData;
import org.johnstonshome.jenatool.ui.Activator;

public class PluginPreferences {
	
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

	public FontData getResultsViewFont() {
		//yourFont.dispose(); 
		FontData fd = PreferenceConverter.getFontData(store, PreferenceConstants.P_RESULTS_VIEW_FONT);
		//yourFont = new Font(null, fd);
		return fd;
	}
	
	public void setResultsViewFont(FontData fontData) {
		PreferenceConverter.setValue(store, PreferenceConstants.P_RESULTS_VIEW_FONT, fontData);
	}
}
