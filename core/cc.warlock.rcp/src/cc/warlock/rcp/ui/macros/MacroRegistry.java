/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientListener;
import cc.warlock.core.client.WarlockClientRegistry;
import cc.warlock.core.client.settings.IClientSetting;
import cc.warlock.core.client.settings.macro.CommandMacroHandler;
import cc.warlock.core.client.settings.macro.IMacro;
import cc.warlock.core.client.settings.macro.IMacroCommand;
import cc.warlock.core.client.settings.macro.IMacroHandler;
import cc.warlock.core.client.settings.macro.IMacroProvider;
import cc.warlock.core.client.settings.macro.IMacroVariable;
import cc.warlock.core.client.settings.macro.internal.Macro;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.ui.macros.internal.SystemMacros;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MacroRegistry implements IMacroProvider {
	private static MacroRegistry instance;
	
	private ArrayList<IMacro> macros;
	private Hashtable<String,IMacroVariable> variables;
	private Hashtable<String,IMacroCommand> commands;
	
	MacroRegistry () {
		macros = new ArrayList<IMacro>();
		variables = new Hashtable<String,IMacroVariable>();
		commands = new Hashtable<String,IMacroCommand>();
	}
	
	protected void init() {
		loadMacros();
		loadVariables();
		loadCommands();
	}
	
	public static MacroRegistry instance ()
	{
		if (instance == null) {
			instance = new MacroRegistry();
			instance.init();
			
			WarlockClientRegistry.addWarlockClientListener(new IWarlockClientListener() {
				public void clientActivated(IWarlockClient client) {}
				public void clientConnected(IWarlockClient client) {
					client.getClientSettings().addClientSettingProvider(instance);
				}
				public void clientDisconnected(IWarlockClient client) {}
				public void clientRemoved(IWarlockClient client) {}
			});
		}
		
		return instance;
	}
	
	public static IMacro createCommandMacro(int keyCode, String command)
	{
		return createMacro(keyCode, new CommandMacroHandler(command));
	}
	
	public static IMacro createCommandMacro(int keyCode, int modifiers, String command)
	{
		return createMacro(keyCode, modifiers, new CommandMacroHandler(command));
	}
	
	public static IMacro createMacro (int keyCode, IMacroHandler handler)
	{
		return createMacro(keyCode, IMacro.NO_MODIFIERS, handler);
	}
	
	public static IMacro createMacro (int keyCode, int modifiers, IMacroHandler handler)
	{
		Macro macro = new Macro(instance(), keyCode, modifiers);
		macro.addHandler(handler);
		
		return macro;
	}
	
	public List<IMacro> getMacros ()
	{
		return macros;
	}
	
	public Collection<IMacroVariable> getMacroVariables ()
	{
		return variables.values();
	}
	
	public Collection<IMacroCommand> getMacroCommands() {
		return commands.values();
	}
	
	public void addMacro (int keycode, String command)
	{
		macros.add(createCommandMacro(keycode, command));
	}
	
	public void addMacro (IMacro macro)
	{
		macros.add(macro);
	}
	
	private void loadMacros () {
		for (IMacro macro : SystemMacros.getSystemMacros())
		{
			macros.add(macro);
		}
	}
	
	public IMacro getMacro(int keycode, int modifiers) {
		for (IMacro macro : macros)
		{
			if (macro.getKeyCode() == keycode && macro.getModifiers() == modifiers) {
				return macro;
			}
		}
		return null;
	}
	
	public IMacroVariable getMacroVariable(String id) {
		return variables.get(id);
	}
	
	public IMacroCommand getMacroCommand(String id) {
		return commands.get(id);
	}
	
	public List<? extends IClientSetting> getSettings() {
		return macros;
	}
	
	private void loadCommands () {
		try {
			IExtension[] extensions = Warlock2Plugin.getDefault().getExtensions("cc.warlock.rcp.macroCommands");
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
	
	
}
