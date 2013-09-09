package sk.lkrnac.discorg.model.dal.io;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for logic in enumeration {@link EDirectoryComparisonResult}
 * 
 * @author sitko
 * 
 */
public class EDirectoryComparisonResultTest {
	/**
	 * Data provider for test
	 * {@link EDirectoryComparisonResultTest#testAreMirrors(EDirectoryComparisonResult, boolean)}
	 * .
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testAreMirrors() {
		//@formatter:off
		return new Object [][]{
			new Object [] { EDirectoryComparisonResult.EQUAL, true },
			new Object [] { EDirectoryComparisonResult.MISSING_MEDIA_FILES_IN_SELECTION, true },
			new Object [] { EDirectoryComparisonResult.MISSING_MEDIA_FILES_IN_FULL, false },
			new Object [] { EDirectoryComparisonResult.DIFFERENT_FILES, false },
		};
		//@formatter:on
	}

	/**
	 * Unit test for method {@link EDirectoryComparisonResult#areMirrors()}
	 * 
	 * @param testingResult
	 *            testing enumeration value
	 * @param expectedResult
	 *            expected result from method
	 *            {@link EDirectoryComparisonResult#areMirrors()}
	 */
	@Test(dataProvider = "testAreMirrors")
	public static void testAreMirrors(EDirectoryComparisonResult testingResult, boolean expectedResult) {
		Assert.assertEquals(testingResult.areMirrors(), expectedResult);
	}
}
