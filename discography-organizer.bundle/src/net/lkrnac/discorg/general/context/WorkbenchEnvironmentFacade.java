package net.lkrnac.discorg.general.context;

import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Class providing access to various configurations and properties of
 * application workbench.
 * <p>
 * NOTE: One of the reasons of this class is fact that Java7+PowerMockito are
 * not interacting properly with each other.<a
 * href=https://groups.google.com/forum/#!topic/powermock/vngllLwhv70>See this
 * discussion.</a> So I decided not to use PowerMock at all during testing.
 * Drawback: this class will not be tested.
 * 
 * @author sitko
 * 
 */
@Component
@Lazy(value = false)
public class WorkbenchEnvironmentFacade {
	/**
	 * @return Current application locale
	 */
	public Locale getCurrentLocale() {
		String languageCode = System.getProperty("osgi.nl"); //$NON-NLS-1$
		return new Locale(languageCode);
	}

	/**
	 * Calls Eclipse error dialog static method.
	 * 
	 * @param dialogTitle
	 *            dialog title
	 * @param errorMessage
	 *            error message
	 */
	public void openErrorDialog(String dialogTitle, String errorMessage) {
		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), dialogTitle,
				errorMessage);
	}
}
