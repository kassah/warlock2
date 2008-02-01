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
package cc.warlock.core.script;

import java.io.Reader;
import java.util.ArrayList;

public abstract class AbstractScript implements IScript {

	protected ArrayList<IScriptListener> listeners;
	protected boolean suspended;
	protected Reader reader;
	protected IScriptInfo info;
	
	public AbstractScript (IScriptInfo info)
	{
		this.listeners = new ArrayList<IScriptListener>();
		
		this.info = info;
	}
	
	public void resume() {
		this.suspended = false;
		
		for (IScriptListener listener : listeners) listener.scriptResumed(this);
	}

	public void stop() {
		for (IScriptListener listener : listeners) listener.scriptStopped(this, true);
	}

	public void suspend() {
		this.suspended = true;
		
		for (IScriptListener listener : listeners) listener.scriptPaused(this);
	}
	
	public boolean isSuspended() {
		return suspended;
	}
	
	public void addScriptListener(IScriptListener listener) {
		listeners.add(listener);
	}
	
	public void removeScriptListener(IScriptListener listener) {
		if (listeners.contains(listener))
			listeners.remove(listener);
	}
	
	public String getName() {
		
		return info.getScriptName();
	}
	
	public IScriptInfo getScriptInfo() {
		return info;
	}
	
}
