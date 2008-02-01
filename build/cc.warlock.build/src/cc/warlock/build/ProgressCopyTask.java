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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Copy;

public class ProgressCopyTask extends Copy {

	protected String name = "DEFAULT";
	protected String bar;
	
	protected void doFileOperations ()
	{
		if (getProject().getProperty("hideProgress") != null)
		{
			super.doFileOperations();
			return;
		}
		
		if (name == null)
		{
			throw new BuildException("No name was specified for this progress!");
		}
		
		if (bar == null)
		{
			throw new BuildException("No progress bar was specified for this progress!");
		}
		
		ProgressDialog dialog = ProgressDialog.getProgressDialog(name);
		
		if (dialog == null)
		{
			throw new BuildException("A dialog for the name \"" + name + "\" could not be found");
		}

		panel = dialog.getPanel(bar);
		if (panel == null)
		{
			throw new BuildException("A progress bar for the name \"" + bar + "\" could not be  found");
		}
		
		panel.setMin(0);
		panel.setMax(fileCopyMap.size()+1);
		panel.setCurrent(0);
		
		loggingToProgress = true;
		super.doFileOperations();
		loggingToProgress = false;
	}
	
	protected boolean loggingToProgress = false;
	protected ProgressPanel panel;
	
	public void log(String msg, int msgLevel) {
		if (loggingToProgress)
		{
			panel.setCurrent(panel.getCurrent()+1);
			panel.setMessage(msg);
		}
		else super.log(msg, msgLevel);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBar() {
		return bar;
	}

	public void setBar(String bar) {
		this.bar = bar;
	}
}
