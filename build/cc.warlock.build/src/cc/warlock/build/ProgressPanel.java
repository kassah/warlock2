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
package cc.warlock.build;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressPanel extends JPanel {

	private static final long serialVersionUID = -4717803719578991721L;
	
	protected String panelName;
	protected ProgressDialog dialog;
	protected String progressMessage;
	protected int progressMin, progressMax, progressCurrent;
	
	protected JProgressBar progress;
	protected JLabel message;
	
	public ProgressPanel (ProgressDialog dialog, String panelName, String progressMessage, int progressMin, int progressMax, int progressCurrent)
	{
		this.dialog = dialog;
		this.panelName = panelName;
		this.progressMessage = progressMessage;
		this.progressMin = progressMin;
		this.progressMax = progressMax;
		this.progressCurrent = progressCurrent;
		
		createWidgets();
	}
	
	protected void createWidgets ()
	{
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.NORTHWEST;
		gc.fill = GridBagConstraints.BOTH;
		gc.gridheight = gc.gridwidth = 1;
		gc.gridx = 0; gc.gridy = GridBagConstraints.RELATIVE;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.weightx = 1.0; gc.weighty = 0.8;
		
		dialog.add(this, gc);
		
		message = new JLabel();
		message.setText(progressMessage);
		message.setFont(new Font(message.getFont().getName(), Font.BOLD, message.getFont().getSize()-2));
		
		gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridheight = gc.gridwidth = 1;
		gc.gridx = 0; gc.gridy = 0;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.weightx = 1.0;
		
		add(message, gc);
		
		progress = new JProgressBar();
		progress.setMaximum(progressMax);
		progress.setMinimum(progressMin);
		progress.setValue(progressCurrent);
		
		gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.gridheight = gc.gridwidth = 1;
		gc.gridx = 0; gc.gridy = 1;
		gc.insets = new Insets(5, 5, 5, 5);
		gc.weightx = 1.0;
		
		add(progress, gc);
	}

	public void setCurrent(int progressCurrent) {
		this.progressCurrent = progressCurrent;
		
		progress.setValue(progressCurrent);
	}

	public void setMessage(String progressMessage) {
		this.progressMessage = progressMessage;
		
		message.setText(progressMessage);
	}

	public int getCurrent() {
		return progressCurrent;
	}

	public int getMin() {
		return progressMin;
	}

	public int getMax() {
		return progressMax;
	}

	public String getPanelName() {
		return panelName;
	}

	public void setMin(int progressMin) {
		this.progressMin = progressMin;
		
		progress.setMinimum(progressMin);
	}

	public void setMax(int progressMax) {
		this.progressMax = progressMax;
		
		progress.setMaximum(progressMax);
	}
}
