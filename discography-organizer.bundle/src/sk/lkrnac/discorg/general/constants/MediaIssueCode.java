package sk.lkrnac.discorg.general.constants;

/**
 * Media directories issue codes.
 * 
 * @author sitko
 * 
 */
public enum MediaIssueCode {
	/** Message when various suitable full albums were found for selection. */
	REFERENCE_VARIOUS_FULL_MIRRORS_FOUND,

	/** Message when full album is missing for selection. */
	REFERENCE_FULL_MIRROR_MISSING,

	/** Message when selection album doesn't contain hard links of full album. */
	REFERENCE_NO_HARD_LINK_IN_SELECTION,

	/**
	 * Message when I/O error occurs during retrieving hard link meta-data for
	 * selection or full album.
	 */
	REFERENCE_HARD_LINK_IO_ERROR,

	/** Message when selection mirror is missing for full album. */
	REFERENCE_MISSING_SELECTION_MIRROR,

	/** Message when various selection mirrors found for full album. */
	REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND,

	/*----------------------------------------------------------------------*/
	/** I/O error occurred during comparing of directories. */
	GENERIC_IO_ERROR_DURING_COMPARISON,
	/**
	 * Message when media directory contains media files and also media
	 * directories.
	 */
	GENERIC_MEDIA_FILES_AND_SUBDIRS,

	/** Message when media directory contains various different media formats. */
	GENERIC_VARIOUS_AUDIO_FORMAT_TYPES,

	/** Message when reference mirror has different file names as input mirror. */
	GENERIC_DIFFERENT_NAMES,
	/*----------------------------------------------------------------------*/
	/**
	 * Message when media files are missing in reference mirror in comparison to
	 * input mirror.
	 */
	INPUT_MISSING_MEDIA_FILES,

	/** Message when loss-less media directory found on input storage. */
	INPUT_LOSSLESS_AUDIO_FORMAT,
}
