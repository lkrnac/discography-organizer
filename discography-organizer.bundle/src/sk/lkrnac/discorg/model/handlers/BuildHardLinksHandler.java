package sk.lkrnac.discorg.model.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;
import sk.lkrnac.discorg.model.cache.MediaIssuesCache;
import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;

/**
 * Replaces media files in selection mirror directories by hard links of media
 * files from full album mirrors
 * 
 * @author sitko
 */
@Service
public class BuildHardLinksHandler implements IBuildHardLinksListener {
	private MediaIssuesCache mediaIssuesCache;
	private ReferenceStorageCache referenceStorageCache;

	/**
	 * @param mediaIssuesCache
	 *            injects {@link MediaIssuesCache} instance
	 */
	@Autowired
	public void setMediaIssuesCache(MediaIssuesCache mediaIssuesCache) {
		this.mediaIssuesCache = mediaIssuesCache;
	}

	/**
	 * @param referenceStorageCache
	 *            injects {@link ReferenceStorageCache} instance
	 */
	@Autowired
	public void setReferenceStorageCache(ReferenceStorageCache referenceStorageCache) {
		this.referenceStorageCache = referenceStorageCache;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBuildHardLinks() {
		// TODO: implement
		new UnsupportedOperationException("not implemented");
	}

}
