package sk.lkrnac.discorg.preferences;

/**
 * Runtime exception that discribes situation when same extension is configured
 * as lossy and even as loss-less.
 * 
 * @author sitko
 */
public class AudioFormatsPreferencesException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String format;

	/**
	 * Creates audio format exception instance.
	 * 
	 * @param format
	 *            audio format extension
	 */
	public AudioFormatsPreferencesException(String format) {
		super();
		this.format = format;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getMessage() {
		return "ERROR: Audio format " + format + " is marked as lossy and also lossless";
	}

}
