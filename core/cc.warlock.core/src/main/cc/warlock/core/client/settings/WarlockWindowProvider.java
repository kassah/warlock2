package cc.warlock.core.client.settings;


public class WarlockWindowProvider extends WarlockStyleProvider {
	private static final WarlockStyleProvider instance = new WarlockWindowProvider();
	
	protected WarlockWindowProvider() { }
	
	public static WarlockStyleProvider getInstance() {
		return instance;
	}
	
	@Override
	protected String getNodeName() {
		return "window";
	}
}
