package sk.lkrnac.discorg.model.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.model.DiscographyOrganizerException;
import sk.lkrnac.discorg.test.utils.TestUtils;

/**
 * Unit test for {@link HardLinksHandler} 
 * @author sitko
 *
 */
public class HardLinksHandlerTest {
	private static final String TEST_ALBUM_PARTIAL = "test - album - partial";
	private static final String TEST_ALBUM_NOK = "test - album - nok";
	private static final String TEST_ALBUM_OK = "test - album - ok";
	private static final String DIR_NAME_FULL_ALBUM = "=[full]";
	private static final String DIR_NAME_TEMP = "temp";

	private String resourcesPath;
	private HardLinksHandler testingObj;
	private File fullDir;
	private DirectoryComparator dirComparator;
	                                 
	/**
	 * Deletes temporary directory after test
	 * @throws IOException if I/O error occurs during deletion
	 */
	@AfterMethod
	public void tidyUp() throws IOException{
		File tempDir = new File(resourcesPath + File.separator + DIR_NAME_TEMP);
		FileUtils.deleteDirectory(tempDir);
	}
	
	/**
	 * Prepares testing data for test {@link HardLinksHandlerTest#
	 * testVerifyHardLinks(String, boolean)}
	 * @return parameters for test
 	 */
	@DataProvider
	public Object [][] testVerifyHardLinks(){
		return new Object[][]{
			new Object[] { TEST_ALBUM_OK, true },
			new Object[] { TEST_ALBUM_NOK, false },
			new Object[] { TEST_ALBUM_PARTIAL, false },
		};
	}

	/**
	 * Tests {@link HardLinksHandler#verifyHardLinks(File)}
	 * <p>
	 * This test expects specific testing directory and file structure on the hard disk
	 * @param testingAlbumName testing full directory name (this should exist on HDD)
	 * @param expectedStatus expected node status
	 * @throws IOException if some I/O error occurs
	 */
	@Test(dataProvider = "testVerifyHardLinks")
	public void testVerifyHardLinks(String testingAlbumName, 
			boolean expectedStatus) throws IOException{
		resourcesPath = TestUtils.getResourcesPathMethod();
		HardLinksHandler testingObj = getSelectionTestingObject(testingAlbumName, false);
		File fullDir = getFullDir(testingAlbumName, false);
		
		//call testing method
		boolean result = testingObj.verifyHardLinks(fullDir);
		
		//verify media node status 
		Assert.assertEquals(result, expectedStatus);
	}

	/**
	 * Verifies if full directory mirror exists for selection directory 
	 * and returns file object for it
	 * @param testingFullDirName name of the testing directory
	 * @param copyOriginalToTemp if there is needed to create a copy 
	 * of the original in temporary directory
	 * @return file object belonging to the testing directory
	 * @throws IOException if I/O error occurs during copying into temporary directory
	 */
	private File getFullDir(String testingFullDirName, boolean copyOriginalToTemp) throws IOException {
		String fullAlbumRelativePath = File.separator + DIR_NAME_FULL_ALBUM +
						File.separator + testingFullDirName;
		File fullDir = new File(resourcesPath + fullAlbumRelativePath);
		if (!fullDir.exists()){
			Assert.fail("Full mirror directory for testing directory doesn't exist: " + 
					fullDir.getAbsolutePath());
		}
		
		if (copyOriginalToTemp){
			File tmpFullDir = new File(resourcesPath + File.separator + 
					DIR_NAME_TEMP + fullAlbumRelativePath);
			FileUtils.copyDirectory(fullDir, tmpFullDir);
			fullDir = tmpFullDir;
		}
		return fullDir;
	}
	
	/**
	 * Verifies if selection directory exists and returns testing object for it
	 * @param testingSelectionDirName name of the testing directory
	 * @param copyOriginalToTemp if there is needed to create a copy 
	 * of the original in temporary directory
	 * @return testing object belonging to the testing directory
	 * @throws IOException if some I/O error occurs 
	 */
	private HardLinksHandler getSelectionTestingObject(String testingSelectionDirName, 
			boolean copyOriginalToTemp) throws IOException{
		File selectionDir = new File(resourcesPath + File.separator + testingSelectionDirName);

		if (!selectionDir.exists()){
			Assert.fail("Testing directory doesn't exist: " + selectionDir.getAbsolutePath());
		}
		if (copyOriginalToTemp){
			File tmpSelectionDir = new File(resourcesPath + File.separator + 
					DIR_NAME_TEMP + File.separator + testingSelectionDirName);
			FileUtils.copyDirectory(selectionDir, tmpSelectionDir);
			selectionDir = tmpSelectionDir;
		}
		
		return new HardLinksHandler(selectionDir);
	}
	
	/**
	 * Prepares testing data for test {@link HardLinksHandlerTest#
	 * testBuildHardLinksSuccess(String)}
	 * @return parameters for test
 	 */
	@DataProvider
	public Object [][] testBuildHardLinksSuccess(){
		return new Object [][]{
			new Object [] { "test - album - success1" },	
			new Object [] { "test - album - success2" },	
			new Object [] { "test - album - success3" },	
			new Object [] { "test - album - success4" },	
		};
	}
	
	/**
	 * Tests success use case of {@link HardLinksHandler#buildHardLinks(File, DirectoryComparator)} 
	 * <p>
	 * This test expects specific testing directory and file structure on the hard disk
	 * @param testingAlbumName album name for testing
	 * @throws IOException if I/O error occurs during test
	 * @throws DiscographyOrganizerException if directory comparison fails 
	 */
	@Test(dataProvider = "testBuildHardLinksSuccess")
	public void testBuildHardLinksSuccess(String testingAlbumName) throws IOException, DiscographyOrganizerException{
		testBuildHardLinks(testingAlbumName);
		
		Assert.assertEquals(testingObj.verifyHardLinks(fullDir), true,
				"Hard links verification failed: ");
	}
	
	/**
	 * Prepares testing data for test {@link HardLinksHandlerTest#
	 * testBuildHardLinksSuccess(String)}
	 * @return parameters for test
 	 */
	@DataProvider
	public Object [][] testBuildHardLinksFail(){
		return new Object [][]{
			new Object [] { "test - album - fail1" },	
			new Object [] { "test - album - fail2" },	
		};
	}

	/**
	 * Tests failure use case of {@link HardLinksHandler#buildHardLinks(File, DirectoryComparator)} 
	 * <p>
	 * This test expects specific testing directory and file structure on the hard disk
	 * @param testingAlbumName album name for testing
	 * @throws IOException if I/O error occurs during test
	 * @throws DiscographyOrganizerException if directory comparison fails
	 * It is expected for this test case. 
	 */
	@Test(dataProvider = "testBuildHardLinksFail", 
			expectedExceptions = DiscographyOrganizerException.class)
	public void testBuildHardLinksFail(String testingAlbumName) 
			throws IOException, DiscographyOrganizerException{
		testBuildHardLinks(testingAlbumName);
	}
	
	/**
	 * Prepares parameters and environment and runs the test
	 * for method {@link HardLinksHandler#buildHardLinks(File, DirectoryComparator)}
	 * @param testingAlbumName album name for testing
	 * @throws IOException if I/O error occurs during test
	 * @throws DiscographyOrganizerException if directory comparison fails
	 */
	private void testBuildHardLinks(String testingAlbumName)
			throws IOException, DiscographyOrganizerException {
		resourcesPath = TestUtils.getResourcesPathMethod();
		testingObj = getSelectionTestingObject(testingAlbumName, true);
		fullDir = getFullDir(testingAlbumName, true);
		dirComparator = new DirectoryComparator();
		
		//call testing method
		testingObj.buildHardLinks(fullDir, dirComparator);
	}

}
class b{
	
}