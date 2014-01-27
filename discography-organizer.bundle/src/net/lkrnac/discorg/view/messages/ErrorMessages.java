package net.lkrnac.discorg.view.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Static class that helps translate general error messages shown to user.
 * 
 * @author sitko
 */
public final class ErrorMessages extends NLS {
	private static final String BUNDLE_NAME = "net.lkrnac.discorg.view.messages.error-messages"; //$NON-NLS-1$

	/**
	 * Prefix of the error message with specified resource path as string
	 * parameter.
	 */
	// SUPPRESS CHECKSTYLE VisibilityModifier 15 lines: NLS functionality
	// requires public fields to assign translations
	public static String errorDialogPrefix;

	/** Title of error message dialog. */
	public static String errorDialogTitle;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ErrorMessages.class);
	}

	/**
	 * Avoid instantiation.
	 */
	private ErrorMessages() {
		super();
	}

	/**
	 * Finds message for message code.
	 * 
	 * @param resourcePath
	 *            path of the resource on which error occurred
	 * @return message to show on GUI
	 */
	public static String getErrorDialogPrefix(String resourcePath) {
		return String.format(errorDialogPrefix, resourcePath);
	}
}
