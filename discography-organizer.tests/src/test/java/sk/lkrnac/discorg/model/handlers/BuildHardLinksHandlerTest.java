package sk.lkrnac.discorg.model.handlers;

import java.io.File;
import java.util.ArrayList;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import sk.lkrnac.discorg.general.constants.MediaIssueCode;
import sk.lkrnac.discorg.model.cache.MediaIssuesCache;
import sk.lkrnac.discorg.model.cache.ReferenceStorageCache;
import sk.lkrnac.discorg.model.dal.io.DirectoryIoFacade;
import sk.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;
import sk.lkrnac.discorg.preferences.StoragesPreferences;

/**
 * Unit test for {@link BuildHardLinksHandler}
 * 
 * @author sitko
 * 
 */
public class BuildHardLinksHandlerTest {
	private static final String SELECTION_SUBDIR = "testSelectionPath";
	private static final String PATH = "path/";
	private static final String TEST_SELECTION_PATH = PATH + SELECTION_SUBDIR;
	private static final String TEST_FULL_SUBDIRECTORY_NAME = "=[full]";
	private static final String TEST_FULL_PATH = PATH + TEST_FULL_SUBDIRECTORY_NAME + "/" + SELECTION_SUBDIR;
	private static final int TESTING_COUNT = 10;

	@InjectMocks
	private BuildHardLinksHandler buildHardLinksHandler = new BuildHardLinksHandler();

	@Mock
	private ReferenceStorageCache referenceStorageCacheMock;

	@Mock
	private MediaIssuesCache mediaIssuesCacheMock;

	@Mock
	private StoragesPreferences storagesPreferencesMock;

	/**
	 * Initialize mocks and testing object spy
	 */
	@BeforeClass(alwaysRun = true)
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Data provider for test
	 * {@link BuildHardLinksHandlerTest#testOnBuildHardLinks(ArrayList, ArrayList, ArrayList)}
	 * 
	 * @return test cases
	 */
	@DataProvider
	public Object[][] testOnBuildHardLinks() {
		// prepare media issues cache mock
		ArrayList<String> selectionPaths = new ArrayList<String>();

		// collection of mocks that will be used for verification
		ArrayList<DirectoryIoFacade> directoryIoFacadeMocks = new ArrayList<DirectoryIoFacade>();

		// collection of full paths used for verification
		ArrayList<String> fullPaths = new ArrayList<String>();

		for (int i = 0; i < TESTING_COUNT; i++) {
			String path = TEST_SELECTION_PATH + i;
			String fullPath = TEST_FULL_PATH + i;
			selectionPaths.add(path);
			fullPaths.add(fullPath);

			DirectoryIoFacade directoryIoFacadeMock = Mockito.mock(DirectoryIoFacade.class);
			directoryIoFacadeMocks.add(directoryIoFacadeMock);

			ReferenceMediaNode selectionNodeMock = Mockito.mock(ReferenceMediaNode.class);
			ReferenceMediaNode fullNodeMock = Mockito.mock(ReferenceMediaNode.class);

			Mockito.when(fullNodeMock.getAbsolutePath()).thenReturn(fullPath);
			Mockito.when(selectionNodeMock.getDirectoryIoFacade()).thenReturn(directoryIoFacadeMock);
			Mockito.when(selectionNodeMock.getFullMirror()).thenReturn(fullNodeMock);
			Mockito.when(referenceStorageCacheMock.getReferenceMediaNode(path)).thenReturn(selectionNodeMock);
		}

		return new Object[][] { new Object[] { selectionPaths, fullPaths, directoryIoFacadeMocks }, };
	}

	/**
	 * Unite test for {@link BuildHardLinksHandler#onBuildHardLinks()}
	 * 
	 * @param selectionPaths
	 *            collection of directories to test
	 * @param fullPaths
	 *            collection of full storage mirrors
	 * @param directoryIoFacadeMocks
	 *            collection of mocks to test IO layer with
	 * @throws Exception
	 *             if error occurs
	 */
	// @Test(dataProvider = "testOnBuildHardLinks")
	public void testOnBuildHardLinks(ArrayList<String> selectionPaths, ArrayList<String> fullPaths,
			ArrayList<DirectoryIoFacade> directoryIoFacadeMocks) throws Exception {
		Mockito.when(
				mediaIssuesCacheMock
						.getSourceAbsolutePaths(MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION))
				.thenReturn(selectionPaths);

		Mockito.when(storagesPreferencesMock.getFullSubDirectory()).thenReturn(TEST_FULL_SUBDIRECTORY_NAME);

		// call testing method
		buildHardLinksHandler.onBuildHardLinks();

		for (int i = 0; i < TESTING_COUNT; i++) {
			DirectoryIoFacade directoryIoFacadeMock = directoryIoFacadeMocks.get(i);

			File expectedFullDir = new File(fullPaths.get(i));
			Mockito.verify(directoryIoFacadeMock).buildHardLinks(expectedFullDir);
		}
	}
	// @DataProvider
	// public Object[][] testOnBuildHardLinks() {
	// // prepare media issues cache mock
	// ArrayList<String> selectionPaths = new ArrayList<String>();
	//
	// // reference storage cache mock
	// ReferenceStorageCache referenceCacheMock =
	// Mockito.mock(ReferenceStorageCache.class);
	//
	// // collection of mocks that will be used for verification
	// ArrayList<DirectoryIoFacade> directoryIoFacadeMocks = new
	// ArrayList<DirectoryIoFacade>();
	//
	// // collection of full paths used for verification
	// ArrayList<String> fullPaths = new ArrayList<String>();
	//
	// for (int i = 0; i < TESTING_COUNT; i++) {
	// String path = TEST_SELECTION_PATH + i;
	// String fullPath = TEST_FULL_PATH + i;
	// selectionPaths.add(path);
	// fullPaths.add(fullPath);
	//
	// DirectoryIoFacade directoryIoFacadeMock =
	// Mockito.mock(DirectoryIoFacade.class);
	// directoryIoFacadeMocks.add(directoryIoFacadeMock);
	//
	// ReferenceMediaNode selectionNodeMock =
	// Mockito.mock(ReferenceMediaNode.class);
	// ReferenceMediaNode fullNodeMock = Mockito.mock(ReferenceMediaNode.class);
	//
	// Mockito.when(fullNodeMock.getAbsolutePath()).thenReturn(fullPath);
	// Mockito.when(selectionNodeMock.getDirectoryIoFacade())
	// .thenReturn(directoryIoFacadeMock);
	// Mockito.when(selectionNodeMock.getFullMirror()).thenReturn(fullNodeMock);
	// Mockito.when(referenceCacheMock.getReferenceMediaNode(path)).thenReturn(
	// selectionNodeMock);
	// }
	//
	// return new Object[][] { new Object[] { referenceCacheMock,
	// selectionPaths, fullPaths,
	// directoryIoFacadeMocks }, };
	// }
	//
	// @Test
	// public void testOnBuildHardLinks(ReferenceStorageCache
	// referenceCacheMock,
	// ArrayList<String> selectionPaths, ArrayList<String> fullPaths,
	// ArrayList<DirectoryIoFacade> directoryIoFacadeMocks) {
	// MediaIssuesCache mediaIssuesCacheMock =
	// Mockito.mock(MediaIssuesCache.class);
	// Mockito.when(
	// mediaIssuesCacheMock
	// .getSourceAbsolutePaths(MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION))
	// .thenReturn(selectionPaths);
	//
	// StoragesPreferences storagesPreferencesMock =
	// Mockito.mock(StoragesPreferences.class);
	// Mockito.when(storagesPreferencesMock.getFullSubDirectory()).thenReturn(
	// TEST_FULL_SUBDIRECTORY_NAME);
	//
	// BuildHardLinksHandler testingObject = new BuildHardLinksHandler();
	// BuildHardLinksHandler testingObjectSpy = Mockito.spy(testingObject);
	//
	// Mockito.wh
	// }
}
