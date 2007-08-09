package com.arcaner.warlock.rcp.util;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.arcaner.warlock.client.stormfront.internal.StormFrontClient;
import com.arcaner.warlock.network.SGEConnection;
import com.arcaner.warlock.rcp.views.GameView;

/**
 * This utility class is for anything login (SGE) related
 * @author marshall
 */
public class LoginUtil {

	public static void connectAndOpenGameView (Map<String,String> loginProperties)
	{
		String server = loginProperties.get("GAMEHOST");
		int port = Integer.parseInt (loginProperties.get("GAMEPORT"));
		String key = loginProperties.get("KEY");
		StormFrontClient client = new StormFrontClient();
		GameView.createNext().setStormFrontClient(client);
		
		try {
			client.connect(server, port, key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void showAuthenticationError (int status)
	{
		String message = "Warlock was unable to log your account in due to the following error: ";
		message += getAuthenticationError(status);
		
		String title = "Login Error: ";
		
		switch (status)
		{
			case SGEConnection.ACCOUNT_REJECTED:
			{
				title += "Account Rejected";
			} break;
			case SGEConnection.INVALID_ACCOUNT:
			{
				title += "Invalid Account";
			} break;
			case SGEConnection.INVALID_PASSWORD:
			{
				title += "Wrong Password";
			} break;
		}
		
		if (title != null && message != null)
			MessageDialog.openError(Display.getDefault().getActiveShell(), title, message);
	}
	
	public static String getAuthenticationError (int status)
	{
		switch (status)
		{
			case SGEConnection.ACCOUNT_REJECTED:
				return "The account was rejected by the server.";
			case SGEConnection.INVALID_ACCOUNT:
				return "The account was not recognized by the server.";
			case SGEConnection.INVALID_PASSWORD:
				return "The password you entered was incorrect.";
		}
		
		return null;
	}
}
