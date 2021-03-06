package net.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.lkrnac.discorg.general.constants.MediaIssueCode;
import net.lkrnac.discorg.model.cache.MediaIssue;
import net.lkrnac.discorg.model.dal.io.DirectoryComparisonResult;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Represents media node in input storage.
 * <p>
 * It is part of composite structure
 * 
 * @author sitko
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class InputMediaNode extends MediaBranchNode {
	private final Collection<ReferenceMediaNode> referenceMirrors;

	/**
	 * Creates instance of media node in input storage.
	 * 
	 * @param parent
	 *            parent node
	 * @param file
	 *            belonging file object
	 * @param relativePath
	 *            relative path within input storage
	 */
	public InputMediaNode(TreeStorageBranchNode parent, File file, String relativePath) {
		super(parent, file, relativePath);
		referenceMirrors = new ArrayList<ReferenceMediaNode>();
	}

	/**
	 * Adds belonging mirror node in reference storage.
	 * 
	 * @param referenceMirror
	 *            mirror node to be added
	 */
	public void addReferenceMirror(ReferenceMediaNode referenceMirror) {
		referenceMirrors.add(referenceMirror);
		referenceMirror.setInputMirror(this);
	}

	/**
	 * @return iterator of reference mirrors
	 */
	public Iterator<ReferenceMediaNode> getReferenceMirrorsIterator() {
		return referenceMirrors.iterator();
	}

	@Override
	public Collection<String> getMirrorsAbsolutePaths() {
		Set<String> retVal = new HashSet<String>();
		for (Iterator<ReferenceMediaNode> i = getReferenceMirrorsIterator(); i.hasNext();) {
			ReferenceMediaNode next = i.next();
			retVal.add(next.getAbsolutePath());
			if (next.getFullMirror() != null) {
				retVal.add(next.getFullMirror().getAbsolutePath());
			}
		}
		return retVal;
	}

	/**
	 * Compares names of media files in media directory and adjusts status of
	 * the tree node.
	 * 
	 * @param mirror
	 *            reference mirror node to compare
	 */
	public void compareMediaFilesWithMirror(ReferenceMediaNode mirror) {
		try {
			DirectoryComparisonResult result =
					getDirectoryIoFacade().compareDirectories(mirror.getFile(), this.getFile());
			if (DirectoryComparisonResult.DIFFERENT_FILES.equals(result)) {
				this.setNodeStatus(BranchNodeStatus.ERROR);
				addMediaIssue(MediaIssueCode.GENERIC_DIFFERENT_NAMES, true, null);
			} else if (DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_FULL.equals(result)) {
				this.setNodeStatus(BranchNodeStatus.ERROR);
				addMediaIssue(MediaIssueCode.INPUT_MISSING_MEDIA_FILES, true, null);
			} else if (DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_SELECTION.equals(result)) {
				this.setNodeStatus(BranchNodeStatus.UPDATE);
			} else if (DirectoryComparisonResult.EQUAL.equals(result)) {
				this.setNodeStatus(BranchNodeStatus.OK);
			}
		} catch (IOException ioException) {
			addMediaIssue(MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON, true,
					ioException.getLocalizedMessage());
			this.setNodeStatus(BranchNodeStatus.ERROR);
		}
	}

	/**
	 * Raises media issue of media node on input storage is loss-less.
	 */
	public void checkLossless() {
		if (NodeStatus.LOSSLESS.equals(this.getAudioFormatType())) {
			this.setNodeStatus(BranchNodeStatus.WARNING);
			addMediaIssue(MediaIssueCode.INPUT_LOSSLESS_AUDIO_FORMAT, false, null);
		}
	}

	/**
	 * Stores media issue into media issues cache.
	 * 
	 * @param mediaIssue
	 *            media issue to be added
	 */
	@Override
	protected void addMediaIssueChild(MediaIssue mediaIssue) {
		getMediaIssuesCache().addInputMediaIssue(mediaIssue);
	}
}
