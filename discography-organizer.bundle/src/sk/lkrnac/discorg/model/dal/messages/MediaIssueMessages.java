package sk.lkrnac.discorg.model.dal.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Resource bundles loader class for media issue messages translation
 * 
 * @author sitko
 * 
 */
public class MediaIssueMessages extends NLS {
	private static final String BUNDLE_NAME = "sk.lkrnac.discorg.model.dal.messages.media-issue-messages"; //$NON-NLS-1$

	/** Message when various suitable full albums were found for selection */
	public static String REFERENCE_VARIOUS_FULL_MIRRORS_FOUND;

	/** Message when full album is missing for selection */
	public static String REFERENCE_FULL_MIRROR_MISSING;

	/** Message when selection album doesn't contain hard links of full album */
	public static String REFERENCE_NO_HARD_LINK_IN_SELECTION;

	/**
	 * Message when I/O error occurs during retrieving hard link meta-data for
	 * selection or full album
	 */
	public static String REFERENCE_HARD_LINK_IO_ERROR;

	/** Message when selection mirror is missing for full album */
	public static String REFERENCE_MISSING_SELECTION_MIRROR;

	/** Message when various selection mirrors found for full album */
	public static String REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND;

	/*----------------------------------------------------------------------*/
	/** Message I/O error occurs */
	public static String GENERIC_COMPARING;

	/** Message I/O error occurs */
	public static String GENERIC_IO_ERROR;

	/**
	 * Message when media directory contains media files and also media
	 * directories
	 */
	public static String GENERIC_MEDIA_FILES_AND_SUBDIRS;

	/** Message when media directory contains various different media formats */
	public static String GENERIC_VARIOUS_AUDIO_FORMAT_TYPES;

	/** More files are in selection than in full album */
	public static String GENERIC_MORE_FILES_IN_SELECTION;

	/*----------------------------------------------------------------------*/
	/** Message when reference mirror has different file names as input mirror */
	public static String INPUT_DIFFERENT_NAMES;

	/**
	 * Message when media files are missing in reference mirror in comparison to
	 * input mirror
	 */
	public static String INPUT_MISSING_MEDIA_FILES;

	/** Message when loss-less media directory found on input storage */
	public static String INPUT_LOSSLESS_AUDIO_FORMAT;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, MediaIssueMessages.class);
	}

	private MediaIssueMessages() {
	}
}
