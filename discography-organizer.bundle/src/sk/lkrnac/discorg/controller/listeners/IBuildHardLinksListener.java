package sk.lkrnac.discorg.controller.listeners;

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
	 * @throws Exception
	 *             if some error occurs
	 */
	// NOPMD: This handler needs to inform invoker about any type of error
	@SuppressWarnings("PMD.SignatureDeclareThrowsException")
	void onBuildHardLinks() throws Exception;
}
