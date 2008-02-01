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
