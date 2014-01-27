package net.lkrnac.discorg.general;

import net.lkrnac.discorg.general.constants.MediaIssueCode;

/**
 * Discography organizer exception.
 * 
 * @author sitko
 * 
 */
public class DiscOrgException extends Exception {
	private static final long serialVersionUID = 1L;
	private MediaIssueCode mediaIssueCode;
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
	 * @param cause
	 *            nested source problem
	 */
	public DiscOrgException(String resourcePath, Throwable cause) {
		super(cause.getLocalizedMessage(), cause);
		this.resourcePath = resourcePath;
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
