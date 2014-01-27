package net.lkrnac.discorg.model.dal.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstract class containing common logic for directory handlers.
 * 
 * @author sitko
 * 
 */
public abstract class AbstractDirectoryHandler {
	/**
	 * Factory method that runs through selection array. For each selection item
	 * finds belonging full mirror and performs action. This action is
	 * implemented in child classes.
	 * 
	 * @param fullDirFiles
	 *            collection of files in full directory
	 * @param selectionDirFiles
	 *            collection of files in selection directory
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	protected final void fileFacingLoop(Collection<File> selectionDirFiles, Collection<File> fullDirFiles)
			throws IOException {
		Collection<File> missingInFullCollection = new ArrayList<>(selectionDirFiles);

		// do the comparison
		for (File fileInSelection : selectionDirFiles) {
			for (File fileInFull : fullDirFiles) {
				if (fileInSelection.getName().equals(fileInFull.getName())
						&& fileInSelection.length() == fileInFull.length()) {
					performActionFace(fileInSelection, fileInFull);
					missingInFullCollection.remove(fileInSelection);
					break;
				}
			}
		}
		for (File missingInFull : missingInFullCollection) {
			performActionMissingInFull(missingInFull);
		}
	}

	/**
	 * Performs action for files that are matched in selection and full
	 * directory.
	 * 
	 * @param fileInSelection
	 *            file in selection directory
	 * @param fileInFull
	 *            file in full directory
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	protected abstract void performActionFace(File fileInSelection, File fileInFull) throws IOException;

	/**
	 * Performs action for files that are missing in full directory.
	 * 
	 * @param fileInSelection
	 *            file in selection directory
	 */
	protected abstract void performActionMissingInFull(File fileInSelection);
}
