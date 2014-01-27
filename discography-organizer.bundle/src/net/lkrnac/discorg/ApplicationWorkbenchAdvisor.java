package net.lkrnac.discorg;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Class for configuring the application's workbench.
 * 
 * @author sitko
 * 
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "discographyorganizer" //$NON-NLS-1$
			+ ".perspectives.TreePairPerspective"; //$NON-NLS-1$

	/**
	 * Creates workbench advisor.
	 * 
	 * @param configurer
	 *            application configurer instance
	 * @return workbench advisor
	 */
	public final WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	/**
	 * @return perspective id
	 */
	public final String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
}
