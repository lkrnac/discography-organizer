package sk.lkrnac.discorg.model.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
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
	private Map<String, IMediaIssue> inputMediaIssues;
	private Map<String, IMediaIssue> referenceMediaIssues;

	/**
	 * Creates instance of the media issues list.
	 * <p>
	 * Initializes list where instances media issues will be stored
	 */
	public MediaIssuesCache() {
		inputMediaIssues = new HashMap<>();
		referenceMediaIssues = new HashMap<>();
	}

	/**
	 * @return iterator for media issues
	 */
	public Iterator<IMediaIssue> getIterator() {
		Set<IMediaIssue> issues = new HashSet<>(inputMediaIssues.size() + referenceMediaIssues.size());
		issues.addAll(referenceMediaIssues.values());
		issues.addAll(inputMediaIssues.values());
		return issues.iterator();
	}

	/**
	 * Clear all media issues.
	 */
	public void clear() {
		inputMediaIssues = new HashMap<>();
		referenceMediaIssues = new HashMap<>();
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

	/**
	 * @return Unmodifiable collection of input media storage issues
	 */
	public Collection<IMediaIssue> getInputMediaIssues() {
		return Collections.unmodifiableCollection(inputMediaIssues.values());
	}

	/**
	 * Stores input media storage issue into cache.
	 * 
	 * @param mediaIssue
	 *            media issue instance
	 */
	public void addInputMediaIssue(IMediaIssue mediaIssue) {
		inputMediaIssues.put(mediaIssue.getSourceAbsolutePath(), mediaIssue);
	}

	/**
	 * Stores reference media storage issue into cache.
	 * 
	 * @param mediaIssue
	 *            media issue instance
	 */
	public void addReferenceMediaIssue(IMediaIssue mediaIssue) {
		referenceMediaIssues.put(mediaIssue.getSourceAbsolutePath(), mediaIssue);
	}
}
