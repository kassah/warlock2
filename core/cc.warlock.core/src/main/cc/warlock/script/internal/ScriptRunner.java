/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.script.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;

import cc.warlock.script.IScriptCommands;
import cc.warlock.script.IScriptEngine;


/**
 * @author Marshall
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScriptRunner {

	public static void runScriptFromFile (IScriptCommands commands, File baseDir, String scriptName, String[] arguments)
	{
		Collection<IScriptEngine> engines = ScriptEngineRegistry.getScriptEngines();
		
		File[] scriptFiles = baseDir.listFiles();
		for (File scriptFile : scriptFiles)
		{
			if (scriptFile.getName().startsWith(scriptName + "."))
			{
				boolean handled = false;
				String extension = scriptFile.getName().substring(scriptFile.getName().lastIndexOf(".") + 1);
				
				for (IScriptEngine engine : engines)
				{
					if (!handled)
					{
						String extensions[] = engine.getSupportedExtensions();
						for (String ext : extensions)
						{
							if (extension.equalsIgnoreCase(ext)) { handled = true; break; }
						}
						
						if (handled)
						{
							try {
								FileReader reader = new FileReader(scriptFile);
								engine.startScript(commands, scriptName, reader, arguments);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
	
	public static void runScriptFromReader (IScriptCommands commands, String engineId, String scriptName, Reader reader, String[] arguments)
	{
		Collection<IScriptEngine> engines = ScriptEngineRegistry.getScriptEngines();
		for (IScriptEngine engine : engines)
		{
			if (engine.getScriptEngineId().equals(engineId))
			{
				engine.startScript(commands, scriptName, reader, arguments);
			}
		}
	}
}
