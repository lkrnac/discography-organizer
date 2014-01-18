package sk.lkrnac.discorg.model.treestorage;

import java.beans.Introspector;
import java.io.File;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;
import sk.lkrnac.discorg.model.preferences.StoragesPreferences;
import sk.lkrnac.discorg.model.treestorage.node.BranchNodeStatus;
import sk.lkrnac.discorg.model.treestorage.node.InputMediaNode;
import sk.lkrnac.discorg.model.treestorage.node.MediaBranchNode;
import sk.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;
import sk.lkrnac.discorg.model.treestorage.node.TreeStorageBranchNode;

/**
 * Handles operations on input media storage.
 * 
 * @author sitko
 */
@Service
public class InputStorage extends AbstractTreeStorage {
	@Autowired
	private ReferenceStorageCache referenceStorageCache;

	@Autowired
	private StoragesPreferences storagesPreferences;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final String getStoragePath() {
		return storagesPreferences.getInputStorage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void loadNode(ITreeStorageNode node) {
		if (node instanceof InputMediaNode) {
			InputMediaNode mediaNode = (InputMediaNode) node;
			// find pairs
			Collection<ReferenceMediaNode> referenceMirrors =
					referenceStorageCache.getReferenceItems(mediaNode.getRelativePath());

			if (referenceMirrors == null || referenceMirrors.isEmpty()) {
				mediaNode.setNodeStatus(BranchNodeStatus.UPLOAD);
			} else {
				for (ReferenceMediaNode mirror : referenceMirrors) {
					mediaNode.addReferenceMirror(mirror);
					mediaNode.compareMediaFilesWithMirror(mirror);
				}
			}
			mediaNode.checkLossless();
		}
	}

	/**
	 * {@inheritDoc}.
	 * <p>
	 * This implementation returns <code>false</code> and causes that
	 * {@link InputStorage#processNodePostLoad(ITreeStorageNode)} wouldn't be
	 * used
	 */
	@Override
	protected final boolean needPostLoadProcessing() {
		return false;
	}

	/**
	 * {@inheritDoc}.
	 * <p>
	 * This wouldn't be invoked because
	 * {@link InputStorage#needPostLoadProcessing()} returns false
	 */
	@Override
	protected void processNodePostLoad(ITreeStorageNode treeNode) {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@link InputMediaNode} instance
	 */
	@Override
	protected final MediaBranchNode createMediaNode(TreeStorageBranchNode parent, File file,
			String relativePath) {
		String beanName = Introspector.decapitalize(InputMediaNode.class.getSimpleName());
		return (InputMediaNode) getApplicationContext().getBean(beanName, parent, file, relativePath);
	}

}
