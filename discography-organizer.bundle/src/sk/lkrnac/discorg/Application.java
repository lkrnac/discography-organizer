package sk.lkrnac.discorg;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.springframework.context.ApplicationContext;

import sk.lkrnac.discorg.general.context.DiscOrgContextHolder;

/**
 * This class controls all aspects of the application's execution.
 */
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	/**
	 * {@inheritDoc}
	 */
	// NOPMD: Signature of this method is declared in Eclipse libraries
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	public final Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();

		registerListeners();
		Object result = null;
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				result = IApplication.EXIT_RESTART;
			} else {
				result = IApplication.EXIT_OK;
			}
		} finally {
			display.dispose();
		}
		return result;
	}

	/**
	 * Register listeners.
	 */
	private void registerListeners() {
		ApplicationContext context = DiscOrgContextHolder.getInstance().getContext();

		// listener for properties change
		IPropertyChangeListener propertyChangeListener = context.getBean(IPropertyChangeListener.class);
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(propertyChangeListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("PMD.DoNotUseThreads")
	public final void stop() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}
}
