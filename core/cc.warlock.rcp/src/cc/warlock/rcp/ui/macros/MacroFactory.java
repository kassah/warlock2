/*
 * Created on Mar 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.ui.macros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.ui.macros.internal.Macro;
import cc.warlock.rcp.ui.macros.internal.SystemMacros;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MacroFactory {

	private static Preferences prefs = Preferences.userNodeForPackage(IMacro.class);
	
	private static final String PREF_NUM_MACROS = "num-macros";
	private static final String PREF_MACRO_PREFIX = "macro-";
	private static final String PREF_MACRO_KEYCODE = "-keycode";
	private static final String PREF_MACRO_COMMAND = "-command";
	private static MacroFactory instance;
	
	private ArrayList<IMacro> macros;
	private Hashtable<String,IMacroVariable> variables;
	private Hashtable<String,IMacroCommand> commands;
	
	MacroFactory () {
		macros = new ArrayList<IMacro>();
		variables = new Hashtable<String,IMacroVariable>();
		commands = new Hashtable<String,IMacroCommand>();
		
		loadMacros();
		loadVariables();
		loadCommands();
	}
	
	public static MacroFactory instance ()
	{
		if (instance == null)
			instance = new MacroFactory();
		
		return instance;
	}
	
	public Collection<IMacro> getMacros ()
	{
		return macros;
	}
	
	public Map<String,IMacroVariable> getMacroVariables ()
	{
		return variables;
	}
	
	public Map<String,IMacroCommand> getMacroCommands ()
	{
		return commands;
	}
	
	public void addMacro (int keycode, String command)
	{
		macros.add(new Macro(keycode, command));
	}
	
	public void save ()
	{
		prefs.putInt(PREF_NUM_MACROS, macros.size());
		
		int i = 0;
		for (IMacro macro : macros)
		{
			prefs.putInt(PREF_MACRO_PREFIX + i + PREF_MACRO_KEYCODE, macro.getKeyCode());
			prefs.put(PREF_MACRO_PREFIX + i + PREF_MACRO_COMMAND, macro.getCommand());
			i++;
		}
	}
	
	private void loadMacros () {
		int numberOfMacros = prefs.getInt(PREF_NUM_MACROS, 0);
		for (int i = 0; i < numberOfMacros; i++)
		{
			int keycode = prefs.getInt(PREF_MACRO_PREFIX + i + PREF_MACRO_KEYCODE, 0);
			String command = prefs.get(PREF_MACRO_PREFIX + i + PREF_MACRO_COMMAND, null);
			
			addMacro(keycode, command);
		}
		
		for (IMacro macro : SystemMacros.getSystemMacros())
		{
			macros.add(macro);
		}
	}
	
	private void loadCommands () {
		try {
			IExtension[] extensions = Warlock2Plugin.getDefault().getExtensions("com.arcaner.warlock.rcp.macroCommands");
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				IConfigurationElement[] ce = ext.getConfigurationElements();
				
				for (int j = 0; j < ce.length; j++) {
					Object obj = ce[j].createExecutableExtension("classname");
					
					if (obj instanceof IMacroCommand)
					{
						IMacroCommand command = (IMacroCommand) obj;
						commands.put(command.getIdentifier(), command);
					}
				}
			}
		} catch (InvalidRegistryObjectException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private void loadVariables () {
		try {
			IExtension[] extensions = Warlock2Plugin.getDefault().getExtensions("com.arcaner.warlock.rcp.macroVariables");
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
}
