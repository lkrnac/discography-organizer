package net.lkrnac.discorg.model.preferences;

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
		//this message is only for logging and shouldn't be shown to user
		return "Audio format " + format + " is marked as lossy and also lossless"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
