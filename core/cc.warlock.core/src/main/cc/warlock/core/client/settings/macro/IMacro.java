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
 */package cc.warlock.core.client.settings.macro;

import java.util.Collection;

import cc.warlock.core.client.IWarlockClientViewer;
import cc.warlock.core.client.settings.IClientSetting;

/**
 * @author Marshall
 *
 * A macro represents a certain key code / modifier combination and the list of handlers (ordered by precedence) that are able/willing to handle the macro.
 */
public interface IMacro extends IClientSetting {

	public static final int NO_MODIFIERS = 0;
	
	public int getKeyCode ();
	public void setKeyCode (int keycode);
	
	public int getModifiers ();
	public void setModifiers (int modifiers);
	
	public void addHandler (IMacroHandler handler);
	public void removeHandler (IMacroHandler handler);
	
	public Collection<IMacroHandler> getHandlers();
	
	public void execute(IWarlockClientViewer viewer);
	
	public Object getUserData();
	public void setUserData(Object object);
}
