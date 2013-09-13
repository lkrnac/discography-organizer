package sk.lkrnac.discorg.model.dal.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.test.utils.TestUtils;

/**
 * Unit test for {@link HardLinksHandler}
 * 
 * @author sitko
 * 
 */
public class HardLinksHandlerTest {
	private static final String TEST_ALBUM = "test - album";
	private static final String DIR_NAME_FULL_ALBUM = "=[full]";
	private static final String DIR_NAME_TEMP = "temp";

	private String resourcesPath;
	private HardLinksHandler testingObj;
	private File fullDir;

	/**
	 * Indicates type copy of testing data into testing temporary directory
	 * 
	 * @author sitko
	 */
	private enum ECopyIntoTempType {
		COPY_NORMAL, // normal copy of all files
		COPY_SOME_HARD_LINKS, // copy files, some normally, some as hard links
		COPY_ALL_HARD_LINKS // make hard links of all files
	}

	/**
	 * Deletes temporary directory after test
	 * 
	 * @throws IOException
	 *             if I/O error occurs during deletion
	 */
	@AfterMethod
	public void tidyUp() throws IOException {
		File tempDir = new File(resourcesPath + File.separator + DIR_NAME_TEMP);
		FileUtils.deleteDirectory(tempDir);
	}

	/**
	 * Prepares testing data for test
	 * {@link HardLinksHandlerTest# testVerifyHardLinks(ECopyIntoTempType, boolean)}
	 * 
	 * @return parameters for test
	 */
	@DataProvider
	public Object[][] testVerifyHardLinks() {
		return new Object[][] { new Object[] { ECopyIntoTempType.COPY_ALL_HARD_LINKS, true },
				new Object[] { ECopyIntoTempType.COPY_SOME_HARD_LINKS, false },
				new Object[] { ECopyIntoTempType.COPY_NORMAL, false }, };
	}

	/**
	 * Tests {@link HardLinksHandler#verifyHardLinks(File)}
	 * <p>
	 * This test expects specific testing directory and file structure on the
	 * hard disk
	 * 
	 * @param copyType
	 *            type of the copying of testing data
	 * @param expectedStatus
	 *            expected node status
	 * @throws IOException
	 *             if some I/O error occurs
	 */
	@Test(dataProvider = "testVerifyHardLinks")
	public void testVerifyHardLinks(ECopyIntoTempType copyType, boolean expectedStatus) throws IOException {
		resourcesPath = TestUtils.getResourcesPathMethod();
		File selectionDir = getTestingDir(TEST_ALBUM, false, false);
		HardLinksHandler testingObj = new HardLinksHandler(selectionDir);
		File fullDir = getTestingDir(TEST_ALBUM, true, true);

		copyDirForTesting(fullDir, selectionDir, copyType);

		// call testing method
		boolean result = testingObj.verifyHardLinks(fullDir);

		// verify media node status
		Assert.assertEquals(result, expectedStatus);
	}

	/**
	 * Verifies if directory exists and returns file object for it. If needed
	 * copy of directory is created
	 * 
	 * @param testingDirName
	 *            name of the testing directory
	 * @param isFull
	 *            flag indicating full or selection directory
	 * @param copyToTemp
	 *            copy of directory into temporary is needed of the original in
	 *            temporary directory
	 * @return file object belonging to the testing directory
	 * @throws IOException
	 *             if I/O error occurs during copying into temporary directory
	 */
	private File getTestingDir(String testingDirName, boolean isFull, boolean copyToTemp) throws IOException {
		String albumRelativePath = (isFull ? File.separator + DIR_NAME_FULL_ALBUM : "") + File.separator
				+ testingDirName;
		File testDir = new File(resourcesPath + albumRelativePath);
		File tmpTestDir = new File(resourcesPath + File.separator + DIR_NAME_TEMP + albumRelativePath);

		if (copyToTemp) {
			if (!testDir.exists()) {
				Assert.fail("Directory for testing doesn't exist: " + testDir.getAbsolutePath());
			}
			FileUtils.copyDirectory(testDir, tmpTestDir);
		}
		testDir = tmpTestDir;
		return testDir;
	}

	/**
	 * Creates hard links of files from given source directory into destination
	 * directory. Ignores sub-directories.
	 * 
	 * @param sourceDir
	 *            source directory of the hard links
	 * @param destinationDir
	 *            destination directory of hard links
	 * @param copyType
	 *            type of copying
	 * @throws IOException
	 *             if I/O error occurs
	 */
	private void copyDirForTesting(File sourceDir, File destinationDir, ECopyIntoTempType copyType)
			throws IOException {
		if (!destinationDir.exists()) {
			destinationDir.mkdirs();
		}
		if (ECopyIntoTempType.COPY_NORMAL.equals(copyType)) {
			FileUtils.copyDirectory(sourceDir, destinationDir);
		} else {
			int idx = 0;
			for (File file : sourceDir.listFiles()) {
				Path destinationPath = Paths.get(destinationDir.getAbsolutePath() + File.separator
						+ file.getName());
				if (ECopyIntoTempType.COPY_SOME_HARD_LINKS.equals(copyType) && idx++ % 2 == 0) {
					Files.copy(file.toPath(), destinationPath);
				} else {
					Files.createLink(destinationPath, file.toPath());
				}
			}
		}
	}

	/**
	 * Prepares testing data for test
	 * {@link HardLinksHandlerTest# testBuildHardLinks(String, boolean)}
	 * 
	 * @return parameters for test
	 */
	@DataProvider
	public Object[][] testBuildHardLinks() {
		// @formatter:off
		return new Object[][] { 
				new Object[] { "test - album - success1", true },
				new Object[] { "test - album - success2", true }, 
				new Object[] { "test - album - success3", true },
				new Object[] { "test - album - success4", true },
				new Object[] { "test - album - fail1", false },
				new Object[] { "test - album - fail2", false },
		};
		// @formatter:on
	}

	/**
	 * Tests success use case of
	 * {@link HardLinksHandler#buildHardLinks(File, DirectoryComparator)}
	 * <p>
	 * This test expects specific testing directory and file structure on the
	 * hard disk
	 * 
	 * @param testingAlbumName
	 *            album name for testing
	 * @param expectedResult
	 *            expected result of testing method
	 * @throws IOException
	 *             if I/O error occurs during test
	 */
	@Test(dataProvider = "testBuildHardLinks")
	public void testBuildHardLinks(String testingAlbumName, boolean expectedResult) throws IOException {
		resourcesPath = TestUtils.getResourcesPathMethod();
		File selectionDir = getTestingDir(testingAlbumName, false, true);
		testingObj = new HardLinksHandler(selectionDir);
		fullDir = getTestingDir(testingAlbumName, true, true);
		DirectoryComparator dirComparator = new DirectoryComparator();

		// call testing method
		boolean actualReault = testingObj.buildHardLinks(fullDir, dirComparator);

		Assert.assertEquals(actualReault, expectedResult);
		Assert.assertEquals(testingObj.verifyHardLinks(fullDir), expectedResult,
				"Hard links verification failed: ");
	}

}