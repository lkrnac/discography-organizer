package sk.lkrnac.discorg.view;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.general.DiscOrgException;
import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.general.context.WorkbenchEnvironmentFacade;
import sk.lkrnac.discorg.view.messages.ErrorMessages;
import sk.lkrnac.discorg.view.messages.MediaIssueMessages;

/**
 * Unit test for {@link ErrorVisualizer}.
 * 
 * @author sitko
 * 
 */
public class ErrorVisualizerTest {
	private static final MediaIssueCode TEST_ISSUE_CODE = MediaIssueCode.INPUT_DIFFERENT_NAMES;
	private static final String TEST_RESOURCE_PATH = "Test Path";
	private static final String ERROR_MESSAGE = "Test error message";

	@InjectMocks
	private ErrorVisualizer testingObject;

	@Mock
	private WorkbenchEnvironmentFacade workbenchEnvironment;

	/**
	 * Inject mocks into testing object
	 */
	@BeforeClass(alwaysRun = true)
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

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
					ErrorMessages.getErrorDialogPrefix(TEST_RESOURCE_PATH) +
					MediaIssueMessages.getMessageForMessageCode(TEST_ISSUE_CODE) },
			new Object [] { new DiscOrgException(TEST_RESOURCE_PATH, new Exception(ERROR_MESSAGE)),
					ErrorMessages.getErrorDialogPrefix(TEST_RESOURCE_PATH) + ERROR_MESSAGE },
		};
		//@formatter:on
	}

	/**
	 * Unit test for {@link ErrorMessages#getErrorDialogPrefix(String)}
	 * 
	 * @param testingError
	 *            testing error
	 * @param expectedMessage
	 *            expected error message
	 */
	//verification phase of test id done via Mockito verification 
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	@Test(dataProvider = "testVisualizeError")
	public void testVisualizeError(DiscOrgException testingError, String expectedMessage) {
		//call testing method
		testingObject.visualizeError(testingError);

		Mockito.verify(workbenchEnvironment).openErrorDialog(ErrorMessages.errorDialogTitle, expectedMessage);
	}
}
