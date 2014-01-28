package net.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import net.lkrnac.discorg.general.constants.MediaIssueCode;
import net.lkrnac.discorg.model.cache.MediaIssuesCache;
import net.lkrnac.discorg.model.dal.io.DirectoryComparisonResult;
import net.lkrnac.discorg.model.dal.io.DirectoryIoFacade;
import net.lkrnac.discorg.model.interfaces.IMediaIssue;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link InputMediaNode}
 * 
 * @author lubos krnac
 * 
 */
public class InputMediaNodeTest {
	private static final int EXPECTED_ISSUE_COUNT = 1;

	private final File INPUT_DIR = new File(INPUT_DIR_PATH);

	@InjectMocks
	private InputMediaNode testingObject;

	@Spy
	private MediaIssuesCache mediaIssueCache;

	@Mock
	private DirectoryIoFacade directoryIoFacadeMock;

	/**
	 * Injects mocks into testing object
	 */
	@BeforeMethod(alwaysRun = true)
	public void initMocks() {
		testingObject = new InputMediaNode(null, INPUT_DIR, "inputDirRelativePath");
		mediaIssueCache = new MediaIssuesCache();
		MockitoAnnotations.initMocks(this);
	}

	private static final String IO_EXCEPTION_MESSAGE = "ioExceptionMessage";
	private static final String INPUT_DIR_PATH = "inputDirPath";

	//@formatter:off
	/**
	 * Data provider for test
	 * {@link InputMediaNodeTest#testCompareMediaFilesWithMirror 
	 * (DirectoryComparisonResult, boolean, BranchNodeStatus, MediaIssueCode, Boolean)}.
	 * 
	 * @return test cases
	 */
	//@formatter:on
	@DataProvider
	public final Object[][] testCompareMediaFilesWithMirror() {
		//@formatter:off
		return new Object [][]{
			new Object [] { DirectoryComparisonResult.EQUAL, false, BranchNodeStatus.OK, null, null },
			new Object [] { DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_SELECTION, 
					false, BranchNodeStatus.UPDATE, null, null },
			new Object [] { DirectoryComparisonResult.MISSING_MEDIA_FILES_IN_FULL, false, 
					BranchNodeStatus.ERROR, MediaIssueCode.INPUT_MISSING_MEDIA_FILES, true },
			new Object [] { DirectoryComparisonResult.DIFFERENT_FILES, false, 
					BranchNodeStatus.ERROR, MediaIssueCode.GENERIC_DIFFERENT_NAMES, true },
			new Object [] { null, true, 
					BranchNodeStatus.ERROR, MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON, true },
		};
		//@formatter:on
	}

	/**
	 * Unit test for method
	 * {@link InputMediaNode#compareMediaFilesWithMirror(ReferenceMediaNode)}.
	 * 
	 * @param comparisonResult
	 *            testing comparison result with reference mirror to mock
	 * @param throwIoException
	 *            flag if IOException should be thrown during the test
	 * @param expectedNodeStatus
	 *            expected node status after comparison
	 * @param expectedMediaIssue
	 *            expected media issue code after comparison
	 * @param expectedIsErrorFlag
	 *            expected media issue error flag after comparison
	 * @throws IOException
	 *             is needed because some mocked methods throws this error
	 */
	@Test(dataProvider = "testCompareMediaFilesWithMirror")
	public void testCompareMediaFilesWithMirror(DirectoryComparisonResult comparisonResult,
			boolean throwIoException, BranchNodeStatus expectedNodeStatus, MediaIssueCode expectedMediaIssue,
			Boolean expectedIsErrorFlag) throws IOException {
		//create mirror mock
		File referenceDir = new File("referenceDirPath");
		ReferenceMediaNode referenceMediaNodeMock = Mockito.mock(ReferenceMediaNode.class);
		Mockito.when(referenceMediaNodeMock.getFile()).thenReturn(referenceDir);

		//create testing object
		File inputDir = new File(INPUT_DIR_PATH);

		//mock directory IO facade
		if (throwIoException) {
			Mockito.when(directoryIoFacadeMock.compareDirectories(referenceDir, inputDir)).thenThrow(
					new IOException(IO_EXCEPTION_MESSAGE));
		} else {
			Mockito.when(directoryIoFacadeMock.compareDirectories(referenceDir, inputDir)).thenReturn(
					comparisonResult);
		}

		//call testing object
		testingObject.compareMediaFilesWithMirror(referenceMediaNodeMock);

		//verify media issue
		Mockito.verify(directoryIoFacadeMock).compareDirectories(referenceDir, inputDir);
		Collection<String> actualPathsForIssue = mediaIssueCache.getSourceAbsolutePaths(expectedMediaIssue);

		//verify number of issues
		int expectedNumberOfIssues = expectedMediaIssue == null ? 0 : 1;
		Assert.assertEquals(actualPathsForIssue.size(), expectedNumberOfIssues, "Number of issues");

		//verify issue
		if (expectedNumberOfIssues == EXPECTED_ISSUE_COUNT) {
			String issuePath = actualPathsForIssue.iterator().next();
			Assert.assertEquals(issuePath, inputDir.getAbsolutePath(), "Input directory path for issue");
			if (throwIoException) {
				IMediaIssue mediaIssue = mediaIssueCache.getIterator().next();
				Assert.assertEquals(mediaIssue.getErrorMessage(), IO_EXCEPTION_MESSAGE);
			}
		}

		//verify node status
		Assert.assertEquals(testingObject.getNodeStatus(), expectedNodeStatus, "Node status");
	}
}