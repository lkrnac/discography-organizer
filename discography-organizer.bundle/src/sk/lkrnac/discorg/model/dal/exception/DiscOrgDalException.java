package sk.lkrnac.discorg.model.dal.exception;

import sk.lkrnac.discorg.constants.MediaIssueCode;

/**
 * Data access layer exception
 * 
 * @author sitko
 * 
 */
public class DiscOrgDalException extends Exception {
	private static final long serialVersionUID = 1L;
	private MediaIssueCode mediaIssueCode;

	/**
	 * @return media issue code belonging to the error
	 */
	public MediaIssueCode getMediaIssueCode() {
		return mediaIssueCode;
	}

	/**
	 * Creates instance of {@link DiscOrgDalException}
	 * 
	 * @param mediaIssueCode
	 *            media issue code belonging to this error
	 */
	public DiscOrgDalException(MediaIssueCode mediaIssueCode) {
		this.mediaIssueCode = mediaIssueCode;
	}

	/**
	 * Creates instance of {@link DiscOrgDalException}
	 * 
	 * @param mediaIssueCode
	 *            media issue code belonging to this error
	 * @param cause
	 *            nested source problem
	 */
	public DiscOrgDalException(MediaIssueCode mediaIssueCode, Throwable cause) {
		super(cause.getLocalizedMessage());
		this.mediaIssueCode = mediaIssueCode;
	}

}
