package sk.lkrnac.discorg.model.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.model.interfaces.IMediaIssue;

/**
 * Class for holding media issues meta-data.
 * 
 * @author sitko
 * 
 */
@Service
public class MediaIssuesCache {
	private Set<IMediaIssue> mediaIssues;

	/**
	 * Creates instance of the media issues list.
	 * <p>
	 * Initializes list where instances media issues will be stored
	 */
	public MediaIssuesCache() {
		mediaIssues = new HashSet<IMediaIssue>();
	}

	/**
	 * @return iterator for media issues
	 */
	public Iterator<IMediaIssue> getIterator() {
		return mediaIssues.iterator();
	}

	/**
	 * Add media issue into issues holder.
	 * 
	 * @param mediaIssue
	 *            - new media issue
	 */
	public void add(IMediaIssue mediaIssue) {
		mediaIssues.add(mediaIssue);
	}

	/**
	 * Clear all media issues.
	 */
	public void clear() {
		mediaIssues = new HashSet<IMediaIssue>();
	}

	/**
	 * Builds collection of all media issues with given code.
	 * 
	 * @param issueCode
	 *            code of media issues
	 * @return media issues collection
	 */
	public Collection<String> getSourceAbsolutePaths(MediaIssueCode issueCode) {
		Collection<String> sourceAbsolutePaths = new HashSet<String>();
		for (Iterator<IMediaIssue> i = this.getIterator(); i.hasNext();) {
			IMediaIssue mediaIssue = i.next();
			if (issueCode == null || issueCode.equals(mediaIssue.getIssueCode())) {
				sourceAbsolutePaths.add(mediaIssue.getSourceAbsolutePath());
			}
		}
		return sourceAbsolutePaths;
	}
}
