package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.util.Locale;

import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.general.context.WorkbenchEnvironmentFacade;
import sk.lkrnac.discorg.model.cache.MediaIssuesCache;
import sk.lkrnac.discorg.model.interfaces.IMediaIssue;
import sk.lkrnac.discorg.model.preferences.AudioFormatsPreferences;
import sk.lkrnac.discorg.test.utils.TestUtils;

/**
 * Unit tests for class {@link MediaBranchNode}
 * 
 * @author lubos krnac
 * 
 */
public class MediaBranchNodeTest {
	private static final String LOSSY_FORMAT = "mp3";
	private static final String LOSSLESS_FORMAT = "flac";

	//@formatter:off
	private static String CASE_01_EMPTY_DIR                    = "01-empty-dir";
	private static String CASE_02_LOSSY_FILE                   = "02-lossy-file";
	private static String CASE_03_LOSSLESS_FILE                = "03-lossless-file";
	private static String CASE_04_UNKNOWN_FILE                 = "04-unknown-file";
	private static String CASE_05_LOSSY_FILES                  = "05-lossy-files";
	private static String CASE_06_LOSSLESS_FILES               = "06-lossless-files";
	private static String CASE_07_UNKNOWN_FILES                = "07-unknown-files";
	private static String CASE_08_LOSSY_UNKNOWN_FILES          = "08-lossy-unknown-files";
	private static String CASE_09_LOSSLESS_UNKNOWN_FILES       = "09-lossless-unknown-files";
	private static String CASE_10_LOSSY_LOSSLESS_FILES         = "10-lossy-lossless-files";
	private static String CASE_11_LOSSY_LOSSLESS_UNKNOWN_FILES = "11-lossy-lossless-unknown-files";
	//@formatter:on

	/**
	 * Data provider for test
	 * {@link MediaBranchNodeTest#testGetAudioFormatTypeNotInitialized(NodeStatus, String, MediaIssueCode)}
	 * .
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testGetAudioFormatTypeNotInitialized() {
		//@formatter:off
		return new Object [][]{
			new Object [] { NodeStatus.NONE    , CASE_01_EMPTY_DIR                    , null },
			new Object [] { NodeStatus.LOSSY   , CASE_02_LOSSY_FILE                   , null },
			new Object [] { NodeStatus.LOSSLESS, CASE_03_LOSSLESS_FILE                , null },
			new Object [] { NodeStatus.NONE    , CASE_04_UNKNOWN_FILE                 , null },
			new Object [] { NodeStatus.LOSSY   , CASE_05_LOSSY_FILES                  , null },
			new Object [] { NodeStatus.LOSSLESS, CASE_06_LOSSLESS_FILES               , null },
			new Object [] { NodeStatus.NONE    , CASE_07_UNKNOWN_FILES                , null },
			new Object [] { NodeStatus.LOSSY   , CASE_08_LOSSY_UNKNOWN_FILES          , null },
			new Object [] { NodeStatus.LOSSLESS, CASE_09_LOSSLESS_UNKNOWN_FILES       , null },
			new Object [] { NodeStatus.NONE    , CASE_10_LOSSY_LOSSLESS_FILES         , MediaIssueCode.GENERIC_VARIOUS_AUDIO_FORMAT_TYPES },
			new Object [] { NodeStatus.NONE    , CASE_11_LOSSY_LOSSLESS_UNKNOWN_FILES , MediaIssueCode.GENERIC_VARIOUS_AUDIO_FORMAT_TYPES },
		};
		//@formatter:on
	}

	/**
	 * Tests method {@link MediaBranchNode#getAudioFormatType()} when audio
	 * format type wasn't initialized
	 * <p>
	 * NOTE: It is needed to use child implementation {@link InputMediaNode} for
	 * testing because {@link MediaBranchNode#addMediaIssueChild} method throws
	 * an error.
	 * 
	 * @param expectedAudioFormatType
	 *            expected audio format type
	 * @param directoryName
	 *            name of the testing media directory
	 * @param expectedMediaIssueCode
	 *            expected media issue code
	 */
	@Test(dataProvider = "testGetAudioFormatTypeNotInitialized")
	public void testGetAudioFormatTypeNotInitialized(NodeStatus expectedAudioFormatType,
			String directoryName, MediaIssueCode expectedMediaIssueCode) {
		String absolutePath = TestUtils.getResourcesPathMethod() + directoryName;
		File file = new File(absolutePath);

		//see NOTE in javadoc
		MediaBranchNode testingObject = new InputMediaNode(null, file, "");

		MediaIssuesCache mediaIssuesCache = new MediaIssuesCache();
		FileDesignator fileDesignator = mockFileDesignator();
		initSpringContextMocks(testingObject, mediaIssuesCache, fileDesignator);

		//call testing method
		NodeStatus actualAudioFormatType = testingObject.getAudioFormatType();

		Assert.assertEquals(actualAudioFormatType, expectedAudioFormatType, "Audio format type");

		verifyMediaIssue(directoryName, expectedMediaIssueCode, file, mediaIssuesCache);
	}

	/**
	 * Verify media issue if expected
	 * 
	 * @param directoryName
	 *            directory name
	 * @param expectedMediaIssueCode
	 *            expected media issue code
	 * @param file
	 *            file instance
	 * @param mediaIssuesCache
	 *            media issues cache
	 */
	void verifyMediaIssue(String directoryName, MediaIssueCode expectedMediaIssueCode, File file,
			MediaIssuesCache mediaIssuesCache) {
		int expectedMediaIssuesCacheSize = 0;
		if (expectedMediaIssueCode != null) {
			expectedMediaIssuesCacheSize = 1;
			IMediaIssue actualMediaIssue =
					(IMediaIssue) mediaIssuesCache.getInputMediaIssues().iterator().next();
			Assert.assertEquals(actualMediaIssue.getIssueCode(), expectedMediaIssueCode, "Media issue code");
			Assert.assertEquals(actualMediaIssue.getRelativePath(), File.separator + directoryName,
					"Media issue relative path");
			Assert.assertEquals(actualMediaIssue.getSourceAbsolutePath(), file.getAbsolutePath(),
					"Media issue absolute path");
			Assert.assertEquals(actualMediaIssue.getErrorMessage(), null, "Media issue error message");
		}
		Assert.assertEquals(mediaIssuesCache.getInputMediaIssues().size(), expectedMediaIssuesCacheSize,
				"Input media issues cache size");
	}

	/**
	 * Prepare file designator for testing and mock audio format preferences
	 * 
	 * @return instance of {@link FileDesignator} for testing
	 */
	private FileDesignator mockFileDesignator() {
		AudioFormatsPreferences audioFormatPreferencesMock = Mockito.mock(AudioFormatsPreferences.class);
		Mockito.when(audioFormatPreferencesMock.isLosslessAudioFormat(Mockito.anyString())).thenReturn(false);
		Mockito.when(audioFormatPreferencesMock.isLosslessAudioFormat(LOSSLESS_FORMAT)).thenReturn(true);
		Mockito.when(audioFormatPreferencesMock.isLossyAudioFormat(Mockito.anyString())).thenReturn(false);
		Mockito.when(audioFormatPreferencesMock.isLossyAudioFormat(LOSSY_FORMAT)).thenReturn(true);

		WorkbenchEnvironmentFacade workbenchEnvironmentMock = Mockito.mock(WorkbenchEnvironmentFacade.class);
		Mockito.when(workbenchEnvironmentMock.getCurrentLocale()).thenReturn(Locale.GERMANY);

		FileDesignator fileDesignator = new FileDesignator();
		Whitebox.setInternalState(fileDesignator, "audioFormatPreferences", audioFormatPreferencesMock);
		Whitebox.setInternalState(fileDesignator, "workbenchEnvironment", workbenchEnvironmentMock);
		return fileDesignator;
	}

	/**
	 * Initializes spring beans mocks into testing object
	 * 
	 * @param testingObject
	 *            testing object
	 * @param mediaIssuesCache
	 *            media issues cache to stub into Spring context mock
	 * @param fileDesignator
	 *            file designator to stub into Spring context mock
	 */
	private void initSpringContextMocks(MediaBranchNode testingObject, MediaIssuesCache mediaIssuesCache,
			FileDesignator fileDesignator) {
		TestUtils.stubBeanIntoContextAdapter(testingObject, MediaIssuesCache.class, mediaIssuesCache);
		TestUtils.stubBeanIntoContextAdapter(testingObject, FileDesignator.class, fileDesignator);
	}

	/**
	 * Unit test for {@link MediaBranchNode#getAudioFormatType()} when audio
	 * format type is already initialized
	 */
	@Test
	public void testGetAudioFormatTypeInitialized() {
		MediaBranchNode testingObject = new MediaBranchNode(null, new File(""), "");
		NodeStatus nodeStatusMock = Mockito.mock(NodeStatus.class);
		Whitebox.setInternalState(testingObject, "audioFormatType", nodeStatusMock);

		FileDesignator fileDesignatorMock = Mockito.mock(FileDesignator.class);
		TestUtils.stubBeanIntoContextAdapter(testingObject, FileDesignator.class, fileDesignatorMock);

		NodeStatus actualStatus = testingObject.getAudioFormatType();

		Assert.assertEquals(actualStatus, nodeStatusMock, "Audio format type");
		Mockito.verifyZeroInteractions(fileDesignatorMock);
	}

	/**
	 * Unit test for {@link MediaBranchNode#getAudioFormatType()} when audio
	 * format type and belonging file instance are not initialized
	 */
	@Test
	public void testGetAudioFormatTypeFileNotInitialized() {
		MediaBranchNode testingObject = new MediaBranchNode(null, null, "");

		FileDesignator fileDesignatorMock = Mockito.mock(FileDesignator.class);
		TestUtils.stubBeanIntoContextAdapter(testingObject, FileDesignator.class, fileDesignatorMock);

		NodeStatus actualStatus = testingObject.getAudioFormatType();

		Assert.assertEquals(actualStatus, NodeStatus.NONE, "Audio format type");
		Mockito.verifyZeroInteractions(fileDesignatorMock);
	}

}
