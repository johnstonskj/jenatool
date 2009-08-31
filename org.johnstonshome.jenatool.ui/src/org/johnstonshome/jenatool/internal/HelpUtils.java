package org.johnstonshome.jenatool.internal;

import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.johnstonshome.jenatool.ui.Activator;

public class HelpUtils {

	public static void setHelp(Control control,  String id) {
		final String qualifiedHelpId = String.format("%s.%s", Activator.PLUGIN_ID, id);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(control, qualifiedHelpId);
	}
}
