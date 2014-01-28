package net.lkrnac.discorg.model.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.lkrnac.discorg.general.constants.MediaIssueCode;
import net.lkrnac.discorg.model.cache.MediaIssuesCache;
import net.lkrnac.discorg.model.cache.ReferenceStorageCache;
import net.lkrnac.discorg.model.dal.io.DirectoryIoFacade;
import net.lkrnac.discorg.model.preferences.StoragesPreferences;
import net.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
	private BuildHardLinksHandler buildHardLinksHandler;

	@Mock
	private ReferenceStorageCache referenceStorageCacheMock;

	@Mock
	private MediaIssuesCache mediaIssuesCacheMock;

	@Mock
	private StoragesPreferences storagesPreferencesMock;

	@Mock
	private LoadStoragesHandler loadStoragesHandlerMock;

	/**
	 * Initialize mocks and testing object spy
	 */
	@BeforeClass(alwaysRun = true)
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Data provider for test
	 * {@link BuildHardLinksHandlerTest#testOnBuildHardLinks(List, List, List)}
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
		ArrayList<File> expectedFullDirs = new ArrayList<File>();

		for (int i = 0; i < TESTING_COUNT; i++) {
			String path = TEST_SELECTION_PATH + i;
			String fullPath = TEST_FULL_PATH + i;
			selectionPaths.add(path);

			DirectoryIoFacade directoryIoFacadeMock = Mockito.mock(DirectoryIoFacade.class);
			directoryIoFacadeMocks.add(directoryIoFacadeMock);

			ReferenceMediaNode selectionNodeMock = Mockito.mock(ReferenceMediaNode.class);

			File expectedFullDir = new File(fullPath);
			expectedFullDirs.add(expectedFullDir);
			ReferenceMediaNode fullNodeMock = Mockito.mock(ReferenceMediaNode.class);

			Mockito.when(fullNodeMock.getFile()).thenReturn(expectedFullDir);
			Mockito.when(fullNodeMock.getAbsolutePath()).thenReturn(fullPath);
			Mockito.when(selectionNodeMock.getDirectoryIoFacade()).thenReturn(directoryIoFacadeMock);
			Mockito.when(selectionNodeMock.getFullMirror()).thenReturn(fullNodeMock);
			Mockito.when(referenceStorageCacheMock.getReferenceMediaNode(path)).thenReturn(selectionNodeMock);
		}

		return new Object[][] { new Object[] { selectionPaths, expectedFullDirs, directoryIoFacadeMocks }, };
	}

	/**
	 * Unite test for {@link BuildHardLinksHandler#onBuildHardLinks()}
	 * 
	 * @param selectionPaths
	 *            collection of directories to test
	 * @param expectedFullDirs
	 *            collection of expected full storage mirrors
	 * @param directoryIoFacadeMocks
	 *            collection of mocks to test IO layer with
	 * @throws Exception
	 *             if error occurs
	 */
	//NOPMD: verification phase is done by Mockito.verify()
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	@Test(dataProvider = "testOnBuildHardLinks")
	public void testOnBuildHardLinks(List<String> selectionPaths, List<File> expectedFullDirs,
			List<DirectoryIoFacade> directoryIoFacadeMocks) throws Exception {
		Mockito.when(
				mediaIssuesCacheMock
						.getSourceAbsolutePaths(MediaIssueCode.REFERENCE_NO_HARD_LINK_IN_SELECTION))
				.thenReturn(selectionPaths);

		Mockito.when(storagesPreferencesMock.getFullSubDirectory()).thenReturn(TEST_FULL_SUBDIRECTORY_NAME);
		Mockito.doNothing().when(loadStoragesHandlerMock).loadStorages();

		// call testing method
		buildHardLinksHandler.onBuildHardLinks();

		for (int idx = 0; idx < TESTING_COUNT; idx++) {
			DirectoryIoFacade directoryIoFacadeMock = directoryIoFacadeMocks.get(idx);
			Mockito.verify(directoryIoFacadeMock).buildHardLinks(expectedFullDirs.get(idx));
		}
		Mockito.verify(loadStoragesHandlerMock, Mockito.times(1)).loadStorages();
	}
}
