package sk.lkrnac.discorg.model.dal.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Class for comparing directories.
 * 
 * @author sitko
 */
public class DirectoryComparator extends AbstractDirectoryHandler {
	private Collection<File> missingInSelectionList = null;
	private Collection<File> missingInFullList = null;

	/**
	 * Compares directories based on file names and file sizes. If media files
	 * are matched (based on file sizes and file names) in fullDir,
	 * <code>true</code> is returned.
	 * <p>
	 * It is expected that fullDir can contain more media files.
	 * 
	 * @param fullDir
	 *            directory in which are files matched
	 * @param selectionDir
	 *            all files in this directory should match files from fullDir If
	 *            the extension is <code>null</code>, all the files are compared
	 * @return <code>true</code> if all files from selectionDir match files in
	 *         fullDir (based on file size and file name)
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	public final EDirectoryComparisonResult compareDirectories(File fullDir, File selectionDir)
			throws IOException {
		EDirectoryComparisonResult result = null;

		if (fullDir != null && selectionDir != null) {
			File[] fullArray = fullDir.listFiles();
			File[] selectionArray = selectionDir.listFiles();
			if (fullArray.length == NumberUtils.INTEGER_ZERO
					|| selectionArray.length == NumberUtils.INTEGER_ZERO) {
				return EDirectoryComparisonResult.DIFFERENT_FILES;
			}
			missingInSelectionList = new ArrayList<>(Arrays.asList(fullArray));
			missingInFullList = new ArrayList<>();

			// do the comparison
			super.fileFacingLoop(Arrays.asList(selectionArray), Arrays.asList(fullArray));
			if (missingInSelectionList.isEmpty() && missingInFullList.isEmpty()) {
				result = EDirectoryComparisonResult.EQUAL;
			} else if (!missingInSelectionList.isEmpty() && !missingInFullList.isEmpty()) {
				result = EDirectoryComparisonResult.DIFFERENT_FILES;
			} else if (!missingInSelectionList.isEmpty()) {
				result = EDirectoryComparisonResult.MISSING_MEDIA_FILES_IN_SELECTION;
			} else if (!missingInFullList.isEmpty()) {
				result = EDirectoryComparisonResult.MISSING_MEDIA_FILES_IN_FULL;
			}
		}
		return result;
	}

	/**
	 * Remove selection file from unmatched list.
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected final void performActionFace(File fileInSelection, File fileInFull) throws IOException {
		missingInSelectionList.remove(fileInFull);
	}

	/**
	 * Saves missing file in selection into private cache.
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected void performActionMissingInFull(File fileInSelection) {
		missingInFullList.add(fileInSelection);
	}
}
