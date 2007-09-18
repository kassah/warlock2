package cc.warlock.rcp.stormfront.ui.views;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockSkin;
import cc.warlock.core.client.WarlockColor;
import cc.warlock.core.configuration.Profile;
import cc.warlock.core.configuration.SavedProfiles;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.serversettings.server.ServerSettings;
import cc.warlock.rcp.application.WarlockApplication;
import cc.warlock.rcp.stormfront.adapters.SWTStormFrontClientViewer;
import cc.warlock.rcp.stormfront.ui.StormFrontMacros;
import cc.warlock.rcp.stormfront.ui.actions.ProfileConnectAction;
import cc.warlock.rcp.stormfront.ui.style.StormFrontStyleProvider;
import cc.warlock.rcp.util.ColorUtil;
import cc.warlock.rcp.views.GameView;

public class StormFrontGameView extends GameView implements IStormFrontClientViewer {
	
	public static final String VIEW_ID = "cc.warlock.rcp.stormfront.ui.views.StormFrontGameView";
	
	protected IStormFrontClient sfClient;
	
	public StormFrontGameView ()
	{
		super();

		wrapper = new SWTStormFrontClientViewer(this);
	}
	
	private ProgressMonitorDialog settingsProgressDialog;
	public void startDownloadingServerSettings() {
		settingsProgressDialog = new ProgressMonitorDialog(getSite().getShell());
		settingsProgressDialog.setBlockOnOpen(false);
		settingsProgressDialog.open();
		
		IProgressMonitor monitor = settingsProgressDialog.getProgressMonitor();
		monitor.beginTask("Downloading server settings...", SettingType.values().length);
	}
	
	public void receivedServerSetting(SettingType settingType)
	{
		IProgressMonitor monitor = settingsProgressDialog.getProgressMonitor();
		monitor.subTask("Downloading " + settingType.toString() + "...");
		
		monitor.worked(1);
	}
	
	public void finishedDownloadingServerSettings() {
		IProgressMonitor monitor = settingsProgressDialog.getProgressMonitor();
		monitor.done();
		settingsProgressDialog.close();
	}
	
	@Override
	public void setClient(IWarlockClient client) {
		super.setClient(client);
		
		if (client instanceof IStormFrontClient)
		{
			addStream(client.getStream(IStormFrontClient.DEATH_STREAM_NAME));
			sfClient = (IStormFrontClient) client;

			styleProvider = new StormFrontStyleProvider(sfClient.getServerSettings());
			
			compass.setCompass(sfClient.getCompass());
		}
	}
	
	protected Font normalFont;
	

	
	public void loadServerSettings (ServerSettings settings)
	{
		WarlockColor bg = settings.getColorSetting(IWarlockSkin.ColorType.MainWindow_Background);
		WarlockColor fg = settings.getColorSetting(IWarlockSkin.ColorType.MainWindow_Foreground);
		
		String fontFace = settings.getFontFaceSetting(IWarlockSkin.FontFaceType.MainWindow_FontFace);
		int fontSize = settings.getFontSizeSetting(IWarlockSkin.FontSizeType.MainWindow_FontSize);
		
		normalFont = fontFace == null ? JFaceResources.getDefaultFont() : new Font(getSite().getShell().getDisplay(), fontFace, fontSize, SWT.NONE);
		text.setFont(normalFont);
		
		WarlockColor entryBG = settings.getColorSetting(IWarlockSkin.ColorType.CommandLine_Background);
		WarlockColor entryFG = settings.getColorSetting(IWarlockSkin.ColorType.CommandLine_Foreground);
		entry.setForeground(ColorUtil.warlockColorToColor(entryFG.equals(WarlockColor.DEFAULT_COLOR) ? fg  : entryFG));
		entry.setBackground(ColorUtil.warlockColorToColor(entryBG.equals(WarlockColor.DEFAULT_COLOR) ? bg : entryBG));
		
		Caret newCaret = createCaret(1, ColorUtil.warlockColorToColor(settings.getColorSetting(IWarlockSkin.ColorType.CommandLine_BarColor)));
		entry.setCaret(newCaret);
		
		text.setBackground(ColorUtil.warlockColorToColor(bg));
		text.setForeground(ColorUtil.warlockColorToColor(fg));
		
		StormFrontMacros.addMacrosFromServerSettings(settings);
		
		if (HandsView.getDefault() != null)
		{
			HandsView.getDefault().loadServerSettings(settings);
		}
		if (StatusView.getDefault() != null)
		{
			StatusView.getDefault().loadServerSettings(settings);
		}
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if (IStormFrontClient.class.equals(adapter))
		{
			return client;
		}
		return super.getAdapter(adapter);
	}
	
	public IStormFrontClient getStormFrontClient() {
		return sfClient;
	}
}
