package sk.lkrnac.discorg.general.constants;

/**
 * Audio formats preferences IDs.
 * 
 * @author sitko
 * 
 */
public final class AudioFormatsPreferenceIds {
	/** Lossy audio formats preference ID. */
	public static final String LOSSY_FORMATS = "lossyFormats"; //$NON-NLS-1$
	/** Loss-less audio formats preference ID. */
	public static final String LOSSLESS_FORMATS = "losslessFormats"; //$NON-NLS-1$
	/** Warning audio formats preference ID. */
	public static final String WARNING_FORMATS = "warningFormats"; //$NON-NLS-1$
	/** Empty directory flag preference ID. */
	public static final String WARNING_EMPTY_FLAG = "warningEmptyFlag"; //$NON-NLS-1$

	/**
	 * Avoid instantiation.
	 */
	private AudioFormatsPreferenceIds() {
	}
}
