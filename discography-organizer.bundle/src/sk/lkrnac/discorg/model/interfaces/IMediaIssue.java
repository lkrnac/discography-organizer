package sk.lkrnac.discorg.model.interfaces;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;

/**
 * Is used for marking some problems with media directory
 * 
 * @author sitko
 * 
 */
public interface IMediaIssue {
	/**
	 * @return ID of item that is source of issue
	 */
	public String getSourceAbsolutePath();

	/**
	 * @return code of the issue
	 */
	public MediaIssueCode getIssueCode();

	/**
	 * Gets error flag
	 * 
	 * @return <code>false</code> - if warning<br>
	 *         <code>true</code> - if error
	 */
	public boolean isError();

	/**
	 * @return relative path of item which caused error
	 */
	public String getRelativePath();

	/**
	 * @return optional error message belonging to the issue
	 */
	public String getErrorMessage();

}
