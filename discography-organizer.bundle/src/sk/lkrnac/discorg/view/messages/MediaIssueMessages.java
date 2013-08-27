package sk.lkrnac.discorg.view.messages;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.osgi.util.NLS;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;

/**
 * Resource bundles loader class for media issue messages translation.
 * 
 * @author sitko
 * 
 */
public final class MediaIssueMessages extends NLS {
	private static final String BUNDLE_NAME = "sk.lkrnac.discorg.view.messages." + //$NON-NLS-1$
			"media-issue-messages"; //$NON-NLS-1$

	// SUPPRESS CHECKSTYLE VisibilityModifier 60 NLS implementation needs public
	// message fields

	/** Message when various suitable full albums were found for selection. */
	public static String referenceVariousFullMirrorsFound;

	/** Message when full album is missing for selection. */
	public static String referenceFullMirrorMissing;

	/** Message when selection album doesn't contain hard links of full album. */
	public static String referenceNoHardLinkInSelection;

	/**
	 * Message when I/O error occurs during retrieving hard link meta-data for
	 * selection or full album.
	 */
	public static String referenceHardLinkIoError;

	/** Message when selection mirror is missing for full album. */
	public static String referenceMissingSelectionMirror;

	/** Message when various selection mirrors found for full album. */
	public static String referenceVariousSelectionMirrorsFound;

	/*----------------------------------------------------------------------*/
	/** I/O error occurred during comparing of directories. */
	public static String genericIoErrorDuringComparison;

	/**
	 * Message when media directory contains media files and also media
	 * directories.
	 */
	public static String genericMediaFilesAndSubdirs;

	/** Message when media directory contains various different media formats. */
	public static String genericVariousAudioFormatTypes;

	/** More files are in selection than in full album. */
	public static String genericMoreFilesInSelection;

	/*----------------------------------------------------------------------*/
	/** Message when reference mirror has different file names as input mirror. */
	public static String inputDifferentNames;

	/**
	 * Message when media files are missing in reference mirror in comparison to
	 * input mirror.
	 */
	public static String inputMissingMediaFiles;

	/** Message when loss-less media directory found on input storage. */
	public static String inputLosslessAudioFormat;

	private static final Map<MediaIssueCode, String> MESSAGES_MAP;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, MediaIssueMessages.class);

		Map<MediaIssueCode, String> tmpMessagesMap = new HashMap<MediaIssueCode, String>();
		tmpMessagesMap.put(MediaIssueCode.GENERIC_MEDIA_FILES_AND_SUBDIRS, genericMediaFilesAndSubdirs);
		tmpMessagesMap.put(MediaIssueCode.GENERIC_MORE_FILES_IN_SELECTION, genericMoreFilesInSelection);
		tmpMessagesMap.put(MediaIssueCode.GENERIC_VARIOUS_AUDIO_FORMAT_TYPES, genericVariousAudioFormatTypes);
		tmpMessagesMap.put(MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON, genericIoErrorDuringComparison);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_FULL_MIRROR_MISSING, referenceFullMirrorMissing);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_HARD_LINK_IO_ERROR, referenceHardLinkIoError);
		tmpMessagesMap
				.put(MediaIssueCode.REFERENCE_MISSING_SELECTION_MIRROR, referenceMissingSelectionMirror);
		tmpMessagesMap
				.put(MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION, referenceNoHardLinkInSelection);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_VARIOUS_FULL_MIRRORS_FOUND,
				referenceVariousFullMirrorsFound);
		tmpMessagesMap.put(MediaIssueCode.REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND,
				referenceVariousSelectionMirrorsFound);
		tmpMessagesMap.put(MediaIssueCode.INPUT_DIFFERENT_NAMES, inputDifferentNames);
		tmpMessagesMap.put(MediaIssueCode.INPUT_LOSSLESS_AUDIO_FORMAT, inputLosslessAudioFormat);
		tmpMessagesMap.put(MediaIssueCode.INPUT_MISSING_MEDIA_FILES, inputMissingMediaFiles);

		MESSAGES_MAP = Collections.unmodifiableMap(tmpMessagesMap);
	}

	/**
	 * Avoid instantiation.
	 */
	private MediaIssueMessages() {
		super();
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
	// NOPMD: PMD doesn't like assertion of the message
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public static String getMessageForMessageCode(MediaIssueCode messageCode, String... messageParameters) {
		String message = MESSAGES_MAP.get(messageCode);
		assert message != null : "Can't find message for message code: " + messageCode; //$NON-NLS-1$
		return String.format(message, (Object []) messageParameters);
	}
}
