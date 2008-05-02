package cc.warlock.rcp.stormfront.ui.menu;

import java.util.ArrayList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;

import cc.warlock.core.script.IScript;
import cc.warlock.rcp.menu.SubMenuContributionItem;

public class ScriptContributionItem extends SubMenuContributionItem {

	IScript m_Script;

	public class StopScriptAction extends Action {
		
		IScript m_Script = null;
		
		public StopScriptAction(IScript script) {
			super("Stop");
			m_Script = script;
		}

		@Override
		public void run() {
			m_Script.stop();
		}
	}

	public class PauseScriptAction extends Action {
		
		IScript m_Script = null;
		
		public PauseScriptAction(IScript script) {
			super("Pause");
			m_Script = script;
		}

		@Override
		public void run() {
			m_Script.suspend();
		}
	}

	public class ResumeScriptAction extends Action {
		
		IScript m_Script = null;
		
		public ResumeScriptAction(IScript script) {
			super("Resume");
			m_Script = script;
		}

		@Override
		public void run() {
			m_Script.resume();
		}
	}

	
	public ScriptContributionItem(IScript script) {
		super(script.getName());
		m_Script = script;
	}
	
	@Override
	protected IContributionItem[] getContributionItems() {
		ArrayList<IContributionItem> items = new ArrayList<IContributionItem>();

		IContributionItem newItem = new ActionContributionItem(new StopScriptAction(m_Script));
		items.add(newItem);

		if(!m_Script.isSuspended())
		{
			newItem = new ActionContributionItem(new PauseScriptAction(m_Script));
			items.add(newItem);
		}
		else
		{
			newItem = new ActionContributionItem(new ResumeScriptAction(m_Script));
			items.add(newItem);
		}
		return items.toArray(new IContributionItem[items.size()]); 
	}

}
