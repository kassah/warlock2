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
	
	protected class SetCommandWrapper implements Runnable {
		public String command;
		
		public SetCommandWrapper(String command) {
			this.command = command;
		}
		
		public void run () {
			viewer.setCurrentCommand(command);
		}
	}
	
	protected class NextCommandWrapper implements Runnable {
		public void run () {
			viewer.nextCommand();
		}
	}
	
	protected class PrevCommandWrapper implements Runnable {
		public void run () {
			viewer.prevCommand();
		}
	}
	
	protected class RepeatLastCommandWrapper implements Runnable {
		public void run () {
			viewer.repeatLastCommand();
		}
	}
	
	protected class RepeatSecondToLastCommandWrapper implements Runnable {
		public void run () {
			viewer.repeatSecondToLastCommand();
		}
	}
	
	protected class SubmitWrapper implements Runnable {
		public void run () {
			viewer.submit();
		}
	}
	
	protected class AppendWrapper implements Runnable {
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
