package net.lkrnac.discorg.model.handlers;

import net.lkrnac.discorg.controller.listeners.ILoadStoragesListener;
import net.lkrnac.discorg.model.cache.MediaIssuesCache;
import net.lkrnac.discorg.model.cache.ReferenceStorageCache;
import net.lkrnac.discorg.model.listeners.IVisualizeStoragesListener;
import net.lkrnac.discorg.model.treestorage.InputStorage;
import net.lkrnac.discorg.model.treestorage.ReferenceStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles loading of storages from disk.
 * 
 * @author sitko
 */
@Service
public class LoadStoragesHandler implements ILoadStoragesListener {
	@Autowired
	private InputStorage inputStorage;

	@Autowired
	private ReferenceStorage referenceStorage;

	@Autowired
	private ReferenceStorageCache referenceStorageCache;

	@Autowired
	private MediaIssuesCache mediaIssuesCache;

	@Autowired
	private IVisualizeStoragesListener visualizeStoragesListener;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void loadStorages() {
		referenceStorageCache.removeAll();
		mediaIssuesCache.clear();
		referenceStorage.loadStorage();
		inputStorage.loadStorage();

		// inform view listeners
		visualizeStoragesListener.visualizeStorages(referenceStorage.getRootNode(),
				inputStorage.getRootNode(), mediaIssuesCache.getIterator());
	}

}
