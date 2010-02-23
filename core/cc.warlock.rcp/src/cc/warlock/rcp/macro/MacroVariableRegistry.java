package cc.warlock.rcp.macro;

import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

import cc.warlock.core.client.settings.WarlockVariableProvider;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.views.GameView;

public class MacroVariableRegistry {
	private HashMap<String, IMacroVariable> variables = new HashMap<String, IMacroVariable>();
	
	private static MacroVariableRegistry instance;
	
	public static synchronized MacroVariableRegistry getInstance() {
		if(instance == null)
			instance = new MacroVariableRegistry();
		return instance;
	}
	
	private MacroVariableRegistry() {
		try {
			IExtension[] extensions = Warlock2Plugin.getDefault().getExtensions("cc.warlock.rcp.macroVariables");
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				IConfigurationElement[] ce = ext.getConfigurationElements();
				
				for (int j = 0; j < ce.length; j++) {
					Object obj = ce[j].createExecutableExtension("classname");
					
					if (obj instanceof IMacroVariable)
					{
						IMacroVariable var = (IMacroVariable) obj;
						variables.put(var.getIdentifier(), var);
					}
				}
			}
		} catch (InvalidRegistryObjectException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	public static String getMacroVariable(String id, GameView gameView) {
		IMacroVariable var = getInstance().variables.get(id);
		
		if(var != null)
			return var.getValue(gameView);
		
		return WarlockVariableProvider.getInstance().get(gameView.getWarlockClient().getClientPreferences(), id);
	}
}
