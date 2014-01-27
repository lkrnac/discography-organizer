package net.lkrnac.discorg.view;

import net.lkrnac.discorg.controller.listeners.IErrorVisualizer;
import net.lkrnac.discorg.general.DiscOrgException;
import net.lkrnac.discorg.general.context.WorkbenchEnvironmentFacade;
import net.lkrnac.discorg.view.messages.ErrorMessages;
import net.lkrnac.discorg.view.messages.MediaIssueMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
