package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;

import sk.lkrnac.discorg.constants.MediaIssueCode;
import sk.lkrnac.discorg.model.cache.MediaIssue;
import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;
import sk.lkrnac.discorg.model.dal.exception.DiscOrgDalException;
import sk.lkrnac.discorg.model.dal.io.DirectoryIoFacade;

/**
 * Represents media node in reference storage
 * <p>
 * It is part of composite structure
 * 
 * @author sitko
 */
public class ReferenceMediaNode extends MediaBranchNode {
	private InputMediaNode inputMirror;
	private ReferenceMediaNode fullMirror;
	private ReferenceMediaNode selectionMirror;
	private boolean fullAlbum = false;
	private DirectoryIoFacade directoryIoFacade;

	/**
	 * Creates instance of media node in reference storage
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
	 * Gets directory handler IO handlers facade
	 * 
	 * @return IO handlers facade for this media node
	 */
	public DirectoryIoFacade getDirectoryIoFacade() {
		if (directoryIoFacade == null) {
			directoryIoFacade = new DirectoryIoFacade(getFile());
		}
		return directoryIoFacade;
	}

	/**
	 * @return input mirror belonging to this reference media node
	 */
	public InputMediaNode getInputMirror() {
		return inputMirror;
	}

	/**
	 * Setter
	 * 
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
	 * Setter
	 * 
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
	 * Setter
	 * 
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
	public boolean isFullAlbum() {
		return fullAlbum;
	}

	/**
	 * Sets full album flag
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
	 * Checks if selection mirror contains hard links of this full album
	 * <p>
	 * <b> This method expects that full mirror is initialized for this node
	 * (see {@link ReferenceMediaNode#checkSelectionForFullAlbum(String)}) </b>
	 */
	void checkHardLinksForSelectionMirror() {
		if (getFullMirror() != null) {
			try {
				if (!this.getDirectoryIoFacade().verifyHardLinks(getFullMirror().getFile())) {
					MediaIssue mediaIssue = new MediaIssue(this.getAbsolutePath(),
							MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION,
							this.getRelativePath(), true);
					this.getMediaIssuesCache().add(mediaIssue);
					this.setNodeStatus(BranchNodeStatus.ERROR);
				}
			} catch (IOException e) {
				MediaIssue mediaIssue = new MediaIssue(this.getAbsolutePath(),
						MediaIssueCode.REFERENCE_HARD_LINK_IO_ERROR, this.getRelativePath(), true);
				mediaIssue.setErrorMessage(e.getLocalizedMessage());
				this.getMediaIssuesCache().add(mediaIssue);
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
	public boolean checkSelectionForFullAlbum(String fullSubDirectory) {
		ReferenceMediaNode selectionMirror = null;
		if (!this.isFullAlbum()) {
			throw new IllegalArgumentException(this.getClass().getSimpleName()
					+ ".checkSelectionForFullAlbum(String) should be invoked only for full album.");
		}
		try {
			if (getSelectionMirror() == null) {
				String selectionPath = this.getRelativePath().replace(
						fullSubDirectory + File.separator, "");

				// get selection mirror for full album
				Collection<ReferenceMediaNode> selectionMirrors = getReferenceStorageCache()
						.getReferenceItems(selectionPath);

				for (ReferenceMediaNode mirror : selectionMirrors) {
					if (this.getDirectoryIoFacade().compareDirectories(this.getFile(),
							mirror.getFile())) {
						if (selectionMirror != null) {
							this.setNodeStatus(BranchNodeStatus.ERROR);
							getMediaIssuesCache()
									.add(new MediaIssue(
											this.getAbsolutePath(),
											MediaIssueCode.REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND,
											this.getRelativePath(), true));
							selectionMirror = null;
						} else {
							selectionMirror = mirror;
						}
					}
				}
				if (selectionMirror == null && !BranchNodeStatus.ERROR.equals(this.getNodeStatus())) {
					// if selection mirror wasn't found for full album ->
					// ignored status
					this.setNodeStatus(BranchNodeStatus.IGNORED);
					getMediaIssuesCache().add(
							new MediaIssue(this.getAbsolutePath(),
									MediaIssueCode.REFERENCE_MISSING_SELECTION_MIRROR, this
											.getRelativePath(), false));
				}
			}
		} catch (DiscOrgDalException e) {
			this.setNodeStatus(BranchNodeStatus.ERROR);
			getMediaIssuesCache().add(
					new MediaIssue(this.getAbsolutePath(), e.getMediaIssueCode(), this
							.getRelativePath(), true));

		}
		return selectionMirror != null;
	}

	/**
	 * Tries to find full album mirror for selection and if this is successful
	 * marks these two as a selection <--> full pair
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
			throw new IllegalArgumentException(
					this.getClass().getSimpleName()
							+ ".checkFullAlbumForSelection(String) should be invoked only for selection album.");
		}
		try {
			if (!this.isFullAlbum()) {
				int pathEndIndex = StringUtils.lastIndexOf(this.getRelativePath(), File.separator) + 1;
				StringBuilder pathBuilder = new StringBuilder((pathEndIndex == 0) ? ""
						: StringUtils.substring(this.getRelativePath(), 0, pathEndIndex));
				pathBuilder.append(fullSubDirectory).append(File.separator)
						.append(StringUtils.substring(this.getRelativePath(), pathEndIndex));

				String fullAlbumMirrorPath = pathBuilder.toString();
				ReferenceMediaNode fullMirror = null;
				for (ReferenceMediaNode tmpMirror : getReferenceStorageCache().getReferenceItems(
						fullAlbumMirrorPath)) {
					if (this.getDirectoryIoFacade().compareDirectories(tmpMirror.getFile(),
							this.getFile())) {
						if (fullMirror == null) {
							fullMirror = tmpMirror;
						} else {
							fullMirror = null;
							this.setNodeStatus(BranchNodeStatus.ERROR);
							getMediaIssuesCache().add(
									new MediaIssue(this.getAbsolutePath(),
											MediaIssueCode.REFERENCE_VARIOUS_FULL_MIRRORS_FOUND,
											this.getRelativePath(), true));
						}
					}
				}
				if (!BranchNodeStatus.ERROR.equals(this.getNodeStatus())) {
					if (fullMirror == null) {
						this.setNodeStatus(BranchNodeStatus.WARNING);
						getMediaIssuesCache().add(
								new MediaIssue(this.getAbsolutePath(),
										MediaIssueCode.REFERENCE_FULL_MIRROR_MISSING, this
												.getRelativePath(), false));
					} else {
						fullMirror.setFullAlbum(true);
						if (fullMirror.checkSelectionForFullAlbum(fullSubDirectory)) {
							this.setFullMirror(fullMirror);
							checkHardLinksForSelectionMirror();
						}
					}
				}
			}
		} catch (DiscOrgDalException e) {
			this.setNodeStatus(BranchNodeStatus.ERROR);
			MediaIssue mediaIssue = new MediaIssue(this.getAbsolutePath(), e.getMediaIssueCode(),
					this.getRelativePath(), true);
			mediaIssue.setErrorMessage(e.getLocalizedMessage());
			getMediaIssuesCache().add(mediaIssue);
		}
	}
}
