package cc.warlock.rcp.stormfront.adapters;

import org.eclipse.swt.widgets.Display;

import cc.warlock.core.stormfront.IStormFrontDialogListener;

public class SWTStormFrontDialogListener implements IStormFrontDialogListener {
	private IStormFrontDialogListener listener;
	
	public SWTStormFrontDialogListener(IStormFrontDialogListener listener) {
		this.listener = listener;
	}
	
	private class ProgressBarWrapper implements Runnable {
		String id;
		String text;
		int value;
		String left;
		String top;
		String width;
		String height;
		
		public ProgressBarWrapper(String id, String text, int value,
				String left, String top, String width, String height) {
			this.id = id;
			this.text = text;
			this.value = value;
			this.left = left;
			this.top = top;
			this.width = width;
			this.height = height;
		}
		
		public void run() {
			listener.progressBar(id, text, value, left, top, width, height);
		}
	}
	
	public void progressBar(String id, String text, int value, String left, String top,
			String width, String height) {
		Display.getDefault().asyncExec(new ProgressBarWrapper(id, text, value, left,
				top, width, height));
	}
}
