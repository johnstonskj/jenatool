package org.johnstonshome.jenatool.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.FontData;
import org.johnstonshome.jenatool.ui.Activator;

public class PluginPreferences {
	
	private static List<String> rdfForms = new ArrayList<String>();
	
	static {
		rdfForms.add("N-Triples");
		rdfForms.add("Turtle");
		rdfForms.add("N3");
		rdfForms.add("RDF/XML");
	}
	
	private IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	public void setUseDefaultGraph(boolean useDefaultGraph) {
		store.setValue(PreferenceConstants.P_DEFAULT_CONTEXT, useDefaultGraph == true ? "union" : "default");
	}
	
	public boolean isUseDefaultGraph() {
		return store.getString(PreferenceConstants.P_DEFAULT_CONTEXT).equals("union");
	}
	
	public void setDefaultRdfForm(String defaultRdfForm) {
		store.setValue(PreferenceConstants.P_RDF_RESULT_FORM, defaultRdfForm);
	}
	
	public String getDefaultRdfForm() {
		return store.getString(PreferenceConstants.P_RDF_RESULT_FORM);
	}

	public FontData getResultsViewFont() {
		FontData fd = PreferenceConverter.getFontData(store, PreferenceConstants.P_RESULTS_VIEW_FONT);
		return fd;
	}
	
	public void setResultsViewFont(FontData fontData) {
		PreferenceConverter.setValue(store, PreferenceConstants.P_RESULTS_VIEW_FONT, fontData);
	}
	
	public final List<String> getRdfForms() {
		return PluginPreferences.rdfForms;
	}
}
