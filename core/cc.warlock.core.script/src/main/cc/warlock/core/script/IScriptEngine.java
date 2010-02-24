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
/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.core.script;

import java.util.Collection;

import cc.warlock.core.client.IWarlockClient;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IScriptEngine {
	
	/**
	 * This method will run the script at the given path. Note that this should return immediately, so the engine
	 * will need to probably create a thread to execute the script.
	 * 
	 * @param script the script to start
	 * @param client the client to start the script on
	 * @param argumetns The arguments passed to the script
	 */
	public IScript startScript(IScriptInfo info, IWarlockClient client, String[] arguments);
	
	public boolean supports (IScriptInfo scriptInfo);
	
	/**
	 * @return The unique id of this scripting engine.
	 */
	public String getScriptEngineId();
	
	/**
	 * @return A display name for this scripting engine.
	 */
	public String getScriptEngineName();
	
	/**
	 * @return a list of currently running scripts for this script engine.
	 */
	public Collection<? extends IScript> getRunningScripts();
	
	public Collection<String> getFileExtensions();
	
}
