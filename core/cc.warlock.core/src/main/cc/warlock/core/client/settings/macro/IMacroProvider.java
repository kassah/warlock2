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
package cc.warlock.core.client.settings.macro;

import java.util.Collection;
import java.util.List;

import cc.warlock.core.client.settings.IClientSettingProvider;
import cc.warlock.core.stormfront.IMacroCommand;
import cc.warlock.rcp.macro.IMacroVariable;


public interface IMacroProvider extends IClientSettingProvider {
	public List<? extends IMacro> getMacros();
	
	public IMacro getMacro (int keycode, int modifiers);
	public void addMacro (IMacro macro);
	public void removeMacro (IMacro macro);
	public void replaceMacro (IMacro originalMacro, IMacro newMacro);
	
	public IMacroVariable getMacroVariable (String id);
	public Collection<IMacroVariable> getMacroVariables();
	public void setMacroVariable (String id, IMacroVariable variable);
	public void removeMacroVariable (IMacroVariable variable);
	
	public IMacroCommand getMacroCommand (String id);
	public Collection<IMacroCommand> getMacroCommands();
	public void addMacroCommand (IMacroCommand command);
	public void removeMacroCommand (IMacroCommand command);
}
