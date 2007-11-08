package cc.warlock.rcp.stormfront.ui.script;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;

public class ServerScriptEditorInput implements IStorageEditorInput, IPersistableElement {

	protected IServerScriptInfo scriptInfo;
	protected StringStorage storage;
	
	protected class StringStorage implements IStorage
	{
		public Object getAdapter(Class adapter) {
			// TODO Auto-generated method stub
			return null;
		}
		public InputStream getContents() throws CoreException {
			return new ByteArrayInputStream(scriptInfo.getContents().getBytes());
		}
		public IPath getFullPath() {
			return null;
		}
		public String getName() {
			return scriptInfo.getScriptName();
		}
		public boolean isReadOnly() {
			return false;
		}
	}
	
	public ServerScriptEditorInput (IServerScriptInfo info)
	{
		this.scriptInfo = info;
		storage = new StringStorage();
	}
	
	public boolean exists() {
		return true;
	}

	public IStorage getStorage ()
	{
		return storage;
	}
	
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return scriptInfo.getScriptName();
	}

	public IPersistableElement getPersistable() {
		return this;
	}
	
	public String getToolTipText() {
		return scriptInfo.getComment();
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public IServerScriptInfo getScriptInfo() {
		return scriptInfo;
	}

	public String getFactoryId() {
		return ServerScriptPersistableFactory.FACTORY_ID;
	}
	
	public void saveState(IMemento memento) {
		ServerScriptPersistableFactory.saveState(memento, this);
	}
	
	
}
