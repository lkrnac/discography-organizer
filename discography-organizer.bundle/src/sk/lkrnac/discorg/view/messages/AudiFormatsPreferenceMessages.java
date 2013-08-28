package sk.lkrnac.discorg.view.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Translations of audio formats preference page.
 * 
 * @author sitko
 * 
 */
public final class AudiFormatsPreferenceMessages extends NLS {
	private static final String BUNDLE_NAME = "sk.lkrnac.discorg.view.messages" //$NON-NLS-1$
			+ ".audio-formats-preference-messages"; //$NON-NLS-1$

	// SUPPRESS CHECKSTYLE VisibilityModifier 15 NLS implementation needs public
	// message fields
	/** Loss-less format field preference translation. */
	public static String losslessFormats;
	/** Lossy format field preference translation. */
	public static String lossyFormats;
	/** Warning if empty field preference translation. */
	public static String warningEmptyFlag;
	/** Warning audio formats field preference translation. */
	public static String warningFormats;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, AudiFormatsPreferenceMessages.class);
	}

	/**
	 * Avoid instantiation.
	 */
	private AudiFormatsPreferenceMessages() {
		super();
	}
}
