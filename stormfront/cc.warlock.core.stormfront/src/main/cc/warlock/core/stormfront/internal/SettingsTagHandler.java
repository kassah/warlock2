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
		
		addTagHandler(new SettingsElementsTagHandler(handler, this));
	}

	protected static interface ViewerVisitor {
		public void visit (IStormFrontClientViewer viewer);
	}
	
	protected void visitViewers (ViewerVisitor visitor)
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
	
	@Override
	public String[] getTagNames() {
		return new String[] { "settings" };
	}
	
	@Override
	public void handleStart(StormFrontAttributeList attributes) {

		buffer.setLength(0);	
		handler.startSavingRawXML(buffer, "settings");
		visitViewers(new ViewerVisitor() {
			public void visit(IStormFrontClientViewer viewer) {
				viewer.startDownloadingServerSettings();
			}
		});
	}

	@Override
	public void handleEnd() {

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
	
	@Override
	public boolean handleCharacters(String characters) {
		return true;
	}
}
