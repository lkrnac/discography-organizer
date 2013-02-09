package sk.lkrnac.discorg.model.io;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.model.DiscographyOrganizerException;

/**
 * Unit tests for {@link DirectoryHandler}
 * @author sitko
 *
 */
public class DirectoryHandlerTest {
	private static final String TEST_FILE_NAME = "test-file";

	/**
	 * Builds testing data for test 
	 * {@link DirectoryHandler#fileFacingLoop(java.util.Collection, java.util.Collection)}
	 * <p>
	 * Generates arrays of files to perform file mirrors facing and also expected matches
	 * @return data for testing
	 */
	@DataProvider
	public Object [][] testFileFacingLoop(){
		int i = 0;
		return new Object[][]{
			new Object[] {i++, generateFiles(0, 3, 1), generateFiles(0, 3, 1), new Integer[]{0,1,2,3}, new Integer[]{}},  
			new Object[] {i++, generateFiles(1, 3, 1), generateFiles(0, 3, 1), new Integer[]{1,2,3}, new Integer[]{}},
			new Object[] {i++, generateFiles(0, 3, 1), generateFiles(0, 4, 1), new Integer[]{0,1,2,3}, new Integer[]{}},
			new Object[] {i++, generateFiles(0, 4, 1), generateFiles(0, 3, 1), new Integer[]{0,1,2,3}, new Integer[]{4}},
			new Object[] {i++, generateFiles(0, 3, 1), generateFiles(1, 3, 1), new Integer[]{1,2,3}, new Integer[]{0}},
			new Object[] {i++, generateFiles(0, 4, 1), generateFiles(1, 3, 1), new Integer[]{1,2,3}, new Integer[]{0,4}},
			new Object[] {i++, generateFiles(1, 3, 1), generateFiles(0, 4, 1), new Integer[]{1,2,3}, new Integer[]{}},
			new Object[] {i++, generateFiles(0, 3, 1), generateFiles(1, 4, 1), new Integer[]{1,2,3}, new Integer[]{0}},
			new Object[] {i++, generateFiles(1, 4, 1), generateFiles(0, 3, 1), new Integer[]{1,2,3}, new Integer[]{4}},
			new Object[] {i++, generateFiles(0, 2, 1), generateFiles(3, 4, 1), new Integer[]{}, new Integer[]{0,2}},
			new Object[] {i++, generateFiles(3, 4, 1), generateFiles(0, 2, 1), new Integer[]{}, new Integer[]{3,4}},
			new Object[] {i++, generateFiles(0, 3, 1), generateFiles(0, 3, 2), new Integer[]{}, new Integer[]{0,1,2,3}},
		};
	}
	
	/**
	 * Tests {@link DirectoryHandler#fileFacingLoop(java.util.Collection, java.util.Collection)} 
	 * @param testCaseId test case id - is not used in test
	 * @param selectionMap map representing files in selection directory
	 * @param fullMap map representing files in full directory
	 * @param expectedMatches expected matches of the files' facing  
	 * @param expectedFullMissings expected missing files in reference directory
	 * @throws DiscographyOrganizerException if I/O error occurs
	 */
	@Test(dataProvider = "testFileFacingLoop")
	public void testFileFacingLoop(int testCaseId, Map<Integer,File> selectionMap, 
			Map<Integer,File> fullMap, Integer[] expectedMatches,
			Integer[] expectedFullMissings) throws DiscographyOrganizerException{
		DirectoryHandler dirHandler = new TestingDirectoryHandler();
		DirectoryHandler dirHandlerSpy = Mockito.spy(dirHandler);
		
		//call testing method
		dirHandlerSpy.fileFacingLoop(selectionMap.values(), 
				fullMap.values());
		
		for (Integer expectedMatch : expectedMatches){
			Mockito.verify(dirHandlerSpy, Mockito.times(1))
				.performActionFace(selectionMap.get(expectedMatch), 
						fullMap.get(expectedMatch));
		}
		
		for (Integer expectedFullMissing : expectedFullMissings){
			Mockito.verify(dirHandlerSpy, Mockito.times(1))
			.performActionMissingInFull(selectionMap.get(expectedFullMissing));
		}
	}
	
	/**
	 * Generates files map for testing 
	 * @param startIdx specifies start of generated files
	 * @param endIdx specifies end of generated files
	 * @param fileSize specifies file size of generated files
	 * @return generated map of files
	 */
	private Map<Integer,File> generateFiles(int startIdx, int endIdx, long fileSize){
		Map<Integer,File> generatedMap = new HashMap<Integer,File>();
		if (startIdx >= 0){
			for (int i = startIdx; i <= endIdx; i++){
				File file = new File(TEST_FILE_NAME + i);
				File fileSpy = Mockito.spy(file);
				
				Mockito.doReturn(fileSize).when(fileSpy).length();
				generatedMap.put(i, fileSpy);
			}
		}
		return generatedMap;
	}

	/**
	 * Test class needed because testing method calls abstract method.
	 * This abstract method calls are verified in the test 
	 * @author sitko
	 */
	private class TestingDirectoryHandler extends DirectoryHandler{
		@Override
		protected void performActionFace(File fileInSelection,
				File fileInFull) {
			//do nothing, will be stubbed
		}

		@Override
		protected void performActionMissingInFull(File fileInSelection) {
			//do nothing, will be stubbed
		}
	}

}
