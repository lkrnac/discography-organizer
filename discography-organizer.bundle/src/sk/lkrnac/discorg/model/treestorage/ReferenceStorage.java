package sk.lkrnac.discorg.model.treestorage;


import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;
import sk.lkrnac.discorg.model.metadata.StorageMetadataMaps;
import sk.lkrnac.discorg.model.treestorage.node.MediaBranchNode;
import sk.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;
import sk.lkrnac.discorg.model.treestorage.node.TreeStorageBranchNode;
import sk.lkrnac.discorg.preferences.StoragesPreferencesFacade;

/**
 * Reference media storage. This media storage contains all the full albums 
 * and also album selections. Full albums are in sub-directories marked according to 
 * preferences setting  
 * @author sitko
 */
@Service
public class ReferenceStorage extends TreeStorage {
	@Autowired
	private StorageMetadataMaps storageMetadataMaps;
	
	@Autowired
	private StoragesPreferencesFacade storagesPreferences;

	/**
	 * @return application preferences concerning to storages 
	 */
	public StoragesPreferencesFacade getSroragesPreferences() {
		return storagesPreferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStoragePath() {
		return getSroragesPreferences().getReferenceStorage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadNode(ITreeStorageNode node) {
		if (node instanceof ReferenceMediaNode) {
			ReferenceMediaNode mediaNode = (ReferenceMediaNode) node;

			mediaNode.checkMediaSubDir();

			// save media node into meta data map
			storageMetadataMaps.putReferenceItem(mediaNode);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean needPostLoadProcessing() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void processNodePostLoad(ITreeStorageNode treeNode) {
		if (treeNode instanceof ReferenceMediaNode) {
			ReferenceMediaNode mediaNode = (ReferenceMediaNode) treeNode;
			
			// check if this album is not stored in full album sub-directory
			String fullSubDirectory = getSroragesPreferences().getFullSubDirectory();
			
			if (mediaNode.getRelativePath().contains(fullSubDirectory)) {
				mediaNode.setFullAlbum(true);
				mediaNode.checkSelectionForFullAlbum(fullSubDirectory);
			} else {
				mediaNode.checkFullAlbumForSelection(fullSubDirectory);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @return {@link ReferenceMediaNode} instance
	 */
	@Override
	protected MediaBranchNode createMediaNode(TreeStorageBranchNode parent,
			File file, String relativePath) {
		return new ReferenceMediaNode(parent, file, relativePath);
	}
}