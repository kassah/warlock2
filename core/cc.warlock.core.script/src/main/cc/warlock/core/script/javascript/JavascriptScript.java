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
package cc.warlock.core.script.javascript;

import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.script.AbstractScript;
import cc.warlock.core.script.IScriptCommands;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptInfo;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class JavascriptScript extends AbstractScript {

	private JavascriptEngine engine;
	private Context context;
	private IScriptCommands commands;
	
	public JavascriptScript (JavascriptEngine engine, IScriptInfo info, IWarlockClient client, IScriptCommands commands)
	{
		super(info, client);
		
		this.engine = engine;
		this.commands = commands;
	}

	public IScriptCommands getCommands() {
		return commands;
	}
	
	public Scriptable getScope() {
		return engine.scope;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public IScriptEngine getScriptEngine() {
		return engine;
	}
	
	public Reader getReader() {
		return reader;
	}
	
	public void execute(String command) { }
}
