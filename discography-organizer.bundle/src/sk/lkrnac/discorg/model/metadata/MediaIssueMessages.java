package sk.lkrnac.discorg.model.metadata;

/**
 * Static messages used in media issues
 * @author sitko
 *
 */
public final class MediaIssueMessages {
	/** Message when various suitable full albums were found for selection*/
	public static final String REFERENCE_VARIOUS_FULL_MIRRORS_FOUND = 
			"Various suitable full albums were found for selection";

	/** Message when full album is missing for selection*/
	public static final String REFERENCE_FULL_MIRROR_MISSING = 
			"Full album mirror is missing for selection";
	
	/** Message when selection album doesn't contain hard links of full album */
	public static final String REFERENCE_NO_HARD_LINK_IN_SELECTION = 
			"Selection album mirror doesn't contain hard links";

	/** Message when I/O error occurs during retrieving 
	 * hard link meta-data for selection or full album */
	public static final String REFERENCE_HARD_LINK_IO_ERROR = 
			"Unable to retrieve hard link metadata";
	
	/** Message when selection mirror is missing for full album */
	public static final String REFERENCE_MISSING_SELECTION_MIRROR = 
			"Missing selection mirror for full album";

	/** Message when various selection mirrors found for full album*/
	public static final String REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND = 
			"Various selection mirrors find for full album";

	/*----------------------------------------------------------------------*/
	/** Message I/O error occurs */
	public static final String GENERIC_COMPARING = 
			"comparing directories";

	/** Message I/O error occurs */
	public static final String GENERIC_IO_ERROR = 
			"I/O error while ";
	
	/**	Message when media directory contains media files and also media directories */
	public static final String GENERIC_MEDIA_FILES_AND_SUBDIRS = 
			"Directory contains audio files and media subdirectories";

	/** Message when media directory contains various different media formats */
	public static final String GENERIC_VARIOUS_AUDIO_FORMAT_TYPES = 
			"Contains various audio format types";
	
	/*----------------------------------------------------------------------*/ 
	/** Message when reference mirror has different file names as input mirror */
	public static final String INPUT_DIFFERENT_NAMES = 
			"Media files in reference mirror has different names";
	
	/** Message when media files are missing in reference mirror in 
	 * comparison to input mirror */
	public static final String INPUT_MISSING_MEDIA_FILES = 
			"Missing media files in reference media directory";
	
	/** Message when loss-less media directory found on input storage */
	public static final String INPUT_LOSSLESS_AUDIO_FORMAT = 
			"Lossless audio format on input storage";
	
	/**
	 * Private constructor to avoid creating instances
	 */
	private MediaIssueMessages(){
	}
}
