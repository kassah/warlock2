package com.arcaner.warlock.script.wsl;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import com.arcaner.warlock.script.IScript;
import com.arcaner.warlock.script.IScriptCommands;
import com.arcaner.warlock.script.IScriptEngine;

public class WarlockWSLEngine implements IScriptEngine {

	public static final String ENGINE_ID = "com.arcaner.warlock.script.wsl.WarlockWSLEngine";
	
	public String getScriptEngineId() {
		return ENGINE_ID;
	}
	
	public String getScriptEngineName() {
		return "Standard Wizard Scripting Language Engine (c) 2002-2007 Warlock Team";
	}
	
	public String[] getSupportedExtensions() {
		return new String[] { "wiz", "cmd", "wsl" };
	}
	
	public IScript startScript (IScriptCommands commands, String scriptName, Reader reader, final String[] arguments) {
		try {
			WarlockWSLScript script = new WarlockWSLScript(commands, scriptName, reader);
			
			ArrayList<String> args = new ArrayList<String>();
			args.addAll(Arrays.asList(arguments));
			
			script.start(args);
			
			return script;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
