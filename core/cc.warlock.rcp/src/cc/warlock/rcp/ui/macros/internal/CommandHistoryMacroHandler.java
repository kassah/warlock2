package cc.warlock.rcp.ui.macros.internal;

import org.eclipse.swt.SWT;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.WarlockEntry;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.IMacroHandler;

public class CommandHistoryMacroHandler implements IMacroHandler {

	//protected IMacro upMacro, downMacro, 
	private IMacro sendMacro;
	//protected IMacro sendLastCommandMacro, sendNextToLastCommandMacro;
	
	public CommandHistoryMacroHandler ()
	{
		
		//upMacro = new Macro(SWT.ARROW_UP);
		//downMacro = new Macro(SWT.ARROW_DOWN);
		sendMacro = new Macro(SWT.CR);
		//sendLastCommandMacro = new Macro(SWT.CR, SWT.CTRL);
		//sendNextToLastCommandMacro = new Macro(SWT.CR, SWT.ALT);
		
		//upMacro.addHandler(this);
		//downMacro.addHandler(this);
		sendMacro.addHandler(this);
		//sendLastCommandMacro.addHandler(this);
		//sendNextToLastCommandMacro.addHandler(this);
	}
	
	public IMacro[] getMacros ()
	{
		//return new IMacro[] { upMacro, downMacro, sendMacro, sendLastCommandMacro, sendNextToLastCommandMacro };
		return new IMacro[] { sendMacro };
	}
	
	public boolean handleMacro (IMacro macro, IWarlockClientViewer viewer)
	{
		/*if (macro.equals(upMacro))
		{
			handleUp(viewer);
		}
		else if (macro.equals(downMacro))
		{
			handleDown(viewer);
		}
		else*/
		if (macro.equals(sendMacro))
		{
			handleSend(viewer);
			return true;
		}
		/*else if (macro.equals(sendLastCommandMacro))
		{
			handleSendLastCommand(viewer);
		}
		else if (macro.equals(sendNextToLastCommandMacro))
		{
			handleSendNextToLastCommand(viewer);
		}*/
		
		return false;
	}
	
	public void handleUp (IWarlockClientViewer viewer) 
	{
		viewer.prevCommand();
	}
	
	public void handleDown (IWarlockClientViewer viewer)
	{
		viewer.nextCommand();
	}

	public void handleSend (IWarlockClientViewer viewer)
	{
		viewer.submit();
	}
	
	/*public void handleSendLastCommand (IWarlockClientViewer viewer)
	{
		viewer.submitLastCommand();
	}
	
	public void handleSendNextToLastCommand (IWarlockClientViewer viewer)
	{
		viewer.submitNextToLastCommand();
	}*/
}
