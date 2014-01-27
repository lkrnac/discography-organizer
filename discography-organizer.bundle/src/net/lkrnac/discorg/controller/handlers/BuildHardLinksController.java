package net.lkrnac.discorg.controller.handlers;

import net.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;
import net.lkrnac.discorg.general.context.DiscOrgContextAdapter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Handler for replacing media files in selection mirror directories by hard
 * links media files from full album mirrors.
 * 
 * @author sitko
 * 
 */
public class BuildHardLinksController extends AbstractHandler {
	private DiscOrgContextAdapter discOrgContextAdapter;

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
		IBuildHardLinksListener listener = getDiscOrgContextAdapter().getBean(IBuildHardLinksListener.class);
		//		try {
		listener.onBuildHardLinks();
		//		} catch (DiscOrgException error) {
		//			IErrorVisualizer errorVisualizer = DiscOrgContextHolder.getBean(IErrorVisualizer.class);
		//			errorVisualizer.visualizeError(error);
		//		}
		return null;
	}

	/**
	 * @return Context adapter, if it's null, create it
	 */
	private DiscOrgContextAdapter getDiscOrgContextAdapter() {
		if (discOrgContextAdapter == null) {
			discOrgContextAdapter = new DiscOrgContextAdapter();
		}
		return discOrgContextAdapter;
	}

}
