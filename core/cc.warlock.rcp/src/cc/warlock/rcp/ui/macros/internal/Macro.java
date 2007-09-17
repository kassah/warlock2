/*
 * Created on Mar 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.ui.macros.internal;

import java.util.ArrayList;
import java.util.Collection;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.rcp.ui.macros.IMacro;
import cc.warlock.rcp.ui.macros.IMacroHandler;

public class Macro implements IMacro
{
	protected int keycode;
	protected int modifiers;
	protected ArrayList<IMacroHandler> handlers;
	protected Object userData;
	
	public Macro (int keycode)
	{
		this (keycode, NO_MODIFIERS);
	}
	
	public Macro (int keycode, int modifiers)
	{
		this.keycode = keycode;
		this.modifiers = modifiers;
		this.handlers = new ArrayList<IMacroHandler>();
	}
	
	public int getKeyCode() {
		return this.keycode;
	}
	
	public void setKeyCode(int keycode) {
		this.keycode = keycode;
	}
	
	public int getModifiers() {
		return modifiers;
	}
	
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
	
	public void addHandler(IMacroHandler handler) {
		this.handlers.add(handler);
	}
	
	public void removeHandler(IMacroHandler handler) {
		if (this.handlers.contains(handler))
			this.handlers.remove(handler);
	}
	
	public Collection<IMacroHandler> getHandlers() {
		return this.handlers;
	}
	
	public void execute(IWarlockClientViewer viewer) {
		for (IMacroHandler handler : handlers)
		{
			handler.handleMacro(this, viewer);
		}
	}
	
	public Object getUserData() {
		return userData;
	}
	
	public void setUserData(Object object) {
		this.userData = object;
	}
	
	@Override
	public String toString() {
		String str = "Macro (keycode="+keycode+",modifiers="+modifiers+",handlers=(";
		for (IMacroHandler handler : handlers)
		{
			str += handler.toString() + ", ";
		}
		
		str += ")";
		return str;
	}
}