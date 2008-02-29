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
package cc.warlock.jira.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.exception.VelocityException;

import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.config.properties.APKeys;
import com.atlassian.jira.event.issue.AbstractIssueEventListener;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.issue.IssueEventListener;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.velocity.VelocityManager;

/**
 * @author marshall
 * 
 * This is default
 *
 */
public class EventListener extends AbstractIssueEventListener implements
		IssueEventListener {

	private final static Logger log = Logger.getLogger(EventListener.class);
	
	public static HashMap eventTemplates = new HashMap();
	static {
		eventTemplates.put(EventType.ISSUE_CREATED_ID, "issueCreated.vm");
		eventTemplates.put(EventType.ISSUE_UPDATED_ID, "issueUpdated.vm");
		eventTemplates.put(EventType.ISSUE_ASSIGNED_ID, "issueAssigned.vm");
		eventTemplates.put(EventType.ISSUE_RESOLVED_ID, "issueResolved.vm");
		eventTemplates.put(EventType.ISSUE_CLOSED_ID, "issueClosed.vm");
		eventTemplates.put(EventType.ISSUE_COMMENTED_ID, "issueCommented.vm");
		eventTemplates.put(EventType.ISSUE_REOPENED_ID, "issueReopened.vm");
		eventTemplates.put(EventType.ISSUE_DELETED_ID, "issueDeleted.vm");
		eventTemplates.put(EventType.ISSUE_MOVED_ID, "issueMoved.vm");
		eventTemplates.put(EventType.ISSUE_WORKLOGGED_ID, "issueWorkLogged.vm");
		eventTemplates.put(EventType.ISSUE_WORKSTARTED_ID, "issueWorkStarted.vm");
		eventTemplates.put(EventType.ISSUE_WORKSTOPPED_ID, "issueWorkStopped.vm");
		eventTemplates.put(EventType.ISSUE_GENERICEVENT_ID, "genericIssueEvent.vm");
		eventTemplates.put(EventType.ISSUE_COMMENT_EDITED_ID, "issueCommentEdited.vm");
		eventTemplates.put(EventType.ISSUE_WORKLOG_UPDATED_ID, "issueWorklogUpdated.vm");
		eventTemplates.put(EventType.ISSUE_WORKLOG_DELETED_ID, "issueWorklogDeleted.vm");
	}
	
	public void workflowEvent(IssueEvent event) {
		String message = getTemplatedMessage(event);
		
		if (message.length() > 0)
		{
			File messageFile = new File("/tmp/warlockbot.message");
			try {
				PrintWriter writer = new PrintWriter(new FileOutputStream(messageFile));
				
				writer.print(message);
				writer.close();
			} catch (FileNotFoundException e) {
				log.error("File not found: " + messageFile.getAbsolutePath());
			}
		}
	}
	
	protected String chop (String str)
	{
		int limit = 250;
		
		if (str.length() > limit) {
			int leftover = str.length() - limit;
			return str.substring(0, limit) + " ... (cut " + leftover + " chars)";
		}
		return str;
	}
	
	protected String getTemplatedMessage(IssueEvent event) {
		Issue issue = event.getIssue();
		String result = "";
		VelocityManager vm = ComponentManager.getInstance().getVelocityManager();
		
		Map params = new HashMap();
		params.put("issue", issue);
		params.put("event", event);
		params.put("comment", event.getComment());
		if (event.getComment() != null)
			params.put("comment_text", chop(event.getComment().getBody()));
		
		String base_url = ComponentManager.getInstance().getApplicationProperties().getString(APKeys.JIRA_BASEURL);
		params.put("base_url", base_url);
		try {
			result = vm.getBody("", (String)eventTemplates.get(event.getEventTypeId()), params);
		} catch (VelocityException e) {
			log.warn("Error rendering WarlockBot notification", e);
		}
		return result;
	}

}
