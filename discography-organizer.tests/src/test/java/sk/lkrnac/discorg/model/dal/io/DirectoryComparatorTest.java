package sk.lkrnac.discorg.model.dal.io;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.test.utils.TestUtils;

/**
 * Unit tests for {@link DirectoryComparatorTest}
 * 
 * @author sitko
 * 
 */
public class DirectoryComparatorTest {
	private static final String DIR_FULL = "full";
	private static final String DIR_SELECTION = "selection";

	/**
	 * Data provider for test
	 * {@link DirectoryComparatorTest# testCompareDirectories(String, boolean)}
	 * 
	 * @return parameters for various test runs
	 */
	@DataProvider
	public Object[][] testCompareDirectories() {
		return new Object[][] { new Object[] { "success1", true },
				new Object[] { "success2", true }, new Object[] { "fail1", false },
				new Object[] { "fail2", false }, new Object[] { "fail3", false },
				new Object[] { "fail4", false }, new Object[] { "fail5", false },
				new Object[] { "fail6", false }, new Object[] { "fail7", false }, };
	}

	/**
	 * Unit test for method
	 * {@link DirectoryComparator#compareDirectories(File, File)}
	 * <p>
	 * This test expects specific testing directory and file structure on the
	 * hard disk
	 * 
	 * @param testingDataLocation
	 *            name of the directory on HDD where are testing data stored
	 * @param expectedResult
	 *            expected status for the test
	 * @throws IOException
	 *             if I/O error occurs
	 */
	@Test(dataProvider = "testCompareDirectories")
	public void testCompareDirectories(String testingDataLocation, boolean expectedResult)
			throws IOException {
		String resourcesPath = TestUtils.getResourcesPathMethod() + File.separator
				+ testingDataLocation + File.separator;

		File fullFile = getTestingDir(resourcesPath, DIR_FULL);
		File selectionFile = getTestingDir(resourcesPath, DIR_SELECTION);

		boolean result = new DirectoryComparator().compareDirectories(fullFile, selectionFile);

		Assert.assertEquals(result, expectedResult);
	}

	/**
	 * Creates File object of testing directory and verifies its existence
	 * 
	 * @param resourcesPath
	 *            path to the testing directory
	 * @param dirName
	 *            testing directory name
	 * @return file object of testing directory
	 */
	private File getTestingDir(String resourcesPath, String dirName) {
		File file = new File(resourcesPath + dirName);
		if (!file.exists()) {
			Assert.fail("Testing directory not found: " + file.getAbsolutePath());
		}
		return file;
	}
}
