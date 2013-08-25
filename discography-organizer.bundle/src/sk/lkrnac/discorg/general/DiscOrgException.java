package sk.lkrnac.discorg.general;

import java.util.Arrays;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;

/**
 * Discography organizer exception.
 * 
 * @author sitko
 * 
 */
public class DiscOrgException extends Exception {
	private static final long serialVersionUID = 1L;
	private final MediaIssueCode mediaIssueCode;
	private String[] messageParameters;
	private final String resourcePath;

	/**
	 * Creates instance of {@link DiscOrgException}.
	 * 
	 * @param resourcePath
	 *            path of the resource on which error occurred
	 * @param mediaIssueCode
	 *            media issue code belonging to this error
	 */
	public DiscOrgException(String resourcePath, MediaIssueCode mediaIssueCode) {
		super();
		this.mediaIssueCode = mediaIssueCode;
		this.resourcePath = resourcePath;
	}

	/**
	 * Creates instance of {@link DiscOrgException}.
	 * 
	 * @param resourcePath
	 *            path of the resource on which error occurred
	 * @param mediaIssueCode
	 *            media issue code belonging to this error
	 * @param cause
	 *            nested source problem
	 */
	public DiscOrgException(String resourcePath, MediaIssueCode mediaIssueCode, Throwable cause) {
		super(cause.getLocalizedMessage());
		this.mediaIssueCode = mediaIssueCode;
		this.resourcePath = resourcePath;
	}

	/**
	 * @return Parameters to be inserted into error message
	 */
	public final String[] getMessageParameters() {
		return Arrays.copyOf(messageParameters, messageParameters.length);
	}

	/**
	 * @param messageParameters
	 *            Parameters to be inserted into error message
	 */
	public final void setMessageParameters(String[] messageParameters) {
		this.messageParameters = Arrays.copyOf(messageParameters, messageParameters.length);
	}

	/**
	 * @return media issue code belonging to the error
	 */
	public final MediaIssueCode getMediaIssueCode() {
		return mediaIssueCode;
	}

	/**
	 * @return path of the resource on which error occurred
	 */
	public final String getResourcePath() {
		return resourcePath;
	}
}
