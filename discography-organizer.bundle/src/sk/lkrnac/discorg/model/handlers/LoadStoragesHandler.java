package sk.lkrnac.discorg.model.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.controller.listeners.ILoadStoragesListener;
import sk.lkrnac.discorg.model.cache.MediaIssuesCache;
import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;
import sk.lkrnac.discorg.model.listeners.IVisualizeStoragesListener;
import sk.lkrnac.discorg.model.treestorage.InputStorage;
import sk.lkrnac.discorg.model.treestorage.ReferenceStorage;

/**
 * Handles loading of storages from disk
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
		referenceStorage.loadStorage();
		inputStorage.loadStorage();
		
		//inform view listeners
		visualizeStoragesListener.visualizeStorages(
				referenceStorage.getRootNode(),
				inputStorage.getRootNode(),
				mediaIssuesCache.getIterator());
	}

}
