package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sk.lkrnac.discorg.model.cache.MediaIssue;
import sk.lkrnac.discorg.model.dal.messages.MediaIssueMessages;

/**
 * Represents media node in input storage
 * <p>
 * It is part of composite structure
 * 
 * @author sitko
 * 
 */
public class InputMediaNode extends MediaBranchNode {
	private Collection<ReferenceMediaNode> referenceMirrors;

	/**
	 * Creates instance of media node in input storage
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
	 * Adds belonging mirror node in reference storage
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
	 * the tree node
	 * 
	 * @param mirror
	 *            reference mirror node to compare
	 */
	public void compareMediaFilesWithMirror(ReferenceMediaNode mirror) {
		if (mirror != null) {
			List<String> mediaFilesNames = mirror.getMediaFilesNames();
			int difference = mediaFilesNames.size() - this.getMediaFilesNames().size();

			if (difference < 0) {
				getMediaIssuesList().add(
						new MediaIssue(getAbsolutePath(),
								MediaIssueMessages.INPUT_MISSING_MEDIA_FILES, this
										.getRelativePath(), true));
				this.setNodeStatus(BranchNodeStatus.ERROR);
			} else {
				if (difference > 0) {
					this.setNodeStatus(BranchNodeStatus.UPDATE);
					getStorageMetadataMaps().putItemForUpdate(this);
				}
				for (String mediaFile : this.getMediaFilesNames()) {
					mediaFilesNames.remove(mediaFile);
				}
				if (mediaFilesNames.size() != difference) {
					getMediaIssuesList().add(
							new MediaIssue(this.getAbsolutePath(),
									MediaIssueMessages.INPUT_DIFFERENT_NAMES, this
											.getRelativePath(), false));
				}
			}
		}
	}

	/**
	 * Raises media issue of media node on input storage is loss-less
	 */
	public void checkLossless() {
		if (NodeStatus.LOSSLESS.equals(this.getAudioFormatType())) {
			this.setNodeStatus(BranchNodeStatus.WARNING);

			getMediaIssuesList().add(
					new MediaIssue(this.getAbsolutePath(),
							MediaIssueMessages.INPUT_LOSSLESS_AUDIO_FORMAT, this.getRelativePath(),
							false));
		}
	}

}
