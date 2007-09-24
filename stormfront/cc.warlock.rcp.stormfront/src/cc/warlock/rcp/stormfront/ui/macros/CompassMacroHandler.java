package cc.warlock.rcp.stormfront.ui.macros;

import org.eclipse.swt.SWT;

import cc.warlock.core.client.ICharacterStatus;
import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.IMacroHandler;
import cc.warlock.rcp.ui.macros.MacroRegistry;

public class CompassMacroHandler implements IMacroHandler {

	protected static CompassMacroHandler _instance;
	
	public static CompassMacroHandler instance() {
		if (_instance == null)
			_instance = new CompassMacroHandler();
		return _instance;
	}
	
	protected static final int[] keycodes = new int[] {
		SWT.KEYPAD_0, SWT.KEYPAD_1,
		SWT.KEYPAD_2, SWT.KEYPAD_3,
		SWT.KEYPAD_4, SWT.KEYPAD_5,
		SWT.KEYPAD_6, SWT.KEYPAD_7,
		SWT.KEYPAD_8, SWT.KEYPAD_9,
		SWT.KEYPAD_DECIMAL
	};
	
	protected static final int[] modifiers = new int[] {0, SWT.CTRL};
	
	public CompassMacroHandler ()
	{
		for (int mod : modifiers)
		{
			for (int keycode : keycodes)
			{
				IMacro macro = MacroRegistry.createMacro(keycode, mod, this);
				MacroRegistry.instance().addMacro(macro);		
			}
		}
	}
	
	public boolean handleMacro(IMacro macro, IWarlockClientViewer viewer) {
		String direction = null;
		
		switch (macro.getKeyCode())
		{
		case SWT.KEYPAD_0: direction = "down"; break;
		case SWT.KEYPAD_1: direction = "sw"; break;
		case SWT.KEYPAD_2: direction = "s"; break;
		case SWT.KEYPAD_3: direction = "se"; break;
		case SWT.KEYPAD_4: direction = "w"; break;
		case SWT.KEYPAD_5: direction = "out"; break;
		case SWT.KEYPAD_6: direction = "e"; break;
		case SWT.KEYPAD_7: direction = "nw"; break;
		case SWT.KEYPAD_8: direction = "n"; break;
		case SWT.KEYPAD_9: direction = "ne"; break;
		case SWT.KEYPAD_DECIMAL: direction = "up"; break;
		}
		
		String command = null;
		if (direction != null)
		{
			if (macro.getModifiers() == SWT.CTRL)
			{
				command = "peer " + direction;
			}
			else
			{
				command = direction;
				
				if (viewer.getWarlockClient() instanceof IStormFrontClient)
				{
					IStormFrontClient client = (IStormFrontClient) viewer.getWarlockClient();
					
					if (client.getCharacterStatus().getStatus().get(ICharacterStatus.StatusType.Hidden))
					{
						command = "sneak " + direction;
					}
				}
			}
			
			if (command != null)
			{
				viewer.getWarlockClient().send(command);
				viewer.getWarlockClient().getDefaultStream().echo(command);
				return true;
			}
		}
		return false;
	}

}
