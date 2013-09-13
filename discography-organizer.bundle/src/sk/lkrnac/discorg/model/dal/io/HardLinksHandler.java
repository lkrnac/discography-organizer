package sk.lkrnac.discorg.model.dal.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

/**
 * Generates hard links of full album media files.
 * 
 * @author sitko
 * 
 */
public class HardLinksHandler extends AbstractDirectoryHandler {
	private final File selectionDir;

	/**
	 * Creates instance of hard links generator for full directory.
	 * 
	 * @param selectionDir
	 *            selection media directory for which is instance of hard link
	 *            handler created in directories comparison
	 */
	public HardLinksHandler(File selectionDir) {
		super();
		if (selectionDir == null) {
			throw new IllegalArgumentException("selectionDir can't be null"); //$NON-NLS-1$
		}
		this.selectionDir = selectionDir;
	}

	/**
	 * @return selection directory for this hard links handler.
	 */
	public final File getSelectionDir() {
		return selectionDir;
	}

	/**
	 * Verify if all files in selection media directory are hard links of files
	 * in full directory.
	 * 
	 * @param fullDir
	 *            full media directory to compare
	 * @return true - if all selection files are hard links
	 * @throws IOException
	 *             if I/O error occurs
	 */
	@SuppressWarnings("PMD.ConfusingTernary")
	public final boolean verifyHardLinks(File fullDir) throws IOException {
		boolean result = false;
		for (File selectionFile : selectionDir.listFiles()) {
			boolean hasHardLink = false;
			for (File fullFile : fullDir.listFiles()) {
				if (getFileKey(selectionFile).equals(getFileKey(fullFile))) {
					hasHardLink = true;
					break;
				}
			}
			if (!hasHardLink) {
				result = false;
				break;
			} else {
				result |= hasHardLink;
			}
		}
		return result;
	}

	/**
	 * Reads hard link key for file.
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
	 * @return if hard link were created in selection directory
	 */
	public final boolean buildHardLinks(File fullDir, DirectoryComparator dirComparator) {
		boolean hardLinksCreated = false;
		try {
			DirectoryComparisonResult result =
					dirComparator.compareDirectories(fullDir, this.getSelectionDir());
			if (result.areMirrors()) {
				File[] fullArray = fullDir.listFiles();
				File[] selectionArray = selectionDir.listFiles();
				fileFacingLoop(Arrays.asList(selectionArray), Arrays.asList(fullArray));
				hardLinksCreated = true;
			}
		} catch (IOException ioException) {
			hardLinksCreated = false;
		}
		return hardLinksCreated;
	}

	/**
	 * Verifies if files are hard links and if not deletes file in selection
	 * directory and creates hard link copy of full file.
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected final void performActionFace(File fileInSelection, File fileInFull)
			throws IOException {
		if (!getFileKey(fileInSelection).equals(getFileKey(fileInFull))) {
			fileInSelection.delete();
			Files.createLink(Paths.get(fileInSelection.getAbsolutePath()),
					Paths.get(fileInFull.getAbsolutePath()));
		}
	}

	/**
	 * Creates hard link of selection file in full directory.
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected final void performActionMissingInFull(File fileInSelection) {
		// TODO not implemented
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}
}
