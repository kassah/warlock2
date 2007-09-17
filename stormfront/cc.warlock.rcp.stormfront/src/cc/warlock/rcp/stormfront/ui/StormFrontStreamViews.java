package cc.warlock.rcp.stormfront.ui;

import cc.warlock.core.stormfront.client.IStormFrontClient;
import cc.warlock.rcp.views.StreamView;

public class StormFrontStreamViews {
	public static final String DEATH_VIEW_ID =  StreamView.STREAM_VIEW_PREFIX + IStormFrontClient.DEATH_STREAM_NAME;
	public static final String INVENTORY_VIEW_ID = StreamView.STREAM_VIEW_PREFIX  + IStormFrontClient.INVENTORY_STREAM_NAME;
	public static final String THOUGHTS_VIEW_ID = StreamView.STREAM_VIEW_PREFIX + IStormFrontClient.THOUGHTS_STREAM_NAME ;
}
