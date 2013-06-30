package sk.lkrnac.discorg.model.dal.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.lkrnac.discorg.model.dal.exception.DiscOrgDalException;

/**
 * Class for comparing directories
 * 
 * @author sitko
 */
public class DirectoryComparator extends DirectoryHandler {
	private List<File> unmatchedList = null;

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
	 * @throws DiscOrgDalException
	 *             if some I/O or application error occurs
	 */
	public boolean compareDirectories(File fullDir, File selectionDir) throws DiscOrgDalException {
		boolean result = false;

		if (fullDir != null && selectionDir != null) {
			File[] fullArray = fullDir.listFiles();
			File[] selectionArray = selectionDir.listFiles();
			unmatchedList = new ArrayList<File>(selectionArray.length);
			boolean found = false;
			for (File fileInSelection : selectionArray) {
				unmatchedList.add(fileInSelection);
				found = true;
			}

			// do the comparison
			super.fileFacingLoop(Arrays.asList(selectionArray), Arrays.asList(fullArray));
			if (found) {
				result = unmatchedList.size() == 0;
			}
		}
		return result;
	}

	/**
	 * Remove selection file from unmatched list
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected void performActionFace(File fileInSelection, File fileInFull)
			throws DiscOrgDalException {
		unmatchedList.remove(fileInSelection);
	}

	/**
	 * Ignores selection files missing in full directory
	 * <p>
	 * <b> Javadoc from parent class:<br>
	 * </b> {@inheritDoc}
	 */
	@Override
	protected void performActionMissingInFull(File fileInSelection) {
		// ignore
	}
}
