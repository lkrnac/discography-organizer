package sk.lkrnac.discorg.model.treestorage;


import java.io.File;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;
import sk.lkrnac.discorg.model.treestorage.node.BranchNodeStatus;
import sk.lkrnac.discorg.model.treestorage.node.InputMediaNode;
import sk.lkrnac.discorg.model.treestorage.node.MediaBranchNode;
import sk.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;
import sk.lkrnac.discorg.model.treestorage.node.TreeStorageBranchNode;
import sk.lkrnac.discorg.preferences.StoragesPreferencesFacade;

/**
 * Handles operations on input media storage
 * @author sitko
 */
@Service
public class InputStorage extends TreeStorage {
	@Autowired
	private ReferenceStorageCache storageMetadataMaps;
	
	@Autowired
	private StoragesPreferencesFacade storagesPreferences;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStoragePath() {
		return storagesPreferences.getInputStorage();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadNode(ITreeStorageNode node) {
		if (node != null && node instanceof InputMediaNode) {
			InputMediaNode mediaNode = (InputMediaNode) node;
			// find pairs
			Collection<ReferenceMediaNode> referenceMirrors =
			storageMetadataMaps.getReferenceItems(mediaNode.getRelativePath());

			if (referenceMirrors == null || referenceMirrors.size() == 0){
				mediaNode.setNodeStatus(BranchNodeStatus.UPLOAD);
			} else {
				for (ReferenceMediaNode mirror : referenceMirrors){
					mediaNode.addReferenceMirror(mirror);
					mediaNode.compareMediaFilesWithMirror(mirror);
				}
			}
			mediaNode.checkLossless();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation returns <code>false</code> and causes that 
	 * {@link InputStorage#processNodePostLoad(ITreeStorageNode)} wouldn't be used
	 */
	@Override
	protected boolean needPostLoadProcessing() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This wouldn't be invoked because {@link InputStorage#needPostLoadProcessing()}
	 * returns false
	 */
	@Override
	protected void processNodePostLoad(ITreeStorageNode treeNode) {
		//do nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @return {@link InputMediaNode} instance
	 */
	@Override
	protected MediaBranchNode createMediaNode(TreeStorageBranchNode parent,
			File file, String relativePath) {
		return new InputMediaNode(parent, file, relativePath);
	}

}
