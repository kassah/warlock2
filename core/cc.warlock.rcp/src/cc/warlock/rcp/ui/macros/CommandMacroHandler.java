package cc.warlock.rcp.ui.macros;

import java.util.Map;

import cc.warlock.client.IWarlockClientViewer;
import cc.warlock.client.internal.Command;

public class CommandMacroHandler implements IMacroHandler {

	protected String command;
	
	public CommandMacroHandler (String command)
	{
		this.command = command;
	}
	
	public boolean handleMacro(IMacro macro, IWarlockClientViewer viewer)
	{
		Map<String, IMacroVariable> variables = MacroRegistry.instance().getMacroVariables();
		Map<String, IMacroCommand> commands = MacroRegistry.instance().getMacroCommands();
		
		//context.getCurrentCommand() + 
		String newCommand = new String(command);
		
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
		
		for (String command : commands.keySet())
		{
			if (newCommand.contains(command))
			{
				for ( int i = newCommand.indexOf(command); i > -1 && i < newCommand.length(); i = newCommand.indexOf(command, i+1))
				{
					String result = commands.get(command).execute(viewer, newCommand, i);
					if (result != null)
					{
						newCommand = result;
					}
				}
			}
		}
		
		viewer.setCurrentCommand(new Command(newCommand));
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
