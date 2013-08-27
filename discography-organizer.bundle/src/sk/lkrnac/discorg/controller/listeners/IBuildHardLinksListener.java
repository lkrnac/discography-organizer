package sk.lkrnac.discorg.controller.listeners;

import sk.lkrnac.discorg.general.DiscOrgException;

/**
 * Command from user, which is replacing media files in selection mirror
 * directories by hard links of media files from full album mirrors.
 * 
 * @author sitko
 * 
 */
public interface IBuildHardLinksListener {
	/**
	 * Build hard links command.
	 * 
	 * @throws DiscOrgException
	 *             if some application specific error occurs
	 */
	void onBuildHardLinks() throws DiscOrgException;
}
