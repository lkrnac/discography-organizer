package sk.lkrnac.discorg.model.handlers;

import org.springframework.stereotype.Component;

import sk.lkrnac.discorg.controller.listeners.ISyncStoragesListener;

/**
 * 
 * @author sitko
 *
 */
@Component
public class SyncStoragesHandler implements ISyncStoragesListener{

	@Override
	public void syncStorages() {
		// TODO implement
		new UnsupportedOperationException("not implemented");
	}
	
}

