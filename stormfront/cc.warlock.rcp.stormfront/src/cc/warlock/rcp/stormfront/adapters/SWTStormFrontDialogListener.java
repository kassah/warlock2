package cc.warlock.rcp.stormfront.adapters;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.stormfront.IStormFrontDialogListener;

public class SWTStormFrontDialogListener implements IStormFrontDialogListener {
	private IStormFrontDialogListener listener;
	
	public SWTStormFrontDialogListener(IStormFrontDialogListener listener) {
		this.listener = listener;
	}
	
	private class ProgressBarWrapper implements Runnable {
		String text;
		int value;
		String left;
		String top;
		String width;
		String height;
		
		public ProgressBarWrapper(String text, int value, String left,
				String top, String width, String height) {
			this.text = text;
			this.value = value;
			this.left = left;
			this.top = top;
			this.width = width;
			this.height = height;
		}
		
		public void run() {
			listener.progressBar(text, value, left, top, width, height);
		}
	}
	
	public void progressBar(String text, int value, String left, String top,
			String width, String height) {
		Display.getDefault().asyncExec(new ProgressBarWrapper(text, value, left,
				top, width, height));
	}
}
