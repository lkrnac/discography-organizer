package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import sk.lkrnac.discorg.context.DiscOrgContextHolder;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;
import sk.lkrnac.discorg.model.metadata.MediaIssue;
import sk.lkrnac.discorg.model.metadata.MediaIssueMessages;
import sk.lkrnac.discorg.model.metadata.MediaIssuesCollection;
import sk.lkrnac.discorg.model.metadata.StorageMetadataMaps;
import sk.lkrnac.discorg.preferences.AudioFormatsPreferencesException;


/**
 * Represents branch node containing audio media files.
 * <br>It is part of composite structure.
 * @author sitko
 */
public class MediaBranchNode extends TreeStorageBranchNode {
	private NodeStatus audioFormatType;
	private String audioFormatName;
	
	/**
	 * Creates new instance of {@link MediaBranchNode}
	 * @param parent - parent directory node
	 * @param file - file object belonging to this node
	 * @param relativePath - relative path of the file
	 */
	public MediaBranchNode(TreeStorageBranchNode parent, File file, 
			String relativePath) {
		super(parent, file, relativePath);
	}
	
	/**
	 * @return audio format type
	 */
	public NodeStatus getAudioFormatType() {
		if (audioFormatType == null){
			initializeAudioFormat();
		}
		return audioFormatType;
	}

	/**
	 * @return get audio format name
	 */
	public String getAudioFormatName() {
		if (audioFormatName == null){
			initializeAudioFormat();
		}
		return audioFormatName;
	}

	/**
	 * @return list of the media files in this directory
	 */
	public List<String> getMediaFilesNames() {
		ArrayList<String> retVal = new ArrayList<String>();
		for (File subFile : getFile().listFiles()){
			if (getFileDesignator().isMediaFile(subFile)){
				retVal.add(subFile.getName());
			}
		}
		return retVal;
	}

	/**
	 * @return List where media issues are stored
	 */
	public MediaIssuesCollection getMediaIssuesList() {
		return DiscOrgContextHolder.getInstance()
				.getContext().getBean(MediaIssuesCollection.class);
	}

	/**
	 * @return Meta-data holder object 
	 */
	public StorageMetadataMaps getStorageMetadataMaps() {
		return DiscOrgContextHolder.getInstance()
				.getContext().getBean(StorageMetadataMaps.class);
	}


	/**
	 * Initializes audio format of media directory node
	 */
	private void initializeAudioFormat(){
		if (getFile() != null){
			String previousAudioFormatString = null;
			for (File subFile : getFile().listFiles()) {
				if (subFile.isFile()) {
					String extension = FileDesignator.getExtension(subFile);
	
					// get type of format
					boolean lossyAudioFormat = getFileDesignator().isLossyMediaFile(subFile);
					boolean losslessAudioFormat = getFileDesignator().isLossLessMediaFile(subFile);
	
					// check for errors
					if (  lossyAudioFormat && losslessAudioFormat) {
						//TODO: this check should be in preferences package
						throw new AudioFormatsPreferencesException(extension);
						// save audio format
					} else if (lossyAudioFormat) {
						previousAudioFormatString = extension;
						audioFormatType = NodeStatus.LOSSY;
					} else if (losslessAudioFormat) {
						previousAudioFormatString = extension;
						audioFormatType = NodeStatus.LOSSLESS;
					} else if ((losslessAudioFormat || lossyAudioFormat) && 
							previousAudioFormatString != null && 
							!previousAudioFormatString.equals(extension)) {
						getMediaIssuesList().add(
								new MediaIssue(getAbsolutePath(), 
										MediaIssueMessages.GENERIC_VARIOUS_AUDIO_FORMAT_TYPES,
										this.getRelativePath(), true));
						break;
					}
				}
			}
	
			if (previousAudioFormatString == null)
				audioFormatType = NodeStatus.NONE;
			else
				audioFormatName = previousAudioFormatString;
			
			setNodeStatus(audioFormatType);
		}
	}

	/**
	 * Checks if media directory does not contain other media directory. If yes
	 * creates media issue.
	 */
	public void checkMediaSubDir() {
		if (this.getFile() != null
				&& this.getFile().isDirectory()) {
			// check if directory is that contains only files
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
						getMediaIssuesList().add(new MediaIssue(
										this.getAbsolutePath(),
										MediaIssueMessages.GENERIC_MEDIA_FILES_AND_SUBDIRS,
										this.getRelativePath(), true));
					}
				}
			}
		}
	}
}
