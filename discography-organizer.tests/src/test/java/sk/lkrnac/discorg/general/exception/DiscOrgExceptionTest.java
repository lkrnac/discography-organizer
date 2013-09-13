package sk.lkrnac.discorg.general.exception;

import java.io.IOException;

import org.testng.Assert;
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
	private static final MediaIssueCode TEST_ISSUE_CODE = MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON;
	private static final String TEST_MESSAGE = "Error occured here";
	private static final String TEST_RESOURCE_PATH = "Test path";

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
	 * Tests population of error code and setting exception fields in
	 * {@link DiscOrgException} constructor.
	 * 
	 */
	@Test
	public final void testDiscOrgExceptionWithMediaIssueCode() {
		DiscOrgException testingException = new DiscOrgException(TEST_RESOURCE_PATH, TEST_ISSUE_CODE);

		Assert.assertEquals(testingException.getLocalizedMessage(), null);
		Assert.assertEquals(testingException.getMessage(), null);
		Assert.assertEquals(testingException.getResourcePath(), TEST_RESOURCE_PATH);
		Assert.assertEquals(testingException.getMediaIssueCode(), TEST_ISSUE_CODE);
	}

}
