package net.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.lkrnac.discorg.general.constants.MediaIssueCode;
import net.lkrnac.discorg.model.cache.MediaIssue;
import net.lkrnac.discorg.model.cache.MediaIssuesCache;
import net.lkrnac.discorg.model.cache.ReferenceStorageCache;
import net.lkrnac.discorg.model.dal.io.DirectoryComparisonResult;
import net.lkrnac.discorg.model.dal.io.DirectoryIoFacade;
import net.lkrnac.discorg.model.interfaces.IMediaIssue;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
	private static final String FULL_PARENT_ABSOLUTE_PATH = TEST_PATH + File.separator + DIR_NAME_FULL_ALBUM;

	@InjectMocks
	private ReferenceMediaNode testingObject;

	@Spy
	private MediaIssuesCache mediaIssuesCache;

	//	@Spy
	//	private ReferenceStorageCache referenceStorageCache;

	//@formatter:off
	/**
	 * Prepares testing data for test
	 * 
	 * {@link ReferenceMediaNodeTest# testCheckFullAlbumForSelection 
	 * (int, String, ReferenceStorageCache, Object, Object, boolean, boolean, boolean, boolean)}
	 * 
	 * 
	 * @return parameters for test
	 */
	//@formatter:on
	//NOPMD: This test is planned to be refactored 
	@SuppressWarnings("PMD.ExcessiveMethodLength")
	@DataProvider(name = DP_TEST_CHECK_FULL_ALBUM_FOR_SELECTION)
	public Object[][] testCheckFullAlbumForSelection() {
		String pathToFullMirror = FULL_PARENT_ABSOLUTE_PATH + File.separator + DIR_NAME_TEST_ALBUM_1;
		String relativePath = TEST_PATH + File.separator + DIR_NAME_TEST_ALBUM_1;
		MediaIssue mediaIssueMissing =
				new MediaIssue(TEST_PATH, MediaIssueCode.REFERENCE_FULL_MIRROR_MISSING, relativePath, false);
		MediaIssue mediaIssueVarious =
				new MediaIssue(TEST_PATH, MediaIssueCode.REFERENCE_VARIOUS_FULL_MIRRORS_FOUND, relativePath,
						true);
		MediaIssue mediaIssueIoErrorDuringComparison =
				new MediaIssue(TEST_PATH, MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON, relativePath,
						true);
		MediaIssue mediaIssueNoHardLinks =
				new MediaIssue(TEST_PATH, MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION, relativePath,
						true);
		MediaIssue mediaIssueIoErrorDuringHardLinksCheck =
				new MediaIssue(TEST_PATH, MediaIssueCode.REFERENCE_HARD_LINK_IO_ERROR, relativePath, true);
		int idx = 0;
		return new Object[][] {
				// create one full album mirror in storages meta-data maps and
				// expect no issue
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, true), BranchNodeStatus.FOLDER,
						null, true, false, true, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, false), BranchNodeStatus.FOLDER,
						null, true, false, true, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, true), BranchNodeStatus.ERROR,
						mediaIssueNoHardLinks, true, false, false, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, false), BranchNodeStatus.FOLDER,
						null, true, false, false, false },

				// - don't create mirror in storages meta-data maps
				// and expect warning and media issue creation
				// - other checks shouldn't affect result
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false, true, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false, true, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false, true, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false, true, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false, false, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false, false, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false, false, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false, false, false },

				// - create various full album mirrors in storages meta-data
				// maps and
				// expect error about various full mirrors found
				// - full mirror checks shouldn't affect the result
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, false), BranchNodeStatus.ERROR,
						mediaIssueVarious, true, false, true, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, true), BranchNodeStatus.ERROR,
						mediaIssueVarious, true, false, true, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, false), BranchNodeStatus.ERROR,
						mediaIssueVarious, true, false, false, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, true), BranchNodeStatus.ERROR,
						mediaIssueVarious, true, false, false, false },

				// test cases with IO error throwing during directory comparison
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, true, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, false), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, true, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, false, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, false), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, false, false },

				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true, true, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true, true, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true, true, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true, true, false },

				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true, false, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true, false, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true, false, false },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true, false, false },

				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, false), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, true, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, true, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, false), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, false, false },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, false, false },

				//all previous test cases with throwing IO error during hard links check
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringHardLinksCheck, true, false, true, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, false), BranchNodeStatus.FOLDER,
						null, true, false, true, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringHardLinksCheck, true, false, false, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, false), BranchNodeStatus.FOLDER,
						null, true, false, false, true },

				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false, true, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false, true, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false, true, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false, true, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false, false, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, false, false, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false, false, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, false, false, true },

				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, false), BranchNodeStatus.ERROR,
						mediaIssueVarious, true, false, true, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, true), BranchNodeStatus.ERROR,
						mediaIssueVarious, true, false, true, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, false), BranchNodeStatus.ERROR,
						mediaIssueVarious, true, false, false, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, true), BranchNodeStatus.ERROR,
						mediaIssueVarious, true, false, false, true },

				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, true, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, false), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, true, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, false, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 1, false), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, false, true },

				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true, true, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true, true, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true, true, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true, true, true },

				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true, false, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, false, true, false, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, false),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true, false, true },
				new Object[] { idx++, pathToFullMirror, getReferenceStorageCacheMock(null, 0, true),
						BranchNodeStatus.WARNING, mediaIssueMissing, true, true, false, true },

				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, false), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, true, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, true, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, false), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, false, true },
				new Object[] { idx++, pathToFullMirror,
						getReferenceStorageCacheMock(pathToFullMirror, 2, true), BranchNodeStatus.ERROR,
						mediaIssueIoErrorDuringComparison, true, true, false, true },

		};
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
	 * @param referenceStorageCache
	 *            meta-data where are testing data for comparison stored
	 * @param expectedStatusObj
	 *            expected node status
	 * @param expectedIssueObj
	 *            expected media issue for testing node
	 * @param dirComparisonSucceed
	 *            if the comparison of files in selection and its full mirror
	 *            should succeed
	 * @param throwIoErrorDuringDirComparison
	 *            if comparison of directories should throw IO error
	 * @param hardLinksCheckPassed
	 *            if hard links check should pass
	 * @param throwIoErrorDuringHardLinksCheck
	 *            if hard link check should throw IO error
	 * @throws IOException
	 *             if I/O error occurs
	 */
	@Test(dataProvider = DP_TEST_CHECK_FULL_ALBUM_FOR_SELECTION)
	public void testCheckFullAlbumForSelection(int testCaseId, String pathToFullMirror,
			ReferenceStorageCache referenceStorageCache, Object expectedStatusObj, Object expectedIssueObj,
			boolean dirComparisonSucceed, boolean throwIoErrorDuringDirComparison,
			boolean hardLinksCheckPassed, boolean throwIoErrorDuringHardLinksCheck) throws IOException {
		testingObject =
				initializeMocks(TEST_PATH, dirComparisonSucceed, throwIoErrorDuringDirComparison,
						hardLinksCheckPassed, throwIoErrorDuringHardLinksCheck);
		mediaIssuesCache = new MediaIssuesCache();
		MockitoAnnotations.initMocks(this);
		Whitebox.setInternalState(testingObject, ReferenceStorageCache.class, referenceStorageCache);

		// call testing method
		testingObject.checkFullAlbumForSelection(DIR_NAME_FULL_ALBUM);

		// verify media node status and expected media issue
		Assert.assertEquals(testingObject.getNodeStatus(), expectedStatusObj);
		verifyMediaIssue(expectedIssueObj, mediaIssuesCache);

		// if there is one one mirror and directories comparison was successful
		Collection<ReferenceMediaNode> fullMirrorsList =
				referenceStorageCache.getReferenceItems(pathToFullMirror);
		if (dirComparisonSucceed && !throwIoErrorDuringDirComparison && fullMirrorsList.size() == 1) {
			// retrieve full mirror mock and verify its fullMirror flag
			ReferenceMediaNode fullMirror = fullMirrorsList.iterator().next();
			Mockito.verify(fullMirror, Mockito.times(1)).setFullAlbum(true);

			// if checks for full album were successful
			if (fullMirror.checkSelectionForFullAlbum("")) {
				// verify pairing between full album and selection
				Assert.assertEquals(testingObject.getFullMirror(), fullMirror);
				Mockito.verify(fullMirror, Mockito.times(1)).setSelectionMirror(testingObject);
			}
		}
	}

	//@formatter:off
	/**
	 * Prepares testing data for test
	 * {@link ReferenceMediaNodeTest#testCheckSelectionForFullAlbum
	 * (int, ReferenceStorageCache, Object, Object, boolean, boolean, boolean)}
	 * 
	 * @return parameters for test
	 */
	//@formatter:on
	@DataProvider(name = DP_TEST_CHECK_SELECTION_FOR_FULL_ALBUM)
	public Object[][] testCheckSelectionForFullAlbum() {
		String relativePath = FULL_PARENT_ABSOLUTE_PATH + File.separator + DIR_NAME_TEST_ALBUM_1;
		String pathToSelectionMirror = TEST_PATH + File.separator + DIR_NAME_TEST_ALBUM_1;
		MediaIssue mediaIssueMissing =
				new MediaIssue(FULL_PARENT_ABSOLUTE_PATH, MediaIssueCode.REFERENCE_MISSING_SELECTION_MIRROR,
						relativePath, false);
		MediaIssue mediaIssueVarious =
				new MediaIssue(FULL_PARENT_ABSOLUTE_PATH,
						MediaIssueCode.REFERENCE_VARIOUS_SELECTION_MIRRORS_FOUND, relativePath, true);
		MediaIssue mediaIssueIoError =
				new MediaIssue(FULL_PARENT_ABSOLUTE_PATH, MediaIssueCode.GENERIC_IO_ERROR_DURING_COMPARISON,
						relativePath, true);
		int idx = 0;
		return new Object[][] {
				// create one selection album mirror in storages meta-data maps and expect no issue
				new Object[] { idx++, getReferenceStorageCacheMock(pathToSelectionMirror, 1, null),
						BranchNodeStatus.FOLDER, null, true, true, false },

				// - don't create mirror in storages meta-data maps
				// and expect warning and media issue creation
				// - comparison of directories doesn't have any sense and
				// shouldn't affect the result
				new Object[] { idx++, getReferenceStorageCacheMock(null, 0, null), BranchNodeStatus.IGNORED,
						mediaIssueMissing, false, false, false },
				new Object[] { idx++, getReferenceStorageCacheMock(null, 0, null), BranchNodeStatus.IGNORED,
						mediaIssueMissing, true, false, false },

				// - create various selection album mirrors in storages meta-data maps and
				// expect error about various selection mirrors found
				new Object[] { idx++, getReferenceStorageCacheMock(pathToSelectionMirror, 2, null),
						BranchNodeStatus.ERROR, mediaIssueVarious, true, false, false },

				// test cases with throwing IO error
				new Object[] { idx++, getReferenceStorageCacheMock(pathToSelectionMirror, 1, null),
						BranchNodeStatus.ERROR, mediaIssueIoError, true, false, true },

				new Object[] { idx++, getReferenceStorageCacheMock(null, 0, null), BranchNodeStatus.IGNORED,
						mediaIssueMissing, false, false, true },
				new Object[] { idx++, getReferenceStorageCacheMock(null, 0, null), BranchNodeStatus.IGNORED,
						mediaIssueMissing, true, false, true },

				new Object[] { idx++, getReferenceStorageCacheMock(pathToSelectionMirror, 2, null),
						BranchNodeStatus.ERROR, mediaIssueIoError, true, false, true }, };
	}

	/**
	 * Tests {@link ReferenceMediaNode#checkSelectionForFullAlbum(String)} by
	 * creating a mockito's spy object, because test wants to avoid the
	 * Spring's context initialization. All the instances needed from Spring
	 * context are mocked.
	 * <p>
	 * Verifies:
	 * <ul>
	 * <li>desired media node status</li>
	 * <li>expected media issue</li>
	 * </ul>
	 * 
	 * @param referenceStorageCache
	 *            meta-data where are testing data for comparison stored
	 * @param expectedStatusObj
	 *            expected node status
	 * @param expectedIssueObj
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
	 *             if I/O error occurs
	 */
	@Test(dataProvider = DP_TEST_CHECK_SELECTION_FOR_FULL_ALBUM)
	public void testCheckSelectionForFullAlbum(int testCaseId, ReferenceStorageCache referenceStorageCache,
			Object expectedStatusObj, Object expectedIssueObj, boolean dirComparisonSucceed,
			boolean expectedResult, boolean throwIoError) throws IOException {
		testingObject =
				initializeMocks(FULL_PARENT_ABSOLUTE_PATH, dirComparisonSucceed, throwIoError, null, null);
		testingObject.setFullAlbum(true);

		mediaIssuesCache = new MediaIssuesCache();
		MockitoAnnotations.initMocks(this);
		Whitebox.setInternalState(testingObject, ReferenceStorageCache.class, referenceStorageCache);

		// call testing method
		boolean result = testingObject.checkSelectionForFullAlbum(DIR_NAME_FULL_ALBUM);

		// verify media node status and expected media issue
		Assert.assertEquals(testingObject.getNodeStatus(), expectedStatusObj);
		verifyMediaIssue(expectedIssueObj, mediaIssuesCache);
		// verify result
		Assert.assertEquals(result, expectedResult, "Different return value expected");
	}

	/**
	 * Initialize mocks for full/selection check tests
	 * 
	 * @param parentDirectoryPath
	 *            parent directory path of testing object
	 * @param dirComparisonSucceed
	 *            desired result of directories comparison
	 * @param throwIoErrorDuringDirComparison
	 *            if comparison of directories should throw IO error
	 * @param hardLinksCheckPassed
	 *            if hard links check should pass
	 * @param throwIoErrorDuringHardLinksCheck
	 *            if hard link check should throw IO error
	 * @return testing object
	 * @throws IOException
	 *             declared to avoid compiler error (method is stubbed)
	 */
	private ReferenceMediaNode initializeMocks(String parentDirectoryPath, boolean dirComparisonSucceed,
			boolean throwIoErrorDuringDirComparison, Boolean hardLinksCheckPassed,
			Boolean throwIoErrorDuringHardLinksCheck) throws IOException {
		DirectoryIoFacade dirIoFacadeMock = Mockito.mock(DirectoryIoFacade.class);

		if (throwIoErrorDuringDirComparison) {
			Mockito.when(dirIoFacadeMock.compareDirectories(Mockito.any(File.class), Mockito.any(File.class)))
					.thenThrow(new IOException());
		} else {
			DirectoryComparisonResult comparisonResult =
					dirComparisonSucceed ? DirectoryComparisonResult.EQUAL
							: DirectoryComparisonResult.DIFFERENT_FILES;
			Mockito.when(dirIoFacadeMock.compareDirectories(Mockito.any(File.class), Mockito.any(File.class)))
					.thenReturn(comparisonResult);
		}

		if (throwIoErrorDuringHardLinksCheck != null && throwIoErrorDuringHardLinksCheck) {
			Mockito.when(dirIoFacadeMock.verifyHardLinks(Mockito.any(File.class))).thenThrow(
					new IOException());
		} else if (hardLinksCheckPassed != null) {
			Mockito.when(dirIoFacadeMock.verifyHardLinks(Mockito.any(File.class))).thenReturn(
					hardLinksCheckPassed);
		}

		File dirMock = Mockito.mock(File.class);
		Mockito.when(dirMock.listFiles()).thenReturn(new File[] {});
		Mockito.when(dirMock.getName()).thenReturn(DIR_NAME_TEST_ALBUM_1);
		Mockito.when(dirMock.getAbsolutePath()).thenReturn(parentDirectoryPath);

		// create testing object
		ReferenceMediaNode testingObject = new ReferenceMediaNode(null, dirMock, parentDirectoryPath);

		Whitebox.setInternalState(testingObject, DirectoryIoFacade.class, dirIoFacadeMock);
		return testingObject;
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
	private ReferenceStorageCache getReferenceStorageCacheMock(String pathToFileMock, int numberOfMirrors,
			Boolean passFullMirrorChecks) {
		ReferenceStorageCache referenceStorageCacheMock = Mockito.mock(ReferenceStorageCache.class);
		Mockito.when(referenceStorageCacheMock.toString()).thenReturn(
				"Mock for " + ReferenceStorageCache.class.getSimpleName() + "[pathToFileMock = \""
						+ ((pathToFileMock == null) ? "null" : pathToFileMock) + "\"]");

		Collection<ReferenceMediaNode> mirrorMocksList = new ArrayList<ReferenceMediaNode>(numberOfMirrors);

		if (pathToFileMock != null && numberOfMirrors > 0) {
			for (int i = 0; i < numberOfMirrors; i++) {
				ReferenceMediaNode fullMirrorMock = Mockito.mock(ReferenceMediaNode.class);

				if (passFullMirrorChecks != null) {
					Mockito.when(fullMirrorMock.checkSelectionForFullAlbum(Mockito.anyString())).thenReturn(
							passFullMirrorChecks);
				}
				mirrorMocksList.add(fullMirrorMock);
			}
		}

		Mockito.when(referenceStorageCacheMock.getReferenceItems(pathToFileMock)).thenReturn(mirrorMocksList);
		return referenceStorageCacheMock;
	}

	/**
	 * Check if testing object caused expected media issue
	 * 
	 * @param expectedIssue
	 *            media issue that is expected to be raised by testing method
	 * @param mediaIssuesCache
	 *            list where media issue should or shouldn't be stored
	 */
	private void verifyMediaIssue(Object expectedIssue, MediaIssuesCache mediaIssuesCache) {
		Iterator<IMediaIssue> iterator = mediaIssuesCache.getIterator();
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
			if (iterator.hasNext()) {
				Assert.fail("More than one media issue found, but expected only one");
			}
		}
	}
}
