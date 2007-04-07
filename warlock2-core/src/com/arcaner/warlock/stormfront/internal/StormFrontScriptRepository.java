package com.arcaner.warlock.stormfront.internal;

import java.util.Hashtable;

import com.arcaner.warlock.stormfront.IStormFrontScript;

public class StormFrontScriptRepository {

	private static Hashtable<String, StormFrontScript> scripts = new Hashtable<String, StormFrontScript>();


	public static void addScript (String name, String comment, String contents)
	{
		StormFrontScript script = new StormFrontScript();
		script.setName(name);
		script.setComment(comment);
		script.setContents(formatContents(contents));
		
		scripts.put(name, script);
	}
	
	private static String formatContents (String scriptContents)
	{
		return scriptContents.replace('\\', '\n');
	}
	
	public static void setScriptContents (String name, String newContents)
	{
		scripts.get(name).setContents(newContents);
	}
	
	public static void setScriptComment (String name, String comment)
	{
		scripts.get(name).setComment(comment);
	}
	
	public static IStormFrontScript getScript (String name)
	{
		return scripts.get(name);
	}
}
