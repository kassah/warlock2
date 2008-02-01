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
package cc.warlock.rcp.ui.macros;

import java.util.Map;

import cc.warlock.core.client.IWarlockClientViewer;

public class CommandMacroHandler implements IMacroHandler {

	protected String command;
	
	public CommandMacroHandler (String command)
	{
		this.command = command;
	}
	
	public boolean handleMacro(IMacro macro, IWarlockClientViewer viewer)
	{
		Map<String, IMacroVariable> variables = MacroRegistry.instance().getMacroVariables();
		
		String newCommand = new String(command);
		String savedCommand = null;
		
		for (String var : variables.keySet())
		{
			if (newCommand.contains(var))
			{
				String newVar = var.replaceAll("\\$", "\\\\\\$");
				String value = variables.get(var).getValue(viewer);
				if (value != null)
				{
					newCommand = newCommand.replaceAll(newVar, value);
				}
			}
		}
		
		for (int pos = 0; pos < newCommand.length(); pos++) {
			char curChar = newCommand.charAt(pos);
			if(curChar == '\\' && newCommand.length() > pos + 1) {
				pos++;
				curChar = newCommand.charAt(pos);
				switch(curChar) {
				
				// submit current text in entry
				case 'n':
				case 'r':
					viewer.submit();
					break;
					
				// pause 1 second
				case 'p':
					// not sure how to implement pause
					break;
					
				// clear the entry
				case 'x':
					viewer.setCurrentCommand("");
					break;
					
				// display a dialog to get the value
				case '?':
					// unimplemented
					break;
					
				// save current text in entry
				case 'S':
					savedCommand = viewer.getCurrentCommand();
					break;
					
				// restore saved command
				case 'R':
					if(savedCommand != null)
						viewer.setCurrentCommand(savedCommand);
					break;
					
				default:
					viewer.append(curChar);
				}
			} else if(curChar == '{') {
				int endPos = newCommand.indexOf('}', pos);
				if(endPos == -1) {
					viewer.append(curChar);
				} else {
					String commandText = newCommand.substring(pos + 1, endPos);
					pos = endPos + 1;
					for(Map.Entry<String, IMacroCommand> macroCommand : MacroRegistry.instance().getMacroCommands().entrySet()) {
						if(commandText.equals(macroCommand.getKey())) {
							macroCommand.getValue().execute(viewer);
						}
					}
				}
			} else {
				viewer.append(curChar);
			}
		}
		return true;
	}
	
	public String getCommand () {
		return this.command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	@Override
	public String toString() {
		return "Command(" + command + ")";
	}
}
