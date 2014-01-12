package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import sk.lkrnac.discorg.general.DiscOrgException;
import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.model.cache.MediaIssue;
import sk.lkrnac.discorg.model.cache.MediaIssuesCache;
import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * Represents branch node containing audio media files. <br>
 * It is part of composite structure.
 * 
 * @author sitko
 */
public class MediaBranchNode extends TreeStorageBranchNode {
	private NodeStatus audioFormatType;

	/**
	 * Creates new instance of {@link MediaBranchNode}.
	 * 
	 * @param parent
	 *            - parent directory node
	 * @param file
	 *            - file object belonging to this node
	 * @param relativePath
	 *            - relative path of the file
	 */
	public MediaBranchNode(TreeStorageBranchNode parent, File file, String relativePath) {
		super(parent, file, relativePath);
	}

	/**
	 * @return audio format type
	 */
	public NodeStatus getAudioFormatType() {
		if (audioFormatType == null) {
			initializeAudioFormat();
		}
		return audioFormatType;
	}

	/**
	 * @return list of the media files in this directory
	 */
	public List<String> getMediaFilesNames() {
		ArrayList<String> retVal = new ArrayList<String>();
		for (File subFile : getFile().listFiles()) {
			if (getFileDesignator().isMediaFile(subFile)) {
				retVal.add(subFile.getName());
			}
		}
		return retVal;
	}

	/**
	 * @return List where media issues are stored
	 */
	public MediaIssuesCache getMediaIssuesCache() {
		return getDiscOrgContextAdapter().getBean(MediaIssuesCache.class);
	}

	/**
	 * @return Meta-data holder object
	 */
	public ReferenceStorageCache getReferenceStorageCache() {
		return getDiscOrgContextAdapter().getBean(ReferenceStorageCache.class);
	}

	/**
	 * Initializes audio format of media directory node.
	 */
	private void initializeAudioFormat() {
		try {
			audioFormatType = NodeStatus.NONE;
			if (getFile() != null) {
				String previousAudioFormatString = null;
				for (File subFile : getFile().listFiles()) {
					if (subFile.isFile()) {
						previousAudioFormatString = initFormatForFileName(previousAudioFormatString, subFile);
					}
				}
				setNodeStatus(audioFormatType);
			}
		} catch (DiscOrgException exception) {
			addMediaIssue(exception.getMediaIssueCode(), true, null);
		}
	}

	/**
	 * Creates media issue and stores it in media issues cache.
	 * 
	 * @param mediaIssueCode
	 *            media issue code
	 * @param isError
	 *            flag if issue is error or warning
	 * @param message
	 *            message for media issue
	 */
	protected void addMediaIssue(MediaIssueCode mediaIssueCode, boolean isError, String message) {
		MediaIssue mediaIssue =
				new MediaIssue(this.getAbsolutePath(), mediaIssueCode, this.getRelativePath(), isError);
		mediaIssue.setErrorMessage(message);
		addMediaIssueChild(mediaIssue);
	}

	/**
	 * Method for storing media issues into media issue cache.
	 * <p>
	 * It is expected to be overridden, because media issue can be added only
	 * from within child of this class
	 * 
	 * @param mediaIssue
	 *            media issue to store in cache
	 */
	protected void addMediaIssueChild(MediaIssue mediaIssue) {
		throw new UnsupportedOperationException("This method must be overidden!"); //$NON-NLS-1$
	}

	/**
	 * Initializes audio format based on file extension.
	 * 
	 * @param previousAudioFormatString
	 *            audio format of previous processed files (can be null)
	 * @param file
	 *            file for which we are initializing audio format
	 * @return audio format string
	 * @throws DiscOrgException
	 *             if found audio format differs to previousAudioFormatString
	 *             (Directory contains various audio formats)
	 */
	private String initFormatForFileName(String previousAudioFormatString, File file) throws DiscOrgException {
		String audioFormatString = null;
		String extension = getFileDesignator().getExtension(file);

		// get type of format
		boolean lossyAudioFormat = getFileDesignator().isLossyMediaFile(file);
		boolean losslessAudioFormat = getFileDesignator().isLossLessMediaFile(file);

		if ((losslessAudioFormat || lossyAudioFormat) && previousAudioFormatString != null
				&& !previousAudioFormatString.equals(extension)) {
			audioFormatType = NodeStatus.NONE;
			throw new DiscOrgException(getAbsolutePath(), MediaIssueCode.GENERIC_VARIOUS_AUDIO_FORMAT_TYPES);
		} else if (lossyAudioFormat) {
			audioFormatString = extension;
			audioFormatType = NodeStatus.LOSSY;
		} else if (losslessAudioFormat) {
			audioFormatString = extension;
			audioFormatType = NodeStatus.LOSSLESS;
		}
		return audioFormatString;
	}

	/**
	 * Checks if media directory does not contain other media directory. If yes
	 * creates media issue.
	 */
	public void checkMediaSubDir() {
		if (this.getFile() != null && this.getFile().isDirectory()) {
			// check if directory contains only files
			boolean isLeaf = this.getFile().listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathName) {
					return pathName.isDirectory();
				}
			}).length == 0;

			// if node isn't leaf and contains some audio files
			if (!isLeaf) {
				// raise error if contains also audio files in sub-directories
				while (this.hasNextChild()) {
					ITreeStorageNode childNode = this.getNextChild();

					if (childNode instanceof MediaBranchNode) {
						this.setNodeStatus(BranchNodeStatus.ERROR);
						addMediaIssue(MediaIssueCode.GENERIC_MEDIA_FILES_AND_SUBDIRS, true, null);
					}
				}
			}
		}
	}
}
