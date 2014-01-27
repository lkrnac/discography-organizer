package net.lkrnac.discorg.model.interfaces;

import net.lkrnac.discorg.general.constants.MediaIssueCode;

/**
 * Is used for marking some problems with media directory.
 * 
 * @author sitko
 * 
 */
public interface IMediaIssue {
	/**
	 * @return ID of item that is source of issue
	 */
	String getSourceAbsolutePath();

	/**
	 * @return code of the issue
	 */
	MediaIssueCode getIssueCode();

	/**
	 * Gets error flag.
	 * 
	 * @return <code>false</code> - if warning<br>
	 *         <code>true</code> - if error
	 */
	boolean isError();

	/**
	 * @return relative path of item which caused error
	 */
	String getRelativePath();

	/**
	 * @return optional error message belonging to the issue
	 */
	String getErrorMessage();

}
