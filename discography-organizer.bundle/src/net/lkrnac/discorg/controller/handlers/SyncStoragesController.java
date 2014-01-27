package net.lkrnac.discorg.controller.handlers;

import net.lkrnac.discorg.controller.listeners.ISyncStoragesListener;
import net.lkrnac.discorg.general.context.DiscOrgContextAdapter;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * Handler for synchronize storages controller command.
 * 
 * @author sitko
 */
public class SyncStoragesController extends AbstractHandler {
	/**
	 * Handles synchronize storages controller command.
	 * <p>
	 * Retrieves synchronize storages listener from Spring context and sends
	 * command
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISyncStoragesListener listener = new DiscOrgContextAdapter().getBean(ISyncStoragesListener.class);

		listener.syncStorages();

		return null;
	}

}
