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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ProgressDialog extends JFrame {

	private static final long serialVersionUID = -264667745724329209L;
	protected static HashMap<String, ProgressDialog> dialogs = new HashMap<String, ProgressDialog>();
	
	public static ProgressDialog getProgressDialog (String name)
	{
		return dialogs.get(name);
	}
	
	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	protected String name;
	protected String iconFilename;
	protected String dialogTitle;
	protected HashMap<String, ProgressPanel> panels = new HashMap<String,ProgressPanel>();
	
	public ProgressDialog (String name, String iconFilename, String dialogTitle, String panelNames[], String panelMessages[], int progressMins[], int progressMaxes[], int progressCurrents[])
	{
		this.name = name;
		this.iconFilename = iconFilename;
		this.dialogTitle = dialogTitle;
		
		dialogs.put(name, this);
		createWidgets(panelNames, panelMessages, progressMins, progressMaxes, progressCurrents);
	}
	
	protected void createWidgets (String panelNames[], String panelMessages[], int progressMins[], int progressMaxes[], int progressCurrents[])
	{
		setLayout(new GridBagLayout());
		
		JLabel label = new JLabel();
		if (iconFilename != null && new File(iconFilename).exists())
			label.setIcon(new ImageIcon(iconFilename));
		
		label.setText(dialogTitle);
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize() + 4));
		label.setBorder(BorderFactory.createLineBorder(Color.black));
		label.setOpaque(true);
		label.setBackground(Color.white);
		setBackground(Color.white);
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.WEST;
		gc.fill = GridBagConstraints.BOTH;
		gc.gridheight = gc.gridwidth = 1;
		gc.gridx = 0; gc.gridy = 0;
		gc.ipadx = gc.ipady = 15;
		gc.weightx = 1.0; gc.weighty = 0.2;
		
		add(label, gc);
		
		for (int i = 0; i < panelNames.length; i++)
		{
			ProgressPanel panel = new ProgressPanel(this, panelNames[i], panelMessages[i], progressMins[i], progressMaxes[i], progressCurrents[i]);
			panels.put(panelNames[i], panel);
		}

		JButton hideButton = new JButton();
		hideButton.setText("Hide Progress");
		
		gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.EAST;
		gc.fill = GridBagConstraints.NONE;
		gc.gridheight = gc.gridwidth = 1;
		gc.gridx = 0; gc.gridy = GridBagConstraints.RELATIVE;
		gc.insets = new Insets(5, 5, 5, 5);
		
		add(hideButton, gc);
		setSize(400, 225);
		setLocationByPlatform(true);
		setVisible(true);
		setTitle(dialogTitle);
		
		hideButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		addWindowListener(new WindowAdapter () {
			@Override
			public void windowClosing(WindowEvent e) {
				dialogs.remove(name);
			}
		});
	}
	
	public ProgressPanel getPanel (String name)
	{
		if (panels.containsKey(name))
			return panels.get(name);
		return null;
	}
	
	public Collection<ProgressPanel> getPanels ()
	{
		return panels.values();
	}
}
