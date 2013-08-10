package sk.lkrnac.discorg.view.messages;

import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
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
	 * Data provider for test
	 * {@link ErrorMessagesTest#testErrorMessage(boolean, String)}.
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testErrorMessage() {
		// @formatter:off
		return new Object[][] { 
			new Object[] { true, "errorMessage" }, //$NON-NLS-1$
			new Object[] { false, "unexpectedErrorMessage" }, //$NON-NLS-1$
		};
		// @formatter:on
	}

	/**
	 * Unit test for {@link ErrorMessages#getErrorMessage(boolean, String)}
	 * 
	 * @param errorIsExpected
	 *            flag indicating if the error is application bug or application
	 *            specific known error
	 * @param errorMessageField
	 *            translated message field that is expected to be used in result
	 *            message
	 */
	@Test(dataProvider = "testErrorMessage")
	public void testErrorMessage(boolean errorIsExpected, String errorMessageField) {
		String expectedMessage = Whitebox.getInternalState(ErrorMessages.class, errorMessageField);

		// call testing method
		String actualMessage = ErrorMessages.getErrorMessage(errorIsExpected, TEST_RESOURCE);

		Assert.assertEquals(actualMessage, String.format(expectedMessage, TEST_RESOURCE));
	}
}
