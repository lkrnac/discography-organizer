package sk.lkrnac.discorg.view.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Static class that helps translate general error messages shown to user.
 * 
 * @author sitko
 */
public final class ErrorMessages extends NLS {
	private static final String BUNDLE_NAME = "sk.lkrnac.discorg.view.messages.error-messages"; //$NON-NLS-1$

	/**
	 * Prefix of the error message with specified resource path as string
	 * parameter.
	 */
	// SUPPRESS CHECKSTYLE VisibilityModifier 10 lines: NLS functionality
	// requires public fields to assign translations
	public static String errorMessage;

	/**
	 * Prefix of unexpected error message with specified resource path as string
	 * parameter.
	 */
	public static String unexpectedErrorMessage;

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
	 * @param expected
	 *            flag if the error is unexpected
	 * @param resourcePath
	 *            path of the resource on which error occurred
	 * @return message to show on GUI
	 */
	public static String getErrorMessage(boolean expected, String resourcePath) {
		String message = expected ? errorMessage : unexpectedErrorMessage;
		return String.format(message, resourcePath);
	}
}
