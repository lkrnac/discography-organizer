package sk.lkrnac.discorg.controller.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import sk.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;
import sk.lkrnac.discorg.general.DiscOrgException;
import sk.lkrnac.discorg.general.context.DiscOrgContextHolder;

/**
 * Handler for replacing media files in selection mirror directories by hard
 * links media files from full album mirrors.
 * 
 * @author sitko
 * 
 */
public class BuildHardLinksHandler extends AbstractHandler {
	/**
	 * Build hard links of media files from full album mirrors in selection
	 * mirrors directories.
	 * <p>
	 * Retrieves build hard links listener from Spring context and sends command
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		IBuildHardLinksListener listener = DiscOrgContextHolder.getInstance().getContext()
				.getBean(IBuildHardLinksListener.class);

		String errorMessage = null;
		try {
			listener.onBuildHardLinks();
		} catch (DiscOrgException e) {
			errorMessage = String.format(e.getLocalizedMessage(), (Object[]) e.getMessageParameters());
		} catch (Throwable t) {

		}

		return null;
	}

}
