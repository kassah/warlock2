package cc.warlock.rcp.ui.macros.internal;

import java.util.Date;

import org.eclipse.swt.SWT;

import cc.warlock.client.ICommand;
import cc.warlock.client.IWarlockClient;
import cc.warlock.client.IWarlockClientViewer;
import cc.warlock.client.internal.Command;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.IMacroHandler;
import cc.warlock.rcp.ui.macros.MacroRegistry;

public class CommandHistoryMacroHandler implements IMacroHandler {

	protected IMacro upMacro, downMacro, sendMacro;
	
	public CommandHistoryMacroHandler ()
	{
		upMacro = new Macro(SWT.ARROW_UP);
		downMacro = new Macro(SWT.ARROW_DOWN);
		sendMacro = new Macro(SWT.CR);
		
		upMacro.addHandler(this);
		downMacro.addHandler(this);
		sendMacro.addHandler(this);
	}
	
	public IMacro[] getMacros ()
	{
		return new IMacro[] { upMacro, downMacro, sendMacro };
	}
	
	public boolean handleMacro (IMacro macro, IWarlockClientViewer viewer)
	{
		if (macro.equals(upMacro))
		{
			handleUp(viewer);
		}
		else if (macro.equals(downMacro))
		{
			handleDown(viewer);
		}
		else if (macro.equals(sendMacro))
		{
			handleSend(viewer);
		}
		
		return true;
	}
	
	public void handleUp (IWarlockClientViewer viewer) 
	{
		IWarlockClient client = viewer.getWarlockClient();
		ICommand currentCommand = viewer.getCurrentCommand();
		if (currentCommand != null && !currentCommand.isInHistory())
		{
			client.getCommandHistory().addCommand(currentCommand);
		}
		
		ICommand command = client.getCommandHistory().prev();
		viewer.setCurrentCommand(command);
	}
	
	public void handleDown (IWarlockClientViewer viewer)
	{
		IWarlockClient client = viewer.getWarlockClient();
		ICommand currentCommand = viewer.getCurrentCommand();
		if (currentCommand != null && !currentCommand.isLast())
		{
			ICommand command = client.getCommandHistory().next();
			viewer.setCurrentCommand(command);
		}
	}

	public void handleSend (IWarlockClientViewer viewer)
	{
		String command = viewer.getCurrentCommand().getCommand();
		viewer.getWarlockClient().send(command);
		viewer.getWarlockClient().getDefaultStream().echo(command);
		
		viewer.setCurrentCommand(new Command("", new Date()));
	}
}
