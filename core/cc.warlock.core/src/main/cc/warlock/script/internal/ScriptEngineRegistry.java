package cc.warlock.script.internal;

import java.util.ArrayList;
import java.util.Collection;

import cc.warlock.script.IScriptEngine;

public class ScriptEngineRegistry {

	private static ArrayList<IScriptEngine> engines = new ArrayList<IScriptEngine>();
	
	public static void addScriptEngine (IScriptEngine engine)
	{
		engines.add(engine);
	}
	
	public static void removeScriptEngine (IScriptEngine engine)
	{
		engines.remove(engine);
	}
	
	public static Collection<IScriptEngine> getScriptEngines ()
	{
		return engines;
	}
}
