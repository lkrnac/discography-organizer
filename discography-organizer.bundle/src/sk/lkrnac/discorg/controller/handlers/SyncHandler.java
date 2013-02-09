package sk.lkrnac.discorg.controller.handlers;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import sk.lkrnac.discorg.context.DiscOrgContextHolder;
import sk.lkrnac.discorg.controller.listeners.ISyncStoragesListener;

/**
 * Handler for synchronize storages controller command
 * @author sitko
 */
public class SyncHandler extends AbstractHandler {
	/**
	 * Handles synchronize storages controller command.
	 * <p>
	 * Retrieves synchronize storages listener from Spring context and
	 * sends command
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISyncStoragesListener listener = DiscOrgContextHolder.getInstance()
				.getContext().getBean(ISyncStoragesListener.class);
		
		listener.syncStorages();
		
		return null;
	}

}
