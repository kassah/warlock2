package cc.warlock.rcp.stormfront.ui.script;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.ForwardingDocumentProvider;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;

public class ServerScriptDocumentProvider extends ForwardingDocumentProvider {

	protected static class SetupParticipant implements IDocumentSetupParticipant
	{
		public void setup(IDocument document) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public ServerScriptDocumentProvider ()
	{
		super(null, new SetupParticipant(), new TextFileDocumentProvider());
	}
	
	
	@Override
	public boolean canSaveDocument(Object element) {
		if (element instanceof ServerScriptEditorInput)
		{
			return true;
		}
		return super.canSaveDocument(element);
	}
	
	@Override
	public void saveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
		throws CoreException
	{
		if (element instanceof ServerScriptEditorInput)
		{
			ServerScriptEditorInput input = (ServerScriptEditorInput)element;
			IServerScriptInfo info = input.getScriptInfo();
			
			String newScriptText = document.get();
			info.getServerScript().setScriptContents(newScriptText);
			
			info.getClient().getServerSettings().saveScript(info.getServerScript());
		}
		
		super.saveDocument(monitor, element, document, overwrite);
	}

}
