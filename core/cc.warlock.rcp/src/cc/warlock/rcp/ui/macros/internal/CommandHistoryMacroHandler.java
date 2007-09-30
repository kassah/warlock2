package cc.warlock.rcp.ui.macros.internal;

import java.util.Date;

import org.eclipse.swt.SWT;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.internal.Command;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.IMacroHandler;

public class CommandHistoryMacroHandler implements IMacroHandler {

	protected IMacro upMacro, downMacro, sendMacro;
	protected IMacro sendLastCommandMacro, sendNextToLastCommandMacro;
	
	public CommandHistoryMacroHandler ()
	{
		upMacro = new Macro(SWT.ARROW_UP);
		downMacro = new Macro(SWT.ARROW_DOWN);
		sendMacro = new Macro(SWT.CR);
		sendLastCommandMacro = new Macro(SWT.CR, SWT.CTRL);
		sendNextToLastCommandMacro = new Macro(SWT.CR, SWT.ALT);
		
		upMacro.addHandler(this);
		downMacro.addHandler(this);
		sendMacro.addHandler(this);
		sendLastCommandMacro.addHandler(this);
		sendNextToLastCommandMacro.addHandler(this);
	}
	
	public IMacro[] getMacros ()
	{
		return new IMacro[] { upMacro, downMacro, sendMacro, sendLastCommandMacro, sendNextToLastCommandMacro };
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
		else if (macro.equals(sendLastCommandMacro))
		{
			handleSendLastCommand(viewer);
		}
		else if (macro.equals(sendNextToLastCommandMacro))
		{
			handleSendNextToLastCommand(viewer);
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
		if (!command.equals(""))
		{
			viewer.getWarlockClient().getDefaultStream().echo(command);
			viewer.getWarlockClient().send(command);
			
			viewer.setCurrentCommand(new Command("", new Date()));
		}
	}
	
	public void handleSendLastCommand (IWarlockClientViewer viewer)
	{
		ICommand command = viewer.getWarlockClient().getCommandHistory().getLastCommand();
		viewer.getWarlockClient().send(command);
		viewer.getWarlockClient().getDefaultStream().echo(command.getCommand());
	}
	
	public void handleSendNextToLastCommand (IWarlockClientViewer viewer)
	{
		ICommandHistory history = viewer.getWarlockClient().getCommandHistory();
		
		if (history.size() >= 2)
		{
			ICommand command = history.getCommandAt(history.size() - 2);
			
			viewer.getWarlockClient().send(command);
			viewer.getWarlockClient().getDefaultStream().echo(command.getCommand());
		}
	}
}
