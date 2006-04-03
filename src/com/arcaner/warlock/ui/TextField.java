package com.arcaner.warlock.ui;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Marshall
 */
public class TextField {
        private String string;
        private Text text;
        
		public TextField (Composite parent, int style) {
			text = new Text(parent, style);
			
			text.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					TextField.this.string = text.getText();
				}            
			});
		}

		public String getText() {
			return string;
		}
		
		public Text getTextControl ()
		{
			return text;
		}
}
