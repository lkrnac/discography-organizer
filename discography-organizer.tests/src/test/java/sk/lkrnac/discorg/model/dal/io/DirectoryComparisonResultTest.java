package sk.lkrnac.discorg.model.dal.io;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for logic in enumeration {@link DirectoryComparisonResult}
 * 
 * @author sitko
 * 
 */
public class DirectoryComparisonResultTest {
	/**
	 * Data provider for test
	 * {@link DirectoryComparisonResultTest#testAreMirrors(DirectoryComparisonResult, boolean)}
	 * .
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testAreMirrors() {
		//@formatter:off
		return new Object [][]{
			new Object [] { DirectoryComparisonResult.EQUAL, true },
			new Object [] { DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_SELECTION, true },
			new Object [] { DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_FULL, false },
			new Object [] { DirectoryComparisonResult.DIFFERENT_FILES, false },
		};
		//@formatter:on
	}

	/**
	 * Unit test for method {@link DirectoryComparisonResult#areMirrors()}
	 * 
	 * @param testingResult
	 *            testing enumeration value
	 * @param expectedResult
	 *            expected result from method
	 *            {@link DirectoryComparisonResult#areMirrors()}
	 */
	@Test(dataProvider = "testAreMirrors")
	public static void testAreMirrors(DirectoryComparisonResult testingResult, boolean expectedResult) {
		Assert.assertEquals(testingResult.areMirrors(), expectedResult);
	}
}
