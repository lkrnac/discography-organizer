package net.lkrnac.discorg;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Class for configuring application's workbench window.
 * 
 * @author sitko
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private static final int Y_SIZE = 300;
	private static final int X_SIZE = 400;

	/**
	 * Creates instance of {@link ApplicationWorkbenchWindowAdvisor}.
	 * 
	 * @param configurer
	 *            an object for configuring the workbench window
	 */
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/**
	 * Action triggered when application is opened.
	 */
	public final void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(X_SIZE, Y_SIZE));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		configurer.setShowPerspectiveBar(true);
		configurer.setTitle("Discography Organizer"); //$NON-NLS-1$
	}

	@Override
	public final void postWindowCreate() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.getWindow().getShell().setMaximized(true);
	}

}
