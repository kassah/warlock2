package cc.warlock.build;

import java.io.File;
import java.io.IOException;

import com.izforge.izpack.PackFile;
import com.izforge.izpack.event.SimpleInstallerListener;
import com.izforge.izpack.util.FileExecutor;
import com.izforge.izpack.util.OsVersion;

public class ChmodInstallerListener extends SimpleInstallerListener {

	@Override
	public boolean isFileListener() {
		return true;
	}
	
	@Override
	public void afterFile(File file, PackFile pf) throws Exception {
		if (file.getName().equals("warlock2"))
		{
			try {
				chmod(file, 755);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	  private void chmod(File path, int permissions) throws IOException
	  {
	    String pathSep = System.getProperty("path.separator");
	    if(OsVersion.IS_WINDOWS)
	    {
	      throw new IOException("Sorry, chmod not supported yet on windows; use this class OS dependant.");
	    }
	    if( path == null )
	    // Oops this is an error, but in this example we ignore it ...
	      return;
	    String permStr = Integer.toOctalString(permissions);
	    String[] params = {"chmod", permStr, path.getAbsolutePath()};
	    String[] output = new String[2];
	    FileExecutor fe = new FileExecutor();
	    fe.executeCommand(params, output);
	  }
}
