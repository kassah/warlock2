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
package cc.warlock.rcp.ui;

import java.util.ArrayList;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.SWTKeySupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * @author Marshall Culpepper
 *
 */
public class KeyStrokeText implements KeyListener {
	
	protected Text keyText;
	protected KeyStroke keyStroke;
	protected ArrayList<KeyStrokeLockListener> listeners = new ArrayList<KeyStrokeLockListener>();
	
	public static interface KeyStrokeLockListener {
		public void keyStrokeLocked();
		public void keyStrokeUnlocked();
	}
	
	public KeyStrokeText (Composite parent, int style) {
		keyText = new Text(parent, style);
		keyText.setBackground(new Color(Display.getDefault(), 255, 255, 164));
		keyText.addKeyListener(this);
	}
	
	protected void fireKeyStrokeLockEvent ()
	{
		for (KeyStrokeLockListener listener : listeners) {
			if (keyStrokeLocked) {
				listener.keyStrokeLocked();
			} else {
				listener.keyStrokeUnlocked();
			}
		}
	}
	
	protected boolean keyStrokeLocked = false;
	public void keyPressed(KeyEvent e) {
		KeyStroke keyStroke = KeyStroke.getInstance(e.stateMask, e.keyCode);
		
		String description = SWTKeySupport.getKeyFormatterForPlatform().format(keyStroke);
		keyText.setText(description);
		if (e.keyCode != SWT.CTRL && e.keyCode != SWT.ALT
			&& e.keyCode != SWT.SHIFT && e.keyCode != SWT.COMMAND) {
			
			keyStrokeLocked = true;
			this.keyStroke = keyStroke;
			
			fireKeyStrokeLockEvent();
			
		} else {
			keyStrokeLocked = false;
			
			fireKeyStrokeLockEvent();
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if (!keyStrokeLocked) {
			KeyStroke keyStroke = KeyStroke.getInstance(e.stateMask, e.keyCode);
			if (e.keyCode == e.stateMask) {
				keyStroke = this.keyStroke;
			} else if ((e.stateMask & e.keyCode) > 0) {
				int stateMask = e.stateMask;
				stateMask ^= e.keyCode;
				
				keyStroke = KeyStroke.getInstance(stateMask, KeyStroke.NO_KEY);
			}
			
			String description = SWTKeySupport.getKeyFormatterForPlatform().format(keyStroke);
			keyText.setText(description);
		}
	}

	public KeyStroke getKeyStroke() {
		return keyStroke;
	}

	public void setKeyStroke(KeyStroke keyStroke) {
		this.keyStroke = keyStroke;
	}

	public Text getText() {
		return keyText;
	}
	
	public void addKeyStrokeLockListener (KeyStrokeLockListener listener) {
		listeners.add(listener);
	}
}
