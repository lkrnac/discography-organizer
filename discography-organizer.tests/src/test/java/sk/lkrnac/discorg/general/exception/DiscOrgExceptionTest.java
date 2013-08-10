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
	private static final MediaIssueCode TEST_ISSUE_CODE = MediaIssueCode.REFERENCE_HARD_LINK_IO_ERROR;
	private static final String TEST_MESSAGE = "Error occured here";
	private static final String TEST_RESOURCE_PATH = "Test path";

	/**
	 * Tests population of error message and setting exception fields in
	 * {@link DiscOrgException} constructor.
	 */
	@Test
	public final void testDiscOrgException() {
		DiscOrgException testingException = new DiscOrgException(TEST_RESOURCE_PATH, TEST_ISSUE_CODE,
				new IOException(TEST_MESSAGE));

		Assert.assertEquals(testingException.getLocalizedMessage(), TEST_MESSAGE);
		Assert.assertEquals(testingException.getResourcePath(), TEST_RESOURCE_PATH);
		Assert.assertEquals(testingException.getMediaIssueCode(), TEST_ISSUE_CODE);
	}
}
