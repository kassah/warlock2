/*
 * Created on Mar 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.arcaner.warlock.client.internal;

import org.eclipse.swt.widgets.Display;

import com.arcaner.warlock.client.IWarlockClientViewer;

/**
 * @author Marshall
 *
 * A convenience super class for viewers who need SWT thread access
 */
public abstract class SWTWarlockClientViewer implements IWarlockClientViewer {

	private AppendRunner appender;
	private EchoRunner echoer;
	private SetViewerTitleRunner setViewerTitleRunner;
	
	public SWTWarlockClientViewer ()
	{
		appender = new AppendRunner();
		echoer = new EchoRunner();
		setViewerTitleRunner = new SetViewerTitleRunner();
	}
	
	protected class AppendRunner implements Runnable {
		public String text;
		
		public void run () {
			handleAppend(text);
		}
	}
	
	public void append(String text) {
		appender.text = text;
		Display.getDefault().syncExec(appender);
	}
	
	public abstract void handleAppend(String text);

	protected class EchoRunner implements Runnable {
		public String text;
		public void run () {
			handleEcho(text);
		}
	}
	
	public void echo(String text) {
		echoer.text = text;
		Display.getDefault().syncExec(echoer);
	}
	
	public abstract void handleEcho (String text);

	
	protected class SetViewerTitleRunner implements Runnable {
		public String text;
		public void run () {
			handleSetViewerTitle(text);
		}
	}
	
	public void setViewerTitle(String title) {
		setViewerTitleRunner.text = title;
		Display.getDefault().syncExec(setViewerTitleRunner);
	}

	public abstract void handleSetViewerTitle (String title);
}
