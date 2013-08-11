package sk.lkrnac.discorg.view;

import org.eclipse.ui.PlatformUI;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.DataProvider;

import sk.lkrnac.discorg.general.DiscOrgException;
import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.view.messages.ErrorMessages;
import sk.lkrnac.discorg.view.messages.MediaIssueMessages;

/**
 * Unit test for {@link ErrorVisualizer}.
 * 
 * @author sitko
 * 
 */
public class ErrorVisualizerTest {
	private static final String COLON = ": ";
	private static final MediaIssueCode TEST_ISSUE_CODE = MediaIssueCode.INPUT_DIFFERENT_NAMES;
	private static final String TEST_RESOURCE_PATH = "Test Path";

	/**
	 * Data provider for test
	 * {@link ErrorVisualizerTest#testVisualizeError(DiscOrgException, String)}.
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testVisualizeError() {
		//@formatter:off
		return new Object [][]{
			new Object [] { new DiscOrgException(TEST_RESOURCE_PATH, TEST_ISSUE_CODE),
					ErrorMessages.getErrorMessage(true, TEST_RESOURCE_PATH) + COLON +
					MediaIssueMessages.getMessageForMessageCode(TEST_ISSUE_CODE) },
		};
		//@formatter:on
	}

	/**
	 * Unit test for {@link ErrorMessages#getErrorMessage(boolean, String)}
	 * 
	 * @param testingError
	 *            testing error
	 * @param expectedMessage
	 *            expected error message
	 */
	// @Test
	public void testVisualizeError(DiscOrgException testingError, String expectedMessage) {
		PowerMockito.mockStatic(PlatformUI.class);
	}
}
