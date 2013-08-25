package sk.lkrnac.discorg.view.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Translations of media issues view.
 * 
 * @author sitko
 * 
 */
public final class MediaIssuesViewMessages extends NLS {
	private static final String BUNDLE_NAME = "sk.lkrnac.discorg.view.messages.media-issues-view-messages"; //$NON-NLS-1$

	// SUPPRESS CHECKSTYLE VisibilityModifier 20 NLS implementation needs public
	// message fields

	/** Translation of error string. */
	public static String errorTranslation;
	/** Translation of media directory name column. */
	public static String mediaDirectoryNameColumn;
	/** Translation of text column. */
	public static String textColumn;
	/** Translation of type column. */
	public static String typeColumn;
	/** Translation of warning string. */
	public static String warningTranslation;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, MediaIssuesViewMessages.class);
	}

	/**
	 * Avoid instantiation.
	 */
	private MediaIssuesViewMessages() {
		super();
	}
}
