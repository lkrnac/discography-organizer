package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.model.dal.io.DirectoryIoFacade;
import sk.lkrnac.discorg.model.dal.messages.MediaIssueMessages;
import sk.lkrnac.discorg.model.interfaces.IMediaIssue;
import sk.lkrnac.discorg.model.metadata.MediaIssue;
import sk.lkrnac.discorg.model.metadata.MediaIssuesCollection;
import sk.lkrnac.discorg.model.metadata.StorageMetadataMaps;

/**
 * Tests {@link ReferenceMediaNode}
 * 
 * @author sitko
 */
public class ReferenceMediaNodeTest {
	private static final String DP_TEST_CHECK_SELECTION_FOR_FULL_ALBUM = "testCheckSelectionForFullAlbum";
	private static final String DP_TEST_CHECK_FULL_ALBUM_FOR_SELECTION = "testCheckFullAlbumForSelection";
	private static final String TEST_PATH = "test-path";
	private static final String DIR_NAME_TEST_ALBUM_1 = "test - album 1";
	private static final String DIR_NAME_FULL_ALBUM = "=[full]";
	private static final String FULL_PARENT_ABSOLUTE_PATH = TEST_PATH + File.separator
			+ DIR_NAME_FULL_ALBUM;

	/**
	 * Prepares testing data for test
	 * {@link ReferenceMediaNodeTest# testCheckFullAlbumForSelection(int, String, StorageMetadataMaps, BranchNodeStatus, MediaIssue, boolean, boolean)}
	 * 
	 * @return parameters for test
	 */
	@DataProvider(name = DP_TEST_CHECK_FULL_ALBUM_FOR_SELECTION)
	public Object[][] testCheckFullAlbumForSelection() {
		String pathToFullMirror = FULL_PARENT_ABSOLUTE_PATH + File.separator
				+ DIR_NAME_TEST_ALBUM_1;
		String relativePath = TEST_PATH + File.separator + DIR_NAME_TEST_ALBUM_1;
		MediaIssue mediaIssueMissing = new MediaIssue(TEST_PATH,
				MediaIssueMessages.REFERENCE_FULL_MIRROR_MISSING, relativePath, false);
		MediaIssue mediaIssueVarious = new MediaIssue(TEST_PATH,
				MediaIssueMessages.REFERENCE_VARIOUS_FULL_MIRRORS_FOUND, relativePath, true);
		MediaIssue mediaIssueIoError = new MediaIssue(TEST_PATH,
				MediaIssueMessages.GENERIC_IO_ERROR + MediaIssueMessages.GENERIC_COMPARING,
				relativePath, true);
		int i = 0;
		return new Object[][] {
				// create one full album mirror in storages meta-data maps and
				// expect no issue
				new Object[] { i++, pathToFullMirror,
						getStorageMetadataMapsMock(pathToFullMirror, 1, true),
						BranchNodeStatus.FOLDER, null, true, false },
				new Object[] { i++, pathToFullMirror,
						getStorageMetadataMapsMock(pathToFullMirror, 1, false),
						BranchNodeStatus.FOLDER, null, true, false },

				// - don't create mirror in storages meta-data maps
				// and expect warning and media issue creation
				// - other checks shouldn't affect result
				new Object[] { i++, pathToFullMirror, getStorageMetadataMapsMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false },
				new Object[] { i++, pathToFullMirror, getStorageMetadataMapsMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false },
				new Object[] { i++, pathToFullMirror, getStorageMetadataMapsMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false },
				new Object[] { i++, pathToFullMirror, getStorageMetadataMapsMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false },

				// - create various full album mirrors in storages meta-data
				// maps and
				// expect error about various full mirrors found
				// - full mirror checks shouldn't affect the result
				new Object[] { i++, pathToFullMirror,
						getStorageMetadataMapsMock(pathToFullMirror, 2, false),
						BranchNodeStatus.ERROR, mediaIssueVarious, true, false },
				new Object[] { i++, pathToFullMirror,
						getStorageMetadataMapsMock(pathToFullMirror, 2, true),
						BranchNodeStatus.ERROR, mediaIssueVarious, true, false },

				// test cases with IO error throwing
				new Object[] { i++, pathToFullMirror,
						getStorageMetadataMapsMock(pathToFullMirror, 1, true),
						BranchNodeStatus.ERROR, mediaIssueIoError, true, true },
				new Object[] { i++, pathToFullMirror,
						getStorageMetadataMapsMock(pathToFullMirror, 1, false),
						BranchNodeStatus.ERROR, mediaIssueIoError, true, true },

				new Object[] { i++, pathToFullMirror, getStorageMetadataMapsMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true },
				new Object[] { i++, pathToFullMirror, getStorageMetadataMapsMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true },
				new Object[] { i++, pathToFullMirror, getStorageMetadataMapsMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true },
				new Object[] { i++, pathToFullMirror, getStorageMetadataMapsMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true },

				new Object[] { i++, pathToFullMirror,
						getStorageMetadataMapsMock(pathToFullMirror, 2, false),
						BranchNodeStatus.ERROR, mediaIssueIoError, true, true },
				new Object[] { i++, pathToFullMirror,
						getStorageMetadataMapsMock(pathToFullMirror, 2, true),
						BranchNodeStatus.ERROR, mediaIssueIoError, true, true }, };
	}

	/**
	 * Tests {@link ReferenceMediaNode#checkFullAlbumForSelection(String)} by
	 * creating a mockito's spy object, because test wants to avoid the Spring's
	 * context initialization. All the instances needed from Spring context are
	 * mocked.
	 * <p>
	 * Verifies:
	 * <ul>
	 * <li>desired media node status</li>
	 * <li>expected media issue</li>
	 * <li>fullMirror flag of full mirror mock</li>
	 * <li>pairing between selection album and mirror</li>
	 * <li>if hard links check was invoked</li>
	 * </ul>
	 * 
	 * @param testCaseId
	 *            identifies test case (not used)
	 * @param pathToFullMirror
	 *            relative path to full mirror mock
	 * @param storageMetadataMaps
	 *            meta-data where are testing data for comparison stored
	 * @param expectedStatus
	 *            expected node status
	 * @param expectedIssue
	 *            expected media issue for testing node
	 * @param dirComparisonSucceed
	 *            if the comparison of files in selection and its full mirror
	 *            should succeed
	 * @param throwIoError
	 *            if comparison of directories should throw IO error
	 * @throws IOException
	 *             if I/O error occurs
	 */
	@Test(dataProvider = DP_TEST_CHECK_FULL_ALBUM_FOR_SELECTION)
	public void testCheckFullAlbumForSelection(int testCaseId, String pathToFullMirror,
			StorageMetadataMaps storageMetadataMaps, BranchNodeStatus expectedStatus,
			MediaIssue expectedIssue, boolean dirComparisonSucceed, boolean throwIoError)
			throws IOException {
		ReferenceMediaNode testingObjectSpy = initializeMocks(TEST_PATH, storageMetadataMaps,
				dirComparisonSucceed, throwIoError);

		MediaIssuesCollection mediaIssuesList = createMediaIssuesMock(testingObjectSpy);
		Mockito.doNothing().when(testingObjectSpy).checkHardLinksForSelectionMirror();

		// call testing method
		testingObjectSpy.checkFullAlbumForSelection(DIR_NAME_FULL_ALBUM);

		// verify media node status and expected media issue
		Assert.assertEquals(testingObjectSpy.getNodeStatus(), expectedStatus);
		verifyMediaIssue(expectedIssue, mediaIssuesList);

		// if there is one one mirror and directories comparison was successful
		Collection<ReferenceMediaNode> fullMirrorsList = storageMetadataMaps
				.getReferenceItems(pathToFullMirror);
		if (dirComparisonSucceed && !throwIoError && fullMirrorsList.size() == 1) {
			// retrieve full mirror mock and verify its fullMirror flag
			ReferenceMediaNode fullMirror = fullMirrorsList.iterator().next();
			Mockito.verify(fullMirror, Mockito.times(1)).setFullAlbum(true);

			// if checks for full album were successful
			if (fullMirror.checkSelectionForFullAlbum("")) {
				// verify pairing between full album and selection
				Mockito.verify(testingObjectSpy, Mockito.times(1)).setFullMirror(fullMirror);
				Mockito.verify(fullMirror, Mockito.times(1)).setSelectionMirror(testingObjectSpy);

				// verify if hard link check was invoked
				Mockito.verify(testingObjectSpy, Mockito.times(1))
						.checkHardLinksForSelectionMirror();
			}
		}
	}

	/**
	 * Prepares testing data for test
	 * {@link ReferenceMediaNodeTest# testCheckFullAlbumForSelection(int, String, StorageMetadataMaps, BranchNodeStatus, MediaIssue, boolean, boolean)}
	 * 
	 * @return parameters for test
	 */
	@DataProvider(name = DP_TEST_CHECK_SELECTION_FOR_FULL_ALBUM)
	public Object[][] testCheckSelectionForFullAlbum() {
		String relativePath = FULL_PARENT_ABSOLUTE_PATH + File.separator + DIR_NAME_TEST_ALBUM_1;
		String pathToSelectionMirror = TEST_PATH + File.separator + DIR_NAME_TEST_ALBUM_1;
		MediaIssue mediaIssueMissing = new MediaIssue(FULL_PARENT_ABSOLUTE_PATH,
				MediaIssueMessages.REFERENCE_MISSING_SELECTION_MIRROR, relativePath, false);
		MediaIssue mediaIssueVarious = new MediaIssue(FULL_PARENT_ABSOLUTE_PATH,
				MediaIssueMessages.REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND, relativePath, true);
		MediaIssue mediaIssueIoError = new MediaIssue(FULL_PARENT_ABSOLUTE_PATH,
				MediaIssueMessages.GENERIC_IO_ERROR + MediaIssueMessages.GENERIC_COMPARING,
				relativePath, true);
		int i = 0;
		return new Object[][] {
				// create one selection album mirror in storages meta-data maps
				// and
				// expect no issue
				new Object[] { i++, getStorageMetadataMapsMock(pathToSelectionMirror, 1, null),
						BranchNodeStatus.FOLDER, null, true, true, false },

				// - don't create mirror in storages meta-data maps
				// and expect warning and media issue creation
				// - comparison of directories doesn't have any sense and
				// shouldn't affect the result
				new Object[] { i++, getStorageMetadataMapsMock(null, 0, null),
						BranchNodeStatus.IGNORED, mediaIssueMissing, false, false, false },
				new Object[] { i++, getStorageMetadataMapsMock(null, 0, null),
						BranchNodeStatus.IGNORED, mediaIssueMissing, true, false, false },

				// - create various selection album mirrors in storages
				// meta-data maps and
				// expect error about various selection mirrors found
				new Object[] { i++, getStorageMetadataMapsMock(pathToSelectionMirror, 2, null),
						BranchNodeStatus.ERROR, mediaIssueVarious, true, false, false },

				// test cases with throwing IO error
				new Object[] { i++, getStorageMetadataMapsMock(pathToSelectionMirror, 1, null),
						BranchNodeStatus.ERROR, mediaIssueIoError, true, false, true },

				new Object[] { i++, getStorageMetadataMapsMock(null, 0, null),
						BranchNodeStatus.IGNORED, mediaIssueMissing, false, false, true },
				new Object[] { i++, getStorageMetadataMapsMock(null, 0, null),
						BranchNodeStatus.IGNORED, mediaIssueMissing, true, false, true },

				new Object[] { i++, getStorageMetadataMapsMock(pathToSelectionMirror, 2, null),
						BranchNodeStatus.ERROR, mediaIssueIoError, true, false, true }, };
	}

	/**
	 * Tests {@link ReferenceMediaNode#checkSelectionForFullAlbum(String)} by
	 * creating a mockito's spy object, because test wants to avoid the Spring's
	 * context initialization. All the instances needed from Spring context are
	 * mocked.
	 * <p>
	 * Verifies:
	 * <ul>
	 * <li>desired media node status</li>
	 * <li>expected media issue</li>
	 * </ul>
	 * 
	 * @param storageMetadataMaps
	 *            meta-data where are testing data for comparison stored
	 * @param expectedStatus
	 *            expected node status
	 * @param expectedIssue
	 *            expected media issue for testing node
	 * @param dirComparisonSucceed
	 *            if the comparison of files in selection and its full mirror
	 *            should succeed
	 * @param expectedResult
	 *            expected result of the check
	 * @param testCaseId
	 *            identifies test case (not used)
	 * @param throwIoError
	 *            if comparison of directories should throw IO error
	 * @throws IOException
	 *             if IO error occurs
	 */
	@Test(dataProvider = DP_TEST_CHECK_SELECTION_FOR_FULL_ALBUM)
	public void testCheckSelectionForFullAlbum(int testCaseId,
			StorageMetadataMaps storageMetadataMaps, BranchNodeStatus expectedStatus,
			MediaIssue expectedIssue, boolean dirComparisonSucceed, boolean expectedResult,
			boolean throwIoError) throws IOException {
		ReferenceMediaNode testingObjectSpy = initializeMocks(FULL_PARENT_ABSOLUTE_PATH,
				storageMetadataMaps, dirComparisonSucceed, throwIoError);
		testingObjectSpy.setFullAlbum(true);

		MediaIssuesCollection mediaIssuesList = createMediaIssuesMock(testingObjectSpy);

		// call testing method
		boolean result = testingObjectSpy.checkSelectionForFullAlbum(DIR_NAME_FULL_ALBUM);

		// verify media node status and expected media issue
		Assert.assertEquals(testingObjectSpy.getNodeStatus(), expectedStatus);
		verifyMediaIssue(expectedIssue, mediaIssuesList);
		// verify result
		Assert.assertEquals(result, expectedResult, "Different return value expected");
	}

	/**
	 * Create media issues list and mock it to the testing spy object
	 * 
	 * @param testingObjectSpy
	 *            testing spy object
	 * @return created media issues list
	 */
	private MediaIssuesCollection createMediaIssuesMock(ReferenceMediaNode testingObjectSpy) {
		MediaIssuesCollection mediaIssuesList = new MediaIssuesCollection();
		Mockito.doReturn(mediaIssuesList).when(testingObjectSpy).getMediaIssuesList();
		return mediaIssuesList;
	}

	/**
	 * Initialize mocks for full/selection check tests
	 * 
	 * @param parentDirectoryPath
	 *            parent directory path of testing object
	 * @param storageMetadataMaps
	 *            Mock for storage meta-data maps (see
	 *            {@link StorageMetadataMaps})
	 * @param dirComparisonSucceed
	 *            desired result of directories comparison
	 * @param throwIoError
	 *            if comparison of directories should throw IO error
	 * @return testing object spy
	 * @throws IOException
	 *             declared to avoid compiler error (method is stubbed)
	 */
	private ReferenceMediaNode initializeMocks(String parentDirectoryPath,
			StorageMetadataMaps storageMetadataMaps, boolean dirComparisonSucceed,
			boolean throwIoError) throws IOException {
		DirectoryIoFacade dirIoFacadeMock = Mockito.mock(DirectoryIoFacade.class);
		if (throwIoError) {
			Mockito.when(
					dirIoFacadeMock.compareDirectories(Mockito.any(File.class),
							Mockito.any(File.class))).thenThrow(
					new IOException(MediaIssueMessages.GENERIC_IO_ERROR
							+ MediaIssueMessages.GENERIC_COMPARING));
		} else {
			Mockito.when(
					dirIoFacadeMock.compareDirectories(Mockito.any(File.class),
							Mockito.any(File.class))).thenReturn(dirComparisonSucceed);
		}

		File dirMock = Mockito.mock(File.class);
		Mockito.when(dirMock.listFiles()).thenReturn(new File[] {});
		Mockito.when(dirMock.getName()).thenReturn(DIR_NAME_TEST_ALBUM_1);
		Mockito.when(dirMock.getAbsolutePath()).thenReturn(parentDirectoryPath);

		// create testing object and it's spy
		ReferenceMediaNode testingObject = new ReferenceMediaNode(null, dirMock,
				parentDirectoryPath);
		ReferenceMediaNode testingObjectSpy = Mockito.spy(testingObject);

		Mockito.doReturn(storageMetadataMaps).when(testingObjectSpy).getStorageMetadataMaps();
		Mockito.doReturn(dirIoFacadeMock).when(testingObjectSpy).getDirectoryIoFacade();
		return testingObjectSpy;
	}

	/**
	 * Creates and returns storage meta-data maps mock object with included mock
	 * {@link ReferenceMediaNode}, which suppose to be mirror album for testing
	 * <p>
	 * Is used for testing where methods where content in storage meta-data is
	 * expected
	 * 
	 * @param pathToFileMock
	 *            relative path to {@link ReferenceMediaNode} mock<br>
	 *            This will be added into storage meta-data maps
	 * @param numberOfMirrors
	 *            number of mirror mocks to be added for pathToFileMock
	 * @param passFullMirrorChecks
	 *            if the checks for full mirror should pass
	 * @return storage meta-data maps mock for testing
	 */
	private StorageMetadataMaps getStorageMetadataMapsMock(String pathToFileMock,
			int numberOfMirrors, Boolean passFullMirrorChecks) {
		StorageMetadataMaps storageMetadataMaps = Mockito.mock(StorageMetadataMaps.class);
		Mockito.when(storageMetadataMaps.toString()).thenReturn(
				"Mock for " + StorageMetadataMaps.class.getSimpleName() + "[pathToFileMock = \""
						+ ((pathToFileMock == null) ? "null" : pathToFileMock) + "\"]");

		Collection<ReferenceMediaNode> mirrorMocksList = new ArrayList<ReferenceMediaNode>(
				numberOfMirrors);

		if (pathToFileMock != null && numberOfMirrors > 0) {
			for (int i = 0; i < numberOfMirrors; i++) {
				ReferenceMediaNode fullMirrorMock = Mockito.mock(ReferenceMediaNode.class);

				if (passFullMirrorChecks != null) {
					Mockito.when(fullMirrorMock.checkSelectionForFullAlbum(Mockito.anyString()))
							.thenReturn(passFullMirrorChecks);
				}
				mirrorMocksList.add(fullMirrorMock);
			}
		}

		Mockito.when(storageMetadataMaps.getReferenceItems(pathToFileMock)).thenReturn(
				mirrorMocksList);
		return storageMetadataMaps;
	}

	/**
	 * Check if testing object caused expected media issue
	 * 
	 * @param expectedIssue
	 *            media issue that is expected to be raised by testing method
	 * @param mediaIssuesList
	 *            list where media issue should or shouldn't be stored
	 */
	private void verifyMediaIssue(MediaIssue expectedIssue, MediaIssuesCollection mediaIssuesList) {
		Iterator<IMediaIssue> iterator = mediaIssuesList.getIterator();
		if (expectedIssue == null) {
			Assert.assertEquals(iterator.hasNext(), false, "No media issue is expected");
		} else {
			try {
				IMediaIssue actualIssueAbstract = iterator.next();
				if (!(actualIssueAbstract instanceof MediaIssue)) {
					Assert.fail("Unexpected media issue type");
				}
				MediaIssue actualIssue = (MediaIssue) actualIssueAbstract;
				Assert.assertEquals(actualIssue, expectedIssue);
			} catch (NoSuchElementException e) {
				Assert.fail("Media issue is expected: " + expectedIssue, e);
			}
			if (iterator.hasNext() == true) {
				Assert.fail("More than one media issue found, but expected only one");
			}
		}
	}
}
