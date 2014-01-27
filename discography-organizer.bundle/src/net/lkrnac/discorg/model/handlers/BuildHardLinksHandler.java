package net.lkrnac.discorg.model.handlers;

import java.util.Collection;

import net.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;
import net.lkrnac.discorg.general.constants.MediaIssueCode;
import net.lkrnac.discorg.model.cache.MediaIssuesCache;
import net.lkrnac.discorg.model.cache.ReferenceStorageCache;
import net.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Replaces media files in selection mirror directories by hard links of media
 * files from full album mirrors.
 * 
 * @author sitko
 */
@Service
public class BuildHardLinksHandler implements IBuildHardLinksListener {
	@Autowired
	private MediaIssuesCache mediaIssuesCache;

	@Autowired
	private ReferenceStorageCache referenceStorageCache;

	@Autowired
	private LoadStoragesHandler loadStoragesHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onBuildHardLinks() {
		Collection<String> selectionPaths = mediaIssuesCache
				.getSourceAbsolutePaths(MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION);
		for (String selectionPath : selectionPaths) {
			ReferenceMediaNode selectionNode = referenceStorageCache.getReferenceMediaNode(selectionPath);
			ReferenceMediaNode fullNode = selectionNode.getFullMirror();
			selectionNode.getDirectoryIoFacade().buildHardLinks(fullNode.getFile());
		}
		loadStoragesHandler.loadStorages();
	}

}
