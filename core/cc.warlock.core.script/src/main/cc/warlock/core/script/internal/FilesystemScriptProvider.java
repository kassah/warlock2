package cc.warlock.core.script.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.configuration.WarlockConfiguration;
import cc.warlock.core.script.IFilesystemScriptProvider;
import cc.warlock.core.script.IScript;
import cc.warlock.core.script.IScriptEngine;
import cc.warlock.core.script.IScriptFileInfo;
import cc.warlock.core.script.IScriptInfo;
import cc.warlock.core.script.ScriptEngineRegistry;
import cc.warlock.core.script.configuration.ScriptConfiguration;

public class FilesystemScriptProvider implements IFilesystemScriptProvider, Runnable {

	protected Hashtable<ScriptFileInfo, Long> infos = new Hashtable<ScriptFileInfo, Long>();
	
	protected boolean scanning = false, scanFinished = false;
	protected static FilesystemScriptProvider _instance;
	protected boolean forcedScan = false;
	protected Thread scanningThread;
	
	static {
		ScriptEngineRegistry.addScriptProvider(instance());
		WarlockConfiguration.getMainConfiguration().addConfigurationProvider(ScriptConfiguration.instance());
		instance().scan();
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
		public String contents;
		
		public File getScriptFile() {
			return scriptFile;
		}
		
		public String getScriptName() {
			return scriptName;
		}
		
		public int compareTo(ScriptFileInfo o) {
			return scriptName.compareToIgnoreCase(o.scriptName);
		}
		
		public String getExtension() {
			if (scriptFile.getName().indexOf(".") > 0)
			{
				return scriptFile.getName().substring(scriptFile.getName().lastIndexOf('.') + 1);
			}
			return null;
		}
		
		public Reader openReader() {
			try {
				return new FileReader(scriptFile);
			} catch (FileNotFoundException e) {
				return null;
			}
		}
	}
	
	FilesystemScriptProvider () { }
	
	protected void start () {
		scanning = true;
		
		scanningThread = new Thread(this);
		scanningThread.start();
	}
	
	public void addScriptDirectory (File directory)
	{
		Set<File> scriptDirs =
			ScriptConfiguration.instance().getScriptDirectories();
		
		if (!scriptDirs.contains(directory))
			scriptDirs.add(directory);
	}
	
	public void removeScriptDirectory (File directory)
	{
		Set<File> scriptDirs =
			ScriptConfiguration.instance().getScriptDirectories();
		
		if (scriptDirs.contains(directory))
			scriptDirs.remove(directory);
	}
	
	public List<IScriptInfo> getScriptInfos() {
		if (!scanning && ScriptConfiguration.instance().getAutoScan().get()) {
			start();
		}
		
		return Arrays.asList(infos.keySet().toArray(new IScriptInfo[infos.keySet().size()]));
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

	public IScript startScript(IScriptInfo scriptInfo, IWarlockClient client, String[] arguments) {
		for (IScriptEngine engine : ScriptEngineRegistry.getScriptEngines())
		{
			if (engine.supports(scriptInfo))
			{
				return engine.startScript(scriptInfo, client, arguments);
			}
		}
		return null;
	}
	
	protected void scanDirectory (File directory)
	{
		if (!directory.exists()) return;
		
		for (File file : directory.listFiles())
		{
			if (file.isFile())
			{
				ScriptFileInfo info = getScriptInfo(file);
				if (info == null) {
					info = new ScriptFileInfo();
					info.scriptFile = file;
					info.scriptName = file.getName();
					if (info.scriptName.indexOf('.') > 0)
					{
						info.scriptName = info.scriptName.substring(0, info.scriptName.indexOf('.'));
					}
					
					infos.put(info, (long) 0);
				}
			}
		}
		
		for (Iterator<ScriptFileInfo> iter = infos.keySet().iterator(); iter.hasNext(); )
		{
			ScriptFileInfo info = iter.next();
			
			if (!info.scriptFile.exists())
			{
				iter.remove();
			}
		}
	}
	
	protected void scan ()
	{
		for (File dir : ScriptConfiguration.instance().getScriptDirectories())
		{
			if (dir.exists())
			{
				scanDirectory(dir);
			}
		}
		scanFinished = true;
	}
	
	public void run ()
	{
		while (scanning)
		{
			scan();
			
			try {
				Thread.sleep(ScriptConfiguration.instance().getScanTimeout().get());
			} catch (InterruptedException e) {
				if (!forcedScan)
				{
					e.printStackTrace();
				}
				forcedScan = false;
			}
		}
	}
	
	public List<File> getScriptDirectories() {
		Set<File> scriptDirs = ScriptConfiguration.instance().getScriptDirectories();
		
		return Arrays.asList(scriptDirs.toArray(new File[scriptDirs.size()]));
	}
	
	public void forceScan() {
		forcedScan = true;
		scanFinished = false;
		
		scanningThread.interrupt();
		while (!scanFinished) {
			try {
				Thread.sleep((long)200);
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
