package sk.lkrnac.discorg.model.handlers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;
import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.model.cache.MediaIssuesCache;
import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;
import sk.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;
import sk.lkrnac.discorg.preferences.StoragesPreferences;

/**
 * Replaces media files in selection mirror directories by hard links of media
 * files from full album mirrors
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
	private StoragesPreferences storagesPreferences;

	@Autowired
	private LoadStoragesHandler loadStoragesHandler;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBuildHardLinks() throws Exception {
		Collection<String> selectionPaths = mediaIssuesCache
				.getSourceAbsolutePaths(MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION);
		for (String selectionPath : selectionPaths) {
			ReferenceMediaNode selectionNode = referenceStorageCache
					.getReferenceMediaNode(selectionPath);
			ReferenceMediaNode fullNode = selectionNode.getFullMirror();
			// selectionNode.getDirectoryIoFacade().buildHardLinks(fullNode.getFile());
		}
		// loadStoragesHandler.loadStorages();
		throw new UnsupportedOperationException("tu sme");
	}

}
