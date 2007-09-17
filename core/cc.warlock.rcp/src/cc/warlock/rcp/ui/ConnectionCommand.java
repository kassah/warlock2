package cc.warlock.rcp.ui;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.commands.ICommandImageService;

public class ConnectionCommand implements IConnectionCommand {

	protected Command command;
	protected Image image;
	
	public ConnectionCommand (String commandId)
	{
		ICommandService service =
			(ICommandService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ICommandService.class);
		ICommandImageService imageService =
			(ICommandImageService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ICommandImageService.class);
		
		command = service.getCommand(commandId);
		
		ImageDescriptor descriptor = imageService.getImageDescriptor(commandId);
		if (descriptor != null) {
			image = descriptor.createImage();
		}
	}
	
	public String getDescription() {
		try {
			return command.getDescription();
		} catch (NotDefinedException e) {
		}
		return "";
	}

	public Image getImage() {
		return image;
	}

	public String getLabel() {
		try {
			return command.getName();
		} catch (NotDefinedException e) {
		}
		return "";
	}

	public void run() {
		try {
			//TODO instantiating an ExecutionEvent is "bad" ?
			command.getHandler().execute(null);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (image != null)
			image.dispose();
		super.finalize();
	}

}
