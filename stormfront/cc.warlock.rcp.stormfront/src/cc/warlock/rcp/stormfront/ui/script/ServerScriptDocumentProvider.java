/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.rcp.stormfront.ui.script;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.ForwardingDocumentProvider;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;

import cc.warlock.core.stormfront.serversettings.server.IServerScriptInfo;

public class ServerScriptDocumentProvider extends ForwardingDocumentProvider {

	protected TextFileDocumentProvider docProvider;
	
	protected static class SetupParticipant implements IDocumentSetupParticipant
	{
		public void setup(IDocument document) {
			// TODO Auto-generated method stub
			
		}
	}
	
	public ServerScriptDocumentProvider ()
	{
		super(null, new SetupParticipant());

		docProvider = new TextFileDocumentProvider();
		setParentProvider(docProvider);
	}
	
	@Override
	public boolean canSaveDocument(Object element) {
		if (element instanceof ServerScriptEditorInput)
		{
			ServerScriptEditorInput input = (ServerScriptEditorInput)element;
			IServerScriptInfo info = input.getScriptInfo();
			
			IDocument document = docProvider.getDocument(element);
			
			String docText = document.get();
			String contents = info.getContents();
			
			return !docText.equals(contents);
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
