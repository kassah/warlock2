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
	}
	
	public SWTWarlockClientViewer (IWarlockClientViewer viewer, boolean asynch)
	{
		super(viewer, asynch);
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
}
