package sk.lkrnac.discorg.model.treestorage.node;

// CHECKSTYLE:OFF

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.model.cache.MediaIssue;
import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;
import sk.lkrnac.discorg.model.dal.io.DirectoryComparisonResult;

//CHECKSTYLE:ON

/**
 * Represents media node in reference storage.
 * <p>
 * It is part of composite structure
 * 
 * @author sitko
 */
public class ReferenceMediaNode extends MediaBranchNode {
	private InputMediaNode inputMirror;
	private ReferenceMediaNode fullMirror;
	private ReferenceMediaNode selectionMirror;
	private boolean fullAlbum;

	/**
	 * Creates instance of media node in reference storage.
	 * 
	 * @param parent
	 *            parent node
	 * @param file
	 *            belonging file object
	 * @param relativePath
	 *            relative path within reference storage
	 */
	public ReferenceMediaNode(TreeStorageBranchNode parent, File file, String relativePath) {
		super(parent, file, relativePath);
	}

	/**
	 * @return input mirror belonging to this reference media node
	 */
	public InputMediaNode getInputMirror() {
		return inputMirror;
	}

	/**
	 * @param inputMirror
	 *            input mirror belonging to this reference media node
	 */
	public void setInputMirror(InputMediaNode inputMirror) {
		this.inputMirror = inputMirror;
	}

	/**
	 * @return full album mirror
	 */
	public ReferenceMediaNode getFullMirror() {
		return fullMirror;
	}

	/**
	 * @param fullMirror
	 *            full album mirror
	 */
	public void setFullMirror(ReferenceMediaNode fullMirror) {
		this.fullMirror = fullMirror;
		if (!this.equals(fullMirror.getSelectionMirror())) {
			fullMirror.setSelectionMirror(this);
		}
	}

	/**
	 * @return selection album mirror
	 */
	public ReferenceMediaNode getSelectionMirror() {
		return selectionMirror;
	}

	/**
	 * @param selectionMirror
	 *            selection album mirror
	 */
	public void setSelectionMirror(ReferenceMediaNode selectionMirror) {
		this.selectionMirror = selectionMirror;
		if (!this.equals(selectionMirror.getFullMirror())) {
			selectionMirror.setFullMirror(this);
		}
	}

	/**
	 * @return flag if the media node is full album
	 */
	@Override
	public boolean isFullAlbum() {
		return fullAlbum;
	}

	/**
	 * Sets full album flag.
	 * 
	 * @param fullAlbum
	 *            full album flag
	 */
	public void setFullAlbum(boolean fullAlbum) {
		this.fullAlbum = fullAlbum;
	}

	@Override
	public Collection<String> getMirrorsAbsolutePaths() {
		Collection<String> retVal = new HashSet<String>();
		if (getFullMirror() != null) {
			retVal.add(getFullMirror().getAbsolutePath());
		}
		if (getSelectionMirror() != null) {
			retVal.add(getSelectionMirror().getAbsolutePath());
			retVal.addAll(getSelectionMirror().getMirrorsAbsolutePaths());
		}
		if (getInputMirror() != null) {
			retVal.add(getInputMirror().getAbsolutePath());
		}
		return retVal;
	}

	/**
	 * Checks if selection mirror contains hard links of this full album.
	 * <p>
	 * <b> This method expects that full mirror is initialized for this node
	 * (see {@link ReferenceMediaNode#checkSelectionForFullAlbum(String)}) </b>
	 */
	//NOPMD: Default access modifier is used because the method is used in unit test
	@SuppressWarnings("PMD.DefaultPackage")
	void checkHardLinksForSelectionMirror() {
		if (getFullMirror() != null) {
			try {
				if (!this.getDirectoryIoFacade().verifyHardLinks(getFullMirror().getFile())) {
					addMediaIssue(MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION, true, null);
					this.setNodeStatus(BranchNodeStatus.ERROR);
				}
			} catch (IOException e) {
				addMediaIssue(MediaIssueCode.REFERENCE_HARD_LINK_IO_ERROR, true, e.getLocalizedMessage());
				this.setNodeStatus(BranchNodeStatus.ERROR);
			}
		}
	}

	/**
	 * If media directory is full album, finds selection mirror for it. If this
	 * does not exist, marks it with {@link BranchNodeStatus#IGNORED}
	 * <p>
	 * <b> It expects that whole storage was loaded into meta-data maps before
	 * </b>
	 * 
	 * @param fullSubDirectory
	 *            full sub-directory name
	 * @return true - if full album has selection mirror
	 */
	//NOPMD: we need to clear selection mirror link if various selection mirrors are found
	@SuppressWarnings("PMD.NullAssignment")
	public boolean checkSelectionForFullAlbum(String fullSubDirectory) {
		if (!this.isFullAlbum()) {
			throw new IllegalArgumentException(this.getClass().getSimpleName()
					+ ".checkSelectionForFullAlbum(String) should be invoked only for full album."); //$NON-NLS-1$
		}
		ReferenceMediaNode selectionMirror = null;
		try {
			if (getSelectionMirror() == null) {
				String selectionPath = this.getRelativePath().replace(fullSubDirectory + File.separator, ""); //$NON-NLS-1$

				// get selection mirror for full album
				Collection<ReferenceMediaNode> selectionMirrors =
						getReferenceStorageCache().getReferenceItems(selectionPath);

				for (ReferenceMediaNode mirror : selectionMirrors) {
					DirectoryComparisonResult result =
							this.getDirectoryIoFacade().compareDirectories(this.getFile(), mirror.getFile());
					if (result.areMirrors()) {
						if (selectionMirror == null) {
							selectionMirror = mirror;
						} else {
							this.setNodeStatus(BranchNodeStatus.ERROR);
							addMediaIssue(MediaIssueCode.REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND, true,
									null);
							selectionMirror = null;
						}
					}
				}
				if (selectionMirror == null && !BranchNodeStatus.ERROR.equals(this.getNodeStatus())) {
					// if selection mirror wasn't found for full album -> ignored status
					this.setNodeStatus(BranchNodeStatus.IGNORED);
					addMediaIssue(MediaIssueCode.REFERENCE_MISSING_SELECTION_MIRROR, false, null);
				}
			}
		} catch (IOException e) {
			this.setNodeStatus(BranchNodeStatus.ERROR);
			addMediaIssue(MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON, true, e.getLocalizedMessage());
		}
		return selectionMirror != null;
	}

	/**
	 * Tries to find full album mirror for selection and if this is successful
	 * marks these two as a selection <--> full pair.
	 * <p>
	 * Raises warning for selection albums without full album mirror
	 * <p>
	 * It expects that whole storage was loaded into meta-data maps (see
	 * {@link ReferenceStorageCache}) before
	 * 
	 * @param fullSubDirectory
	 *            full sub-directory name
	 */
	public void checkFullAlbumForSelection(String fullSubDirectory) {
		if (this.isFullAlbum()) {
			throw new IllegalArgumentException(this.getClass().getSimpleName()
					+ ".checkFullAlbumForSelection(String) " //$NON-NLS-1$
					+ "should be called only for selection album."); //$NON-NLS-1$
		}
		if (!this.isFullAlbum()) {
			int pathEndIndex = StringUtils.lastIndexOf(this.getRelativePath(), File.separator) + 1;
			StringBuilder pathBuilder = new StringBuilder((pathEndIndex == 0) ? "" //$NON-NLS-1$
					: StringUtils.substring(this.getRelativePath(), 0, pathEndIndex));
			pathBuilder.append(fullSubDirectory).append(File.separator)
					.append(StringUtils.substring(this.getRelativePath(), pathEndIndex));

			String fullAlbumMirrorPath = pathBuilder.toString();
			ReferenceMediaNode fullMirror = compareDirectoryWithFullMirror(fullAlbumMirrorPath);
			saveFullMirror(fullSubDirectory, fullMirror);
		}
	}

	/**
	 * Sets full mirror if no error was found. If error occurred, save it into
	 * issues cache
	 * 
	 * @param fullSubDirectory
	 *            name of the full sub-directory
	 * @param fullMirror
	 *            full mirror node instance
	 */
	private void saveFullMirror(String fullSubDirectory, ReferenceMediaNode fullMirror) {
		if (!BranchNodeStatus.ERROR.equals(this.getNodeStatus())) {
			if (fullMirror == null) {
				this.setNodeStatus(BranchNodeStatus.WARNING);
				addMediaIssue(MediaIssueCode.REFERENCE_FULL_MIRROR_MISSING, false, null);
			} else {
				fullMirror.setFullAlbum(true);
				if (fullMirror.checkSelectionForFullAlbum(fullSubDirectory)) {
					this.setFullMirror(fullMirror);
					checkHardLinksForSelectionMirror();
				}
			}
		}
	}

	/**
	 * Compare selection media directory with its full mirror. Log error into
	 * issues cache if occurs.
	 * 
	 * @param fullAlbumMirrorPath
	 *            path of full media directory mirror
	 * @return full mirror node instance
	 */
	//NOPMD: we need to clear full mirror link if various reference nodes are found
	@SuppressWarnings("PMD.NullAssignment")
	private ReferenceMediaNode compareDirectoryWithFullMirror(String fullAlbumMirrorPath) {
		ReferenceMediaNode fullMirror = null;
		try {
			for (ReferenceMediaNode tmpMirror : getReferenceStorageCache().getReferenceItems(
					fullAlbumMirrorPath)) {
				DirectoryComparisonResult result =
						this.getDirectoryIoFacade().compareDirectories(tmpMirror.getFile(), this.getFile());
				if (result.areMirrors()) {
					if (fullMirror == null) {
						fullMirror = tmpMirror;
					} else {
						fullMirror = null;
						this.setNodeStatus(BranchNodeStatus.ERROR);
						addMediaIssue(MediaIssueCode.REFERENCE_VARIOUS_FULL_MIRRORS_FOUND, true, null);
					}
				}
			}
		} catch (IOException e) {
			this.setNodeStatus(BranchNodeStatus.ERROR);
			addMediaIssue(MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON, true, null);
		}
		return fullMirror;
	}

	/**
	 * Stores media issue into media issues cache.
	 * 
	 * @param mediaIssue
	 *            media issue to be added
	 */
	@Override
	protected void addMediaIssueChild(MediaIssue mediaIssue) {
		getMediaIssuesCache().addReferenceMediaIssue(mediaIssue);
	}

}
