package sk.lkrnac.discorg.general.exception;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.general.DiscOrgException;
import sk.lkrnac.discorg.general.constants.MediaIssueCode;

/**
 * Unit test for {@link DiscOrgException}.
 * 
 * @author sitko
 * 
 */
public class DiscOrgExceptionTest {
	private static final MediaIssueCode TEST_ISSUE_CODE = MediaIssueCode.REFERENCE_HARD_LINK_IO_ERROR;
	private static final String TEST_MESSAGE = "Error occured here";
	private static final String TEST_RESOURCE_PATH = "Test path";
	private static final String TEST_MESSAGE_PARAMETER = "Test message parameter";

	/**
	 * Tests population of error message and setting exception fields in
	 * {@link DiscOrgException#DiscOrgException(String, Throwable)} constructor.
	 */
	@Test
	public final void testDiscOrgExceptionWithCause() {
		DiscOrgException testingException = new DiscOrgException(TEST_RESOURCE_PATH, new IOException(
				TEST_MESSAGE));

		Assert.assertEquals(testingException.getLocalizedMessage(), TEST_MESSAGE);
		Assert.assertEquals(testingException.getMessage(), TEST_MESSAGE);
		Assert.assertEquals(testingException.getResourcePath(), TEST_RESOURCE_PATH);
	}

	/**
	 * Data provider for test
	 * {@link DiscOrgExceptionTest#testDiscOrgExceptionWithMediaIssueCode}.
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testDiscOrgExceptionWithMediaIssueCode() {
		//@formatter:off
		return new Object [][]{
			new Object [] { null, new String [] {} },
			new Object [] { new String [] {} , new String [] {} },
			new Object [] { new String [] { TEST_MESSAGE_PARAMETER } , new String [] { TEST_MESSAGE_PARAMETER } },
		};
		//@formatter:on
	}

	/**
	 * Tests population of error code and setting exception fields in
	 * {@link DiscOrgException} constructor.
	 * 
	 * @param testingMessageParameters
	 *            testing message parameters
	 * @param expectedMessageParameters
	 *            expected message parameters
	 */
	@Test(dataProvider = "testDiscOrgExceptionWithMediaIssueCode")
	public final void testDiscOrgExceptionWithMediaIssueCode(String[] testingMessageParameters,
			String[] expectedMessageParameters) {
		DiscOrgException testingException = new DiscOrgException(TEST_RESOURCE_PATH, TEST_ISSUE_CODE);
		if (testingMessageParameters != null) {
			testingException.setMessageParameters(testingMessageParameters);
		}

		Assert.assertEquals(testingException.getLocalizedMessage(), null);
		Assert.assertEquals(testingException.getMessage(), null);
		Assert.assertEquals(testingException.getResourcePath(), TEST_RESOURCE_PATH);
		Assert.assertEquals(testingException.getMediaIssueCode(), TEST_ISSUE_CODE);
		Assert.assertEquals(testingException.getMessageParameters(), expectedMessageParameters);
	}

}
