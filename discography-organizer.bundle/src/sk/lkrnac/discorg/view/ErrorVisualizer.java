package sk.lkrnac.discorg.view;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.springframework.stereotype.Component;

import sk.lkrnac.discorg.controller.listeners.IErrorVisualizer;

/**
 * 
 * @author sitko
 * 
 */
@Component
public class ErrorVisualizer implements IErrorVisualizer {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void visualizeError(Throwable throwable) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow win = workbench.getActiveWorkbenchWindow();
		MessageDialog.openError(win.getShell(), "My Title", "My Message");
	}
}
