package cc.warlock.rcp.stormfront.ui.util;

import java.io.IOException;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cc.warlock.core.client.IWarlockClient;
import cc.warlock.core.client.IWarlockStyle;
import cc.warlock.core.client.internal.WarlockStyle;
import cc.warlock.core.stormfront.client.internal.StormFrontClient;
import cc.warlock.core.stormfront.network.SGEConnection;
import cc.warlock.rcp.application.WarlockApplication;
import cc.warlock.rcp.plugin.Warlock2Plugin;
import cc.warlock.rcp.stormfront.ui.StormFrontPerspectiveFactory;
import cc.warlock.rcp.stormfront.ui.views.BarsView;
import cc.warlock.rcp.stormfront.ui.views.HandsView;
import cc.warlock.rcp.stormfront.ui.views.StormFrontGameView;
import cc.warlock.rcp.ui.network.SWTConnectionListenerAdapter;
import cc.warlock.rcp.views.DebugView;
import cc.warlock.rcp.views.GameView;

public class LoginUtil {

	public static void connect (GameView gameView, Map<String,String> loginProperties)
	{
		String server = loginProperties.get("GAMEHOST");
		int port = Integer.parseInt (loginProperties.get("GAMEPORT"));
		String key = loginProperties.get("KEY");

		IWarlockClient client = Warlock2Plugin.getDefault().addNextClient(new StormFrontClient());
		gameView.setClient(client);
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		
		try {
			if (page.findView(HandsView.VIEW_ID) == null)
			{
				page.showView(HandsView.VIEW_ID);
			}
			if (page.findView(BarsView.VIEW_ID) == null)
			{
				page.showView(BarsView.VIEW_ID);
			}
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			client.connect(server, port, key);
			
			if (WarlockApplication.instance().inDebugMode())
			{
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(DebugView.VIEW_ID);
				
				client.getConnection().addConnectionListener(new SWTConnectionListenerAdapter(DebugView.instance()));
			}
			
		} catch (IOException e) {
			String errorConnectMessage =
			"******************************************************************\n" +
			"* The connection was refused, possibly meaning the server is currently down,\n" +
			"* or your internet connection is not active\n" +
			"******************************************************************\n";
			
			IWarlockStyle style = WarlockStyle.createCustomStyle("mono", 0, errorConnectMessage.length());
			style.addStyleType(IWarlockStyle.StyleType.MONOSPACE);
			
			client.getDefaultStream().send(errorConnectMessage, style);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	public static void connectAndOpenGameView (Map<String,String> loginProperties)
	{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (!page.getPerspective().getId().equals(StormFrontPerspectiveFactory.PERSPECTIVE_ID))
		{
			IPerspectiveDescriptor perspective =
				PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(StormFrontPerspectiveFactory.PERSPECTIVE_ID);
			
			page.setPerspective(perspective);
		}
		
		connect (GameView.createNext(StormFrontGameView.VIEW_ID), loginProperties);
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
