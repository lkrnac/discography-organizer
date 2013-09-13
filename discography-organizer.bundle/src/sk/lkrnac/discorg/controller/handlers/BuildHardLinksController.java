package sk.lkrnac.discorg.controller.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import sk.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;
import sk.lkrnac.discorg.general.context.DiscOrgContextHolder;

/**
 * Handler for replacing media files in selection mirror directories by hard
 * links media files from full album mirrors.
 * 
 * @author sitko
 * 
 */
public class BuildHardLinksController extends AbstractHandler {
	/**
	 * Build hard links of media files from full album mirrors in selection
	 * mirrors directories.
	 * <p>
	 * Retrieves build hard links listener from Spring context and sends command
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IBuildHardLinksListener listener = DiscOrgContextHolder.getBean(IBuildHardLinksListener.class);
		//		try {
		listener.onBuildHardLinks();
		//		} catch (DiscOrgException error) {
		//			IErrorVisualizer errorVisualizer = DiscOrgContextHolder.getBean(IErrorVisualizer.class);
		//			errorVisualizer.visualizeError(error);
		//		}
		return null;
	}

}
