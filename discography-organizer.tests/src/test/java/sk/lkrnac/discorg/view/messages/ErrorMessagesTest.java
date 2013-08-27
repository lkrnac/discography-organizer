package sk.lkrnac.discorg.view.messages;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for class {@link ErrorMessages}.
 * 
 * @author sitko
 * 
 */
public class ErrorMessagesTest {
	private static final String TEST_RESOURCE = " test resource"; //$NON-NLS-1$

	/**
	 * Unit test for {@link ErrorMessages#getErrorDialogPrefix(String)}
	 */
	@Test
	public void testErrorMessage() {
		// call testing method
		String actualMessage = ErrorMessages.getErrorDialogPrefix(TEST_RESOURCE);

		Assert.assertEquals(actualMessage, String.format(ErrorMessages.errorDialogPrefix, TEST_RESOURCE));
	}
}
