package cc.warlock.stormfront.internal;

import cc.warlock.stormfront.IStormFrontProtocolHandler;

abstract public class DefaultTagHandler extends BaseTagHandler {
	protected IStormFrontProtocolHandler handler;
	
	public DefaultTagHandler(IStormFrontProtocolHandler handler) {
		this.handler = handler;
		handler.registerHandler(this);
	}
}
