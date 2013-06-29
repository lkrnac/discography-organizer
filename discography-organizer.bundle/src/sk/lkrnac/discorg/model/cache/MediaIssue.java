package sk.lkrnac.discorg.model.cache;

import sk.lkrnac.discorg.model.interfaces.IMediaIssue;

/**
 * Is used for tracking various problems on media storages
 * @author sitko
 */
public class MediaIssue implements IMediaIssue{
	private String sourceAbsolutePath;
	private String issueMessage;
	private boolean error;
	private String relativePath;

	/**
	 * Creates new {@link MediaIssue} instance
	 * @param sourceAbsolutePath - ID of issue source
	 * @param issueMessage - issue message
	 * @param relativePath - Relative path of issue source
	 * @param error - indicates is issue is error or warning
	 */
	public MediaIssue(String sourceAbsolutePath, String issueMessage, 
			String relativePath, boolean error){
		this.sourceAbsolutePath = sourceAbsolutePath;
		this.issueMessage = issueMessage;
		this.error = error;
		this.relativePath = relativePath;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSourceAbsolutePath() {
		return sourceAbsolutePath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIssueMessage() {
		return issueMessage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isError() {
		return error;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "MediaIssue [sourceAbsolutePath=" + sourceAbsolutePath
				+ ", issueMessage=" + issueMessage + ", error=" + error
				+ ", relativePath=" + relativePath + "]";
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (error ? 1231 : 1237);
		result = prime * result
				+ ((issueMessage == null) ? 0 : issueMessage.hashCode());
		result = prime * result
				+ ((relativePath == null) ? 0 : relativePath.hashCode());
		result = prime * result
				+ ((sourceAbsolutePath == null) ? 0 : sourceAbsolutePath.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediaIssue other = (MediaIssue) obj;
		if (error != other.error)
			return false;
		if (issueMessage == null) {
			if (other.issueMessage != null)
				return false;
		} else if (!issueMessage.equals(other.issueMessage))
			return false;
		if (relativePath == null) {
			if (other.relativePath != null)
				return false;
		} else if (!relativePath.equals(other.relativePath))
			return false;
		if (sourceAbsolutePath == null) {
			if (other.sourceAbsolutePath != null)
				return false;
		} else if (!sourceAbsolutePath.equals(other.sourceAbsolutePath))
			return false;
		return true;
	}
}
