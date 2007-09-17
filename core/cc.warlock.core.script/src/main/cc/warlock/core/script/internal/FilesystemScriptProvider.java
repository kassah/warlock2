package cc.warlock.core.script.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import cc.warlock.core.configuration.WarlockConfiguration;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.IScriptProvider;
import cc.warlock.core.script.ScriptEngineRegistry;

public class FilesystemScriptProvider implements IScriptProvider, Runnable {

	protected Hashtable<String, IScript> scripts = new Hashtable<String, IScript>();
	protected Hashtable<ScriptFileInfo, Long> infos = new Hashtable<ScriptFileInfo, Long>();
	protected ArrayList<File> scriptDirs = new ArrayList<File>();
	protected long scanTimeout = 500;
	protected boolean scanning = true;
	protected static FilesystemScriptProvider _instance;
	
	static {
		ScriptEngineRegistry.addScriptProvider(instance());
		instance().addScriptDirectory(WarlockConfiguration.getUserDirectory("warlock-scripts", true));
	}
	
	public static FilesystemScriptProvider instance()
	{
		if (_instance == null) {
			_instance = new FilesystemScriptProvider();
		}
		return _instance;
	}
	
	protected class ScriptFileInfo implements IScriptFileInfo, Comparable<ScriptFileInfo>
	{
		public String scriptName;
		public File scriptFile;
		
		public File getScriptFile() {
			return scriptFile;
		}
		
		public String getScriptName() {
			return scriptName;
		}
		
		public int compareTo(ScriptFileInfo o) {
			return scriptName.compareTo(o.scriptName);
		}
		
		public String getExtension() {
			if (scriptFile.getName().indexOf(".") > 0)
			{
				return scriptFile.getName().substring(scriptFile.getName().lastIndexOf('.') + 1);
			}
			return null;
		}
	}
	
	FilesystemScriptProvider ()
	{
		new Thread(this).start();
	}
	
	public void addScriptDirectory (File directory)
	{
		if (!scriptDirs.contains(directory))
			scriptDirs.add(directory);
	}
	
	public void removeScriptDirectory (File directory)
	{
		if (scriptDirs.contains(directory))
			scriptDirs.remove(directory);
	}
	
	public List<File> getScriptDirectories()
	{
		return scriptDirs;
	}
	
	public List<IScript> getScripts() {
		return Arrays.asList(scripts.values().toArray(new IScript[scripts.values().size()]));
	}
	
	protected ScriptFileInfo getScriptInfo (File file)
	{
		for (ScriptFileInfo info : infos.keySet())
		{
			if (info.scriptFile.getAbsolutePath().equals(file.getAbsolutePath()))
			{
				return info;
			}
		}
		return null;
	}

	protected IScript createScript (ScriptFileInfo info)
	{
		IScript script = null;
		for (IScriptEngine engine : ScriptEngineRegistry.getScriptEngines())
		{
			if (engine.supports(info))
			{
				try {
					FileReader reader = new FileReader(info.scriptFile);
					script = engine.createScript(info.scriptName, reader);
					reader.close();
					break;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return script;
	}
	
	protected void scanDirectory (File directory)
	{
		for (File file : directory.listFiles())
		{
			if (file.isFile())
			{
				ScriptFileInfo info = getScriptInfo(file);
				if (info == null) {
					info = new ScriptFileInfo();
					info.scriptFile = file;
					info.scriptName = file.getName();
					infos.put(info, (long) 0);
				}
				
				if (infos.get(info) < file.lastModified())
				{
					IScript script = createScript(info);
					if (script != null) {
						System.out.println("adding script: " + script.getName());
						scripts.put(script.getName(), script);
						infos.put(info, file.lastModified());
					}
				}
			}
		}
		
		for (Iterator<ScriptFileInfo> iter = infos.keySet().iterator(); iter.hasNext(); )
		{
			ScriptFileInfo info = iter.next();
			
			if (!info.scriptFile.exists())
			{
				if (!scripts.containsKey(info.scriptName))
					scripts.remove(info.scriptName);
				iter.remove();
			}
		}
	}
	
	public void run ()
	{
		while (scanning)
		{
			for (File dir : scriptDirs)
			{
				if (dir.exists())
				{
					scanDirectory(dir);
				}
			}
			
			try {
				Thread.sleep(scanTimeout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		scanning = false;
		
		super.finalize();
	}
}
