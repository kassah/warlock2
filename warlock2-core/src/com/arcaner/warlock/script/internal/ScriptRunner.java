/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.script.internal;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.arcaner.warlock.client.IWarlockClient;
import com.arcaner.warlock.plugin.Warlock2Plugin;
import com.arcaner.warlock.script.IScriptEngine;

/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScriptRunner {

	public static void runScript (IWarlockClient client, IPath scriptPath)
	{
		Collection<IScriptEngine> engines = Warlock2Plugin.getDefault().getScriptEngines();
		
		System.out.println("engines = " + engines);
		for (IScriptEngine engine : engines)
		{
			String path = scriptPath.toString();
			String extensions[] = engine.getSupportedExtensions();
			
			for (int i = 0; i < extensions.length; i++)
			{
				System.out.println("searching for '"+path+"."+extensions[i] +"'...");
				if (new File(path+"."+extensions[i]).exists())
				{
					System.out.println("Engine (" + engine.getScriptEngineName() + ") starting " + scriptPath);
					engine.startScript(client, new Path(path+"."+extensions[i]));
					return;	
				}
			}
		}	
	}
}
