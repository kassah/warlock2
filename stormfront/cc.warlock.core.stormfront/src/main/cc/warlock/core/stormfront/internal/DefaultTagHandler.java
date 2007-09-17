package cc.warlock.core.stormfront.internal;

import cc.warlock.core.stormfront.IStormFrontProtocolHandler;

abstract public class DefaultTagHandler extends BaseTagHandler {
	protected IStormFrontProtocolHandler handler;
	
	public DefaultTagHandler(IStormFrontProtocolHandler handler) {
		this.handler = handler;
		handler.registerHandler(this);
	}
}
