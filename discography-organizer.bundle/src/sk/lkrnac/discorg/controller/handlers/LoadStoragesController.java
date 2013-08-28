package sk.lkrnac.discorg.controller.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import sk.lkrnac.discorg.controller.listeners.ILoadStoragesListener;
import sk.lkrnac.discorg.general.context.DiscOrgContextHolder;

/**
 * Handler for load storages controller command.
 * 
 * @author sitko
 */
public class LoadStoragesController extends AbstractHandler {
	/**
	 * Handles load storages controller command.
	 * <p>
	 * Retrieves load storages listener from Spring context and sends load
	 * storages command
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ILoadStoragesListener listener = DiscOrgContextHolder.getInstance().getContext()
				.getBean(ILoadStoragesListener.class);

		listener.loadStorages();

		return null;
	}
}
