package cc.warlock.rcp.stormfront.ui.script;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;
import cc.warlock.core.stormfront.serversettings.server.IServerScriptProvider;

public class ServerScriptPersistableFactory implements IElementFactory {

	public static final String FACTORY_ID = "cc.warlock.rcp.stormfront.serverScriptPersistableFactory";
	private static final String TAG_SCRIPTNAME = "warlock.scriptname";
	private static final String TAG_PLAYERID = "warlock.playerid";
	
	public static void saveState (IMemento memento, ServerScriptEditorInput input)
	{
		memento.putString(TAG_SCRIPTNAME, input.getScriptInfo().getScriptName());
		memento.putString(TAG_PLAYERID, input.getScriptInfo().getClient().getPlayerId().get());
	}
	
	public IAdaptable createElement(IMemento memento) {
		String scriptName = memento.getString(TAG_SCRIPTNAME);
		String playerId = memento.getString(TAG_PLAYERID);
		
		for (IScriptProvider provider: ScriptEngineRegistry.getScriptProviders())
		{
			if (provider instanceof IServerScriptProvider)
			{
				IServerScriptProvider p = (IServerScriptProvider) provider;
				if (p.getClient().getPlayerId() != null && p.getClient().getPlayerId().equals(playerId))
				{
					for (IScriptInfo info : p.getScriptInfos())
					{
						IServerScriptInfo scriptInfo = (IServerScriptInfo)info;
						if (scriptInfo.getScriptName().equals(scriptName))
						{
							return new ServerScriptEditorInput(scriptInfo);
						}
					}
				}
			}
		}
		return null;
	}

}
