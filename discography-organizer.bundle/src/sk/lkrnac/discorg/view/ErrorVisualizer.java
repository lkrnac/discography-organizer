package sk.lkrnac.discorg.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sk.lkrnac.discorg.controller.listeners.IErrorVisualizer;
import sk.lkrnac.discorg.general.DiscOrgException;
import sk.lkrnac.discorg.general.context.WorkbenchEnvironmentFacade;
import sk.lkrnac.discorg.view.messages.ErrorMessages;
import sk.lkrnac.discorg.view.messages.MediaIssueMessages;

/**
 * Visualizes error message to user.
 * 
 * @author sitko
 * 
 */
@Component
public class ErrorVisualizer implements IErrorVisualizer {
	@Autowired
	private WorkbenchEnvironmentFacade workbenchEnvironment;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void visualizeError(DiscOrgException error) {
		StringBuilder message = new StringBuilder();
		message.append(ErrorMessages.getErrorDialogPrefix(error.getResourcePath()));
		if (error.getMediaIssueCode() == null) {
			message.append(error.getLocalizedMessage());
		} else {
			message.append(MediaIssueMessages.getMessageForMessageCode(error.getMediaIssueCode()));
		}
		workbenchEnvironment.openErrorDialog(ErrorMessages.errorDialogTitle, message.toString());
	}
}
