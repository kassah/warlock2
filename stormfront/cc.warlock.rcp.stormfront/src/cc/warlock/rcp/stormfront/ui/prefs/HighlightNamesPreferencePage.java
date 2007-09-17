package cc.warlock.rcp.stormfront.ui.prefs;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import cc.warlock.core.stormfront.serversettings.server.HighlightString;

public class HighlightNamesPreferencePage extends
		HighlightStringsPreferencePage {

	public static final String PAGE_ID = "cc.warlock.rcp.prefs.highlightNames";
	
	@Override
	protected String getDisplayName() {
		return "Highlight Names";
	}
	
	protected ViewerFilter getViewerFilter ()
	{
		return new ViewerFilter ()
		{
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return ((HighlightString)element).isName();
			}
		};
	}
	
	@Override
	protected HighlightString createHighlightString ()
	{
		 HighlightString newString = client.getServerSettings().createHighlightString(true);
		 newString.setText("<Highlight Name>");
		 return newString;
	}
	
	@Override
	protected void saveSettings() {
		client.getServerSettings().saveHighlightNames();
	}
}
