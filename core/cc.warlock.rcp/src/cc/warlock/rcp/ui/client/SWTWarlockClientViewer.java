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
/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.ui.client;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockClientViewer;

/**
 * @author Marshall
 *
 * A convenience super class for viewers who need SWT thread access
 */
public class SWTWarlockClientViewer extends SWTStreamListener implements IWarlockClientViewer  {

	private IWarlockClientViewer viewer;
	
	public SWTWarlockClientViewer (IWarlockClientViewer viewer)
	{
		super(viewer);
		this.viewer = viewer;
	}
	
	private class SetCommandWrapper implements Runnable {
		public String command;
		
		public SetCommandWrapper(String command) {
			this.command = command;
		}
		
		public void run () {
			viewer.setCurrentCommand(command);
		}
	}
	
	private class NextCommandWrapper implements Runnable {
		public void run () {
			viewer.nextCommand();
		}
	}
	
	private class PrevCommandWrapper implements Runnable {
		public void run () {
			viewer.prevCommand();
		}
	}
	
	private class SearchHistoryWrapper implements Runnable {
		public void run () {
			viewer.searchHistory();
		}
	}
	
	private class RepeatLastCommandWrapper implements Runnable {
		public void run () {
			viewer.repeatLastCommand();
		}
	}
	
	private class RepeatSecondToLastCommandWrapper implements Runnable {
		public void run () {
			viewer.repeatSecondToLastCommand();
		}
	}
	
	private class SubmitWrapper implements Runnable {
		public void run () {
			viewer.submit();
		}
	}
	
	private class AppendWrapper implements Runnable {
		public char c;
		
		public AppendWrapper(char ch) {
			this.c = ch;
		}
		
		public void run () {
			viewer.append(c);
		}
	}
	
	private class CopyWrapper implements Runnable {
		public void run () {
			viewer.copy();
		}
	}
	
	private class PasteWrapper implements Runnable {
		public void run () {
			viewer.paste();
		}
	}
	
	private class PageUpWrapper implements Runnable {
		public void run () {
			viewer.pageUp();
		}
	}
	
	private class PageDownWrapper implements Runnable {
		public void run () {
			viewer.pageDown();
		}
	}
	
	public String getCurrentCommand() {
		return viewer.getCurrentCommand();
	}
	
	public IWarlockClient getWarlockClient() {
		return viewer.getWarlockClient();
	}
	
	public void setCurrentCommand(String command) {
		run(new SetCommandWrapper(command));
	}
	
	public void append(char ch) {
		run(new AppendWrapper(ch));
	}
	
	public void nextCommand() {
		run(new NextCommandWrapper());
	}
	
	public void prevCommand() {
		run(new PrevCommandWrapper());
	}
	
	public void searchHistory() {
		run(new SearchHistoryWrapper());
	}
	
	public void repeatLastCommand() {
		run(new RepeatLastCommandWrapper());
	}
	
	public void repeatSecondToLastCommand() {
		run(new RepeatSecondToLastCommandWrapper());
	}
	
	public void submit() {
		run(new SubmitWrapper());
	}
	
	public void copy() {
		run(new CopyWrapper());
	}
	
	public void paste() {
		run(new PasteWrapper());
	}
	
	public void pageUp() {
		run(new PageUpWrapper());
	}
	
	public void pageDown() {
		run(new PageDownWrapper());
	}
}
