package cc.warlock.rcp.ui.macros.internal;

import org.eclipse.swt.SWT;

import cc.warlock.core.client.ICommand;
import cc.warlock.core.client.ICommandHistory;
import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;
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
		
		ICommand command = client.getCommandHistory().prev();
		if(command != null)
			viewer.setCurrentCommand(command.getCommand());
		else
			viewer.setCurrentCommand("");
	}
	
	public void handleDown (IWarlockClientViewer viewer)
	{
		IWarlockClient client = viewer.getWarlockClient();
		ICommand command = client.getCommandHistory().next();
		if(command != null) {
			viewer.setCurrentCommand(command.getCommand());
		} else {
			viewer.setCurrentCommand("");
		}
	}

	public void handleSend (IWarlockClientViewer viewer)
	{
		String command = viewer.getCurrentCommand();
		if (!command.equals(""))
		{
			viewer.getWarlockClient().send(command);
			
			viewer.setCurrentCommand("");
		}
	}
	
	public void handleSendLastCommand (IWarlockClientViewer viewer)
	{
		ICommand command = viewer.getWarlockClient().getCommandHistory().getLastCommand();
		viewer.getWarlockClient().send(command.getCommand());
	}
	
	public void handleSendNextToLastCommand (IWarlockClientViewer viewer)
	{
		ICommandHistory history = viewer.getWarlockClient().getCommandHistory();
		
		if (history.size() >= 2)
		{
			ICommand command = history.getCommandAt(1);
			
			viewer.getWarlockClient().send(command.getCommand());
		}
	}
}
