package cc.warlock.rcp.ui.client;

public class CatchingRunnable implements Runnable {

	private Runnable runner;
	
	public CatchingRunnable(Runnable runner) {
		this.runner = runner;
	}
	
	public void run() {
		try {
			runner.run();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
