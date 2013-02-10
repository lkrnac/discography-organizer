package sk.lkrnac.discorg.model.handlers;

import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;

/**
 * Replaces media files in selection mirror directories
 * by hard links of media files from full album mirrors 
 * @author sitko
 */
@Service
public class BuildHardLinksHandler implements IBuildHardLinksListener {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBuildHardLinks() {
		// TODO implement
		new UnsupportedOperationException("not implemented");
	}
}
