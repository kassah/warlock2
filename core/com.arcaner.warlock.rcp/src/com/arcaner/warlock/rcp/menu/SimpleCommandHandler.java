package com.arcaner.warlock.rcp.menu;

import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;

public abstract class SimpleCommandHandler implements IHandler {

	public void addHandlerListener(IHandlerListener handlerListener) {	}

	public void dispose() {	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isHandled() {
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {}

}
