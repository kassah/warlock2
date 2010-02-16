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
package cc.warlock.rcp.macro;

import java.util.Collection;
import java.util.HashMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.InvalidRegistryObjectException;

import cc.warlock.rcp.plugin.Warlock2Plugin;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MacroCommandRegistry {
	private static MacroCommandRegistry instance;
	
	// TODO - determine if variables and commands should be synchronized
	
	private HashMap<String, IMacroCommand> commands = new HashMap<String, IMacroCommand>();
	
	protected MacroCommandRegistry () {
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
	
	public static MacroCommandRegistry instance()
	{
		if (instance == null)
			instance = new MacroCommandRegistry();
		
		return instance;
	}
	
	public static Collection<IMacroCommand> getMacroCommands() {
		return instance().commands.values();
	}
	
	public static IMacroCommand getMacroCommand(String id) {
		return instance().commands.get(id);
	}
}
