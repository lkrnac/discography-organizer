package sk.lkrnac.discorg.model.dal.exception;

import java.io.IOException;

import junit.framework.Assert;

import org.testng.annotations.Test;

import sk.lkrnac.discorg.constants.MediaIssueCode;

/**
 * Unit test for {@link DiscOrgDalException}
 * 
 * @author sitko
 * 
 */
public class DiscOrgDalExceptionTest {
	private static final String TEST_MESSAGE = "Error occured here";

	/**
	 * Tests population of error message in {@link DiscOrgDalException}
	 * constructor
	 */
	@Test
	public void testDiscOrgDalException() {
		DiscOrgDalException testingException = new DiscOrgDalException(
				MediaIssueCode.REFERENCE_HARD_LINK_IO_ERROR, new IOException(TEST_MESSAGE));

		Assert.assertEquals(TEST_MESSAGE, testingException.getLocalizedMessage());
	}
}
