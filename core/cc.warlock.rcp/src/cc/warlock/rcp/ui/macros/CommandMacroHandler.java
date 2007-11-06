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
