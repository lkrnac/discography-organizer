package sk.lkrnac.discorg.view.messages;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.osgi.util.NLS;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;

/**
 * Resource bundles loader class for media issue messages translation
 * 
 * @author sitko
 * 
 */
public final class MediaIssueMessages extends NLS {
	private static final String BUNDLE_NAME = "sk.lkrnac.discorg.view.messages." + //$NON-NLS-1$
			"media-issue-messages"; //$NON-NLS-1$

	/** Message when various suitable full albums were found for selection. */
	public static String REFERENCE_VARIOUS_FULL_MIRRORS_FOUND;

	/** Message when full album is missing for selection. */
	public static String REFERENCE_FULL_MIRROR_MISSING;

	/** Message when selection album doesn't contain hard links of full album. */
	public static String REFERENCE_NO_HARD_LINK_IN_SELECTION;

	/**
	 * Message when I/O error occurs during retrieving hard link meta-data for
	 * selection or full album.
	 */
	public static String REFERENCE_HARD_LINK_IO_ERROR;

	/** Message when selection mirror is missing for full album. */
	public static String REFERENCE_MISSING_SELECTION_MIRROR;

	/** Message when various selection mirrors found for full album. */
	public static String REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND;

	/*----------------------------------------------------------------------*/
	/** I/O error occurred during comparing of directories. */
	public static String GENERIC_IO_ERROR_DURING_COMPARISON;

	/**
	 * Message when media directory contains media files and also media
	 * directories.
	 */
	public static String GENERIC_MEDIA_FILES_AND_SUBDIRS;

	/** Message when media directory contains various different media formats. */
	public static String GENERIC_VARIOUS_AUDIO_FORMAT_TYPES;

	/** More files are in selection than in full album. */
	public static String GENERIC_MORE_FILES_IN_SELECTION;

	/*----------------------------------------------------------------------*/
	/** Message when reference mirror has different file names as input mirror. */
	public static String INPUT_DIFFERENT_NAMES;

	/**
	 * Message when media files are missing in reference mirror in comparison to
	 * input mirror.
	 */
	public static String INPUT_MISSING_MEDIA_FILES;

	/** Message when loss-less media directory found on input storage. */
	public static String INPUT_LOSSLESS_AUDIO_FORMAT;

	private static final Map<MediaIssueCode, String> MESSAGES_MAP;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, MediaIssueMessages.class);

		Map<MediaIssueCode, String> tmpMessagesMap = new HashMap<MediaIssueCode, String>();
		tmpMessagesMap.put(MediaIssueCode.GENERIC_MEDIA_FILES_AND_SUBDIRS, GENERIC_MEDIA_FILES_AND_SUBDIRS);
		tmpMessagesMap.put(MediaIssueCode.GENERIC_MORE_FILES_IN_SELECTION, GENERIC_MORE_FILES_IN_SELECTION);
		tmpMessagesMap.put(MediaIssueCode.GENERIC_VARIOUS_AUDIO_FORMAT_TYPES,
				GENERIC_VARIOUS_AUDIO_FORMAT_TYPES);
		tmpMessagesMap.put(MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON,
				GENERIC_IO_ERROR_DURING_COMPARISON);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_FULL_MIRROR_MISSING, REFERENCE_FULL_MIRROR_MISSING);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_HARD_LINK_IO_ERROR, REFERENCE_HARD_LINK_IO_ERROR);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_MISSING_SELECTION_MIRROR,
				REFERENCE_MISSING_SELECTION_MIRROR);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION,
				REFERENCE_NO_HARD_LINK_IN_SELECTION);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_VARIOUS_FULL_MIRRORS_FOUND,
				REFERENCE_VARIOUS_FULL_MIRRORS_FOUND);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND,
				REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND);
		tmpMessagesMap.put(MediaIssueCode.INPUT_DIFFERENT_NAMES, INPUT_DIFFERENT_NAMES);
		tmpMessagesMap.put(MediaIssueCode.INPUT_LOSSLESS_AUDIO_FORMAT, INPUT_LOSSLESS_AUDIO_FORMAT);
		tmpMessagesMap.put(MediaIssueCode.INPUT_MISSING_MEDIA_FILES, INPUT_MISSING_MEDIA_FILES);

		MESSAGES_MAP = Collections.unmodifiableMap(tmpMessagesMap);
	}

	/**
	 * Avoid instantiation.
	 */
	private MediaIssueMessages() {
	}

	/**
	 * Finds message for message code.
	 * 
	 * @param messageCode
	 *            message code
	 * @param messageParameters
	 *            parameters of the message
	 * @return message to show on GUI
	 */
	public static String getMessageForMessageCode(MediaIssueCode messageCode, Object... messageParameters) {
		String message = MESSAGES_MAP.get(messageCode);
		assert message != null : "Can't find message for message code: " + messageCode; //$NON-NLS-1$
		return String.format(message, messageParameters);
	}
}
