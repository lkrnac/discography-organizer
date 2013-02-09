package sk.lkrnac.discorg.controller.handlers;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import sk.lkrnac.discorg.context.DiscOrgContextHolder;
import sk.lkrnac.discorg.controller.listeners.ILoadStoragesListener;


/**
 * Handler for load storages controller command
 * @author sitko
 */
public class LoadHandler extends AbstractHandler {
	/**
	 * Handles load storages controller command.
	 * <p>
	 * Retrieves load storages listener from Spring context and
	 * sends load storages command
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ILoadStoragesListener listener = DiscOrgContextHolder
				.getInstance().getContext().getBean(ILoadStoragesListener.class);
		
		listener.loadStorages();
		
		return null;
	}
}
