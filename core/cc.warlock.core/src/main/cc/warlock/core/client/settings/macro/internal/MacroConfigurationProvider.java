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
package cc.warlock.core.client.settings.macro.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import cc.warlock.core.client.settings.internal.ClientConfigurationProvider;
import cc.warlock.core.client.settings.macro.CommandMacroHandler;
import cc.warlock.core.client.settings.macro.IMacro;
import cc.warlock.core.client.settings.macro.IMacroHandler;
import cc.warlock.core.client.settings.macro.IMacroProvider;
import cc.warlock.core.stormfront.IMacroCommand;
import cc.warlock.rcp.macro.IMacroVariable;

/**
 * Macros defined by this provider are command-based only. 
 * Everything else will be a system macro, probably needing special configuration, or more foresight in this class.
 *  
 * @author marshall
 */
public class MacroConfigurationProvider extends ClientConfigurationProvider implements IMacroProvider {

	protected ArrayList<IMacro> macros = new ArrayList<IMacro>();
	protected HashMap<String, IMacroVariable> variables = new HashMap<String, IMacroVariable>();
	protected HashMap<String, IMacroCommand> commands = new HashMap<String, IMacroCommand>();
	
	public MacroConfigurationProvider ()
	{
		super("macros");
		
		setHandleChildren(false);
	}
	
	public void addMacro(IMacro macro) {
		if (!macros.contains(macro)) {
			macros.add(macro);
		}
	}

	public IMacro getMacro(int keycode, int modifiers) {
		for (IMacro macro : macros) {
			if (macro.getKeyCode() == keycode && macro.getModifiers() == modifiers) {
				return macro;
			}
		}
		return null;
	}

	public List<? extends IMacro> getMacros() {
		return macros;
	}

	public void removeMacro(IMacro macro) {
		if (macros.contains(macro)) {
			macros.remove(macro);
		}
	}
	
	public void replaceMacro(IMacro originalMacro, IMacro newMacro) {
		int index = macros.indexOf(originalMacro);
		if (index > -1) {
			macros.set(index, newMacro);
		}
	}

	public void addMacroVariable(IMacroVariable var) {
		setMacroVariable(var.getIdentifier(), var);
	}

	public IMacroVariable getMacroVariable(String id) {
		if (variables.containsKey(id)) {
			return variables.get(id);
		}
		return null;
	}
	
	public void removeMacroVariable(IMacroVariable variable) {
		if (variables.containsKey(variable.getIdentifier())) {
			variables.remove(variable.getIdentifier());
		}
	}

	public Collection<IMacroVariable> getMacroVariables() {
		return variables.values();
	}

	public void setMacroVariable(String id, IMacroVariable var) {
		variables.put(id, var);
	}

	@Override
	protected void parseData() {}

	@Override
	protected void parseChild(Element child) {
		if (child.getName().equals("macro"))
		{
			String command = child.attributeValue("command");
			int keycode = intValue(child, "keycode");
			int modifiers = intValue(child, "modifiers");
			
			Macro macro = new Macro(this, keycode, modifiers);
			macro.addHandler(new CommandMacroHandler(command));
			
			macros.add(macro);
		}
	}
	
	@Override
	protected void saveTo(List<Element> elements) {
		Element macrosElement = DocumentHelper.createElement("macros");
		for (IMacro macro : macros)
		{
			if (macro.getHandlers().size() == 1) {
				
				IMacroHandler first = macro.getHandlers().iterator().next();
				if (first instanceof CommandMacroHandler) {
					CommandMacroHandler handler = (CommandMacroHandler)first;
					
					Element mElement = macrosElement.addElement("macro");
					mElement.addAttribute("command", handler.getCommand());
					mElement.addAttribute("keycode", macro.getKeyCode()+"");
					mElement.addAttribute("modifiers", macro.getModifiers()+"");
				}
			}
		}
		
		elements.add(macrosElement);
	}
	
	public void addMacroCommand (IMacroCommand command)
	{
		setMacroCommand(command.getIdentifier(), command);
	}

	public Collection<IMacroCommand> getMacroCommands() {
		return commands.values();
	}
	
	public void removeMacroCommand(IMacroCommand command) {
		if (commands.containsKey(command.getIdentifier())) {
			commands.remove(command.getIdentifier());
		}
	}
	
	public IMacroCommand getMacroCommand(String id) {
		if (commands.containsKey(id)) {
			return commands.get(id);
		}
		return null;
	}
	
	public void setMacroCommand (String id, IMacroCommand command) {
		commands.put(id, command);
	}
}
