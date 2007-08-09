/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cc.warlock.rcp.ui.client;

import cc.warlock.client.IWarlockClient;
import cc.warlock.client.IWarlockClientViewer;

/**
 * @author Marshall
 *
 * A convenience super class for viewers who need SWT thread access
 */
public class SWTWarlockClientViewer extends SWTStreamListener implements IWarlockClientViewer  {

	private IWarlockClientViewer viewer;
	private ListenerWrapper wrapper;
	
	private static enum EventType
	{
		SetCurrentCommand
	};
	
	public SWTWarlockClientViewer (IWarlockClientViewer viewer)
	{
		super(viewer);
	}
	
	public SWTWarlockClientViewer (IWarlockClientViewer viewer, boolean asynch)
	{
		super(viewer, asynch);
		this.viewer = viewer;
		this.wrapper = new ListenerWrapper();
	}
	
	protected class ListenerWrapper implements Runnable {
		public String text;
		public IWarlockClient client;
		public EventType eventType;
		
		public void run () {
			switch (eventType)
			{
				case SetCurrentCommand: viewer.setCurrentCommand(text); break;
			}
			text = null;
			client = null;
		}
	}
	
	public String getCurrentCommand() {
		return viewer.getCurrentCommand();
	}
	
	public IWarlockClient getWarlockClient() {
		return viewer.getWarlockClient();
	}
	
	public void setCurrentCommand(String command) {
		wrapper.text = command;
		wrapper.eventType = EventType.SetCurrentCommand;
		run(wrapper);
	}
}
