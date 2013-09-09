package sk.lkrnac.discorg.model.dal.io;

/**
 * Results of directory comparison.
 * 
 * @author sitko
 * 
 */
public enum EDirectoryComparisonResult {
	/** Directories can be considered as identical mirrors. */
	EQUAL,

	/**
	 * Files in comparing directories are different, probably incorrectly paired
	 * directories.
	 */
	DIFFERENT_FILES,

	/** Missing media files in full mirror. */
	MISSING_MEDIA_FILES_IN_FULL,

	/** Missing media files in selection mirror. */
	MISSING_MEDIA_FILES_IN_SELECTION;

	/**
	 * @return if comparison result is indicating that compared directories are
	 *         considered as mirrors
	 */
	public boolean areMirrors() {
		boolean result = false;
		if (EQUAL.equals(this) || MISSING_MEDIA_FILES_IN_SELECTION.equals(this)) {
			result = true;
		}
		return result;
	}
}
