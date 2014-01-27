package net.lkrnac.discorg.model.treestorage;

import java.beans.Introspector;
import java.io.File;

import net.lkrnac.discorg.model.cache.ReferenceStorageCache;
import net.lkrnac.discorg.model.interfaces.ITreeStorageNode;
import net.lkrnac.discorg.model.preferences.StoragesPreferences;
import net.lkrnac.discorg.model.treestorage.node.MediaBranchNode;
import net.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;
import net.lkrnac.discorg.model.treestorage.node.TreeStorageBranchNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Reference media storage. This media storage contains all the full albums and
 * also album selections. Full albums are in sub-directories marked according to
 * preferences setting
 * 
 * @author sitko
 */
@Service
public class ReferenceStorage extends AbstractTreeStorage {
	@Autowired
	private ReferenceStorageCache referenceStorageCache;

	@Autowired
	private StoragesPreferences storagesPreferences;

	/**
	 * @return application preferences concerning to storages
	 */
	public final StoragesPreferences getSroragesPreferences() {
		return storagesPreferences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final String getStoragePath() {
		return getSroragesPreferences().getReferenceStorage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void loadNode(ITreeStorageNode node) {
		if (node instanceof ReferenceMediaNode) {
			ReferenceMediaNode mediaNode = (ReferenceMediaNode) node;

			mediaNode.checkMediaSubDir();

			// save media node into meta data map
			referenceStorageCache.putReferenceItem(mediaNode);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final boolean needPostLoadProcessing() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void processNodePostLoad(ITreeStorageNode treeNode) {
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
	 * 
	 * @return {@link ReferenceMediaNode} instance
	 */
	@Override
	protected final MediaBranchNode createMediaNode(TreeStorageBranchNode parent, File file,
			String relativePath) {
		String beanName = Introspector.decapitalize(ReferenceMediaNode.class.getSimpleName());
		return (ReferenceMediaNode) getApplicationContext().getBean(beanName, parent, file, relativePath);
	}
}
