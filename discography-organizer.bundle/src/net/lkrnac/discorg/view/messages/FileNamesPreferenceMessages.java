package net.lkrnac.discorg.view.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Translations of file names preferences.
 * 
 * @author sitko
 * 
 */
public final class FileNamesPreferenceMessages extends NLS {
	private static final String BUNDLE_NAME = "net.lkrnac.discorg.view.messages" //$NON-NLS-1$
			+ ".file-names-preference-messages"; //$NON-NLS-1$

	// SUPPRESS CHECKSTYLE VisibilityModifier 15 NLS implementation needs public
	// message fields
	/**
	 * Regular expression of string that will be ignored as part of path during
	 * directory comparison.
	 */
	public static String regexIgnorePattern;
	/**
	 * Characters that will be ignored as part of path during directory
	 * comparison.
	 */
	public static String charsIgnorePattern;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, FileNamesPreferenceMessages.class);
	}

	/**
	 * Avoid instantiation.
	 */
	private FileNamesPreferenceMessages() {
		super();
	}
}
