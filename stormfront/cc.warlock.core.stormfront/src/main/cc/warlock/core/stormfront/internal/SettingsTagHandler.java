package cc.warlock.core.stormfront.internal;

import java.io.File;
import java.io.FileOutputStream;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.configuration.ConfigurationUtil;
import cc.warlock.core.stormfront.IStormFrontProtocolHandler;
import cc.warlock.core.stormfront.client.IStormFrontClientViewer;
import cc.warlock.core.stormfront.xml.StormFrontAttributeList;


public class SettingsTagHandler extends DefaultTagHandler {

	private StringBuffer buffer = new StringBuffer();
	private SettingsInfoTagHandler infoTagHandler;
	
	public SettingsTagHandler(IStormFrontProtocolHandler handler, SettingsInfoTagHandler infoTagHandler) {
		super(handler);
		this.infoTagHandler = infoTagHandler;
		
		for(String tagName : settingsTags) {
			addTagHandler(tagName, this);
		}
	}

	private static interface ViewerVisitor {
		public void visit (IStormFrontClientViewer viewer);
	}
	
	private void visitViewers (ViewerVisitor visitor)
	{
		for (IWarlockClientViewer viewer : handler.getClient().getViewers())
		{
			if (viewer instanceof IStormFrontClientViewer)
			{
				IStormFrontClientViewer sfViewer = (IStormFrontClientViewer) viewer;
				visitor.visit(sfViewer);
			}
		}
	}
	
	private static final String[] onlySettingsTag = new String[] { "settings" };
	private static final String[] settingsTags = new String[] {
		"settings", "presets", "strings", "stream", "scripts",
		"names", "macros", "palette", "vars", "dialog",
		"panels", "toggles", "misc", "options", "cmdline",
		"keys", "p", "k", "i", "w", "h", "k", "v", "s", "m", "o",
		"font", "columnFont", "detach", "ignores", "builtin",
		"group", "toggles", "app", "display"};
	
	@Override
	public String[] getTagNames() {
		return onlySettingsTag;
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes, String newLine) {
		if ("settings".equals(getCurrentTag()))
		{	
			buffer.setLength(0);	
			handler.startSavingRawXML(buffer, "settings");
			visitViewers(new ViewerVisitor() {
				public void visit(IStormFrontClientViewer viewer) {
					viewer.startDownloadingServerSettings();
				}
			});
		}
	}

	@Override
	public void handleEnd(String newLine) {
		if ("settings".equals(getCurrentTag()))
		{
			handler.stopSavingRawXML();
			buffer.insert(0, "<settings>\n");
			buffer.append("</settings>");
			
			String settings = "<settings>";
			int index = buffer.indexOf(settings);
			buffer = buffer.replace(index, index+settings.length(),
				"<settings crc=\"" + infoTagHandler.getCRC() +
				"\" major=\"" + infoTagHandler.getMajorVersion() +
				"\" client=\"" + infoTagHandler.getClientVersion() + "\">");
			
			String playerId = handler.getClient().getPlayerId().get();
			File serverSettings = ConfigurationUtil.getConfigurationFile("serverSettings_" + playerId + ".xml");
			
			try {
	
				FileOutputStream stream = new FileOutputStream(serverSettings);
				stream.write(buffer.toString().getBytes());
				stream.close();
				buffer = null;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			handler.getClient().getServerSettings().load(handler.getClient().getPlayerId().get());
			visitViewers(new ViewerVisitor() {
				public void visit(IStormFrontClientViewer viewer) {
					viewer.finishedDownloadingServerSettings();
				}
			});
		}
		else 
		{
			IStormFrontClientViewer.SettingType setting = null;
			if ("presets".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Presets;
			else if ("strings".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Strings;
			else if ("stream".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Streams;
			else if ("scripts".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Scripts;
			else if ("names".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Names;
			else if ("macros".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Macros;
			else if ("palette".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Palette;
			else if ("vars".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Vars;
			else if ("dialog".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Dialogs;
			else if ("panels".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Panels;
			else if ("toggles".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Toggles;
			else if ("misc".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Misc;
			else if ("options".equals(getCurrentTag())) setting = IStormFrontClientViewer.SettingType.Options;
			
			if (setting !=  null)
			{
				final IStormFrontClientViewer.SettingType finalSetting = setting;
				visitViewers(new ViewerVisitor() {
					public void visit(IStormFrontClientViewer viewer) {
						viewer.receivedServerSetting(finalSetting);
					}
				});
			}
		}
	}
	
	@Override
	public boolean handleCharacters(String characters) {
		return true;
	}
}
