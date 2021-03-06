package net.lkrnac.discorg.model.dal.io;

import java.io.File;
import java.io.IOException;

/**
 * Facade for directory I/O handlers. Lazy initialization of I/O handlers
 * 
 * @author sitko
 * 
 */
public class DirectoryIoFacade {
	private final File selectionDir;
	private DirectoryComparator directoryComparator;
	private HardLinksHandler hardLinksHandler;

	/**
	 * Creates directory facade instance.
	 * 
	 * @param selectionDir
	 *            selection directory to which directory IO facade belongs
	 */
	public DirectoryIoFacade(File selectionDir) {
		super();
		this.selectionDir = selectionDir;
	}

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
	 * @return result of comparison
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	public DirectoryComparisonResult compareDirectories(File fullDir, File selectionDir) throws IOException {
		return getDirectoryComparator().compareDirectories(fullDir, selectionDir);
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
	public boolean verifyHardLinks(File fullDir) throws IOException {
		return getHardLinksHandler().verifyHardLinks(fullDir);
	}

	/**
	 * Builds hard links in {@link HardLinksHandler#getSelectionDir()}. Original
	 * files are read from given full album
	 * 
	 * @param fullDir
	 *            full album from which to create hard links
	 */
	public void buildHardLinks(File fullDir) {
		this.getHardLinksHandler().buildHardLinks(fullDir, getDirectoryComparator());
	}

	/**
	 * @return directory comparator I/O handler
	 */
	private DirectoryComparator getDirectoryComparator() {
		if (directoryComparator == null) {
			directoryComparator = new DirectoryComparator();
		}
		return directoryComparator;
	}

	/**
	 * @return directory hard links I/O handler
	 */
	private HardLinksHandler getHardLinksHandler() {
		if (hardLinksHandler == null) {
			hardLinksHandler = new HardLinksHandler(selectionDir);
		}
		return hardLinksHandler;
	}
}
