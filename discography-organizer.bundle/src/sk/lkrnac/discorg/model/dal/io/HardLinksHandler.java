package sk.lkrnac.discorg.model.dal.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import sk.lkrnac.discorg.general.DiscOrgException;
import sk.lkrnac.discorg.general.constants.MediaIssueCode;

/**
 * Generates hard links of full album media files
 * 
 * @author sitko
 * 
 */
public class HardLinksHandler extends DirectoryHandler {
	private File selectionDir;

	/**
	 * Creates instance of hard links generator for full directory
	 * 
	 * @param selectionDir
	 *            selection media directory for which is instance of hard link
	 *            handler created in directories comparison
	 */
	public HardLinksHandler(File selectionDir) {
		if (selectionDir == null) {
			throw new IllegalArgumentException("selectionDir can't be null");
		}
		this.selectionDir = selectionDir;
	}

	/**
	 * @return selection directory for this hard links handler
	 */
	public File getSelectionDir() {
		return selectionDir;
	}

	/**
	 * Verify if all files in selection media directory are hard links of files
	 * in full directory
	 * 
	 * @param fullDir
	 *            full media directory to compare
	 * @return true - if all selection files are hard links
	 * @throws IOException
	 *             if I/O error occurs
	 */
	public boolean verifyHardLinks(File fullDir) throws IOException {
		boolean result = false;
		for (File selectionFile : selectionDir.listFiles()) {
			boolean hasHardLink = false;
			for (File fullFile : fullDir.listFiles()) {
				if (getFileKey(selectionFile).equals(getFileKey(fullFile))) {
					hasHardLink = true;
					break;
				}
			}
			if (hasHardLink == false) {
				result = false;
				break;
			} else {
				result |= hasHardLink;
			}
		}
		return result;
	}

	/**
	 * Reads hard link key for file
	 * 
	 * @param file
	 *            file on disk
	 * @return hard link key value
	 * @throws IOException
	 *             if I/O error occurs
	 */
	private static Object getFileKey(File file) throws IOException {
		Path filePath = FileSystems.getDefault().getPath(file.getAbsolutePath());
		BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
		return attrs.fileKey();
	}

	/**
	 * Builds hard links in {@link HardLinksHandler#getSelectionDir()}. Original
	 * files are read from given full album
	 * 
	 * @param fullDir
	 *            full album from which to create hard links
	 * @param dirComparator
	 *            comparator of media directories
	 * @throws IOException
	 *             if there are more media files in selection mirror than in
	 *             full album or if I/O error occurs
	 * @throws DiscOrgException
	 *             if full media directory contains less files than selection
	 *             mirror
	 */
	public void buildHardLinks(File fullDir, DirectoryComparator dirComparator) throws IOException,
			DiscOrgException {
		if (dirComparator.compareDirectories(fullDir, this.getSelectionDir())) {
			File[] fullArray = fullDir.listFiles();
			File[] selectionArray = selectionDir.listFiles();
			fileFacingLoop(Arrays.asList(selectionArray), Arrays.asList(fullArray));
		} else {
			throw new DiscOrgException(selectionDir.getAbsolutePath(),
					MediaIssueCode.GENERIC_MORE_FILES_IN_SELECTION);
		}
	}

	/**
	 * Verifies if files are hard links and if not deletes file in selection
	 * directory and creates hard link copy of full file
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected void performActionFace(File fileInSelection, File fileInFull) throws IOException {
		if (!getFileKey(fileInSelection).equals(getFileKey(fileInFull))) {
			fileInSelection.delete();
			Files.createLink(Paths.get(fileInSelection.getAbsolutePath()),
					Paths.get(fileInFull.getAbsolutePath()));
		}
	}

	/**
	 * Creates hard link of selection file in full directory
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected void performActionMissingInFull(File fileInSelection) {
		// TODO not implemented
		throw new UnsupportedOperationException("not implemented");
	}
}
