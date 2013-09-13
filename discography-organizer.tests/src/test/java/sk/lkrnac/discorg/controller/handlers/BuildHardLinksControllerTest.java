package sk.lkrnac.discorg.controller.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import sk.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;
import sk.lkrnac.discorg.controller.listeners.IErrorVisualizer;
import sk.lkrnac.discorg.general.DiscOrgException;
import sk.lkrnac.discorg.general.context.DiscOrgContextHolder;

/**
 * Unit test for class {@link BuildHardLinksController}
 * 
 * @author sitko
 * 
 */
@PrepareForTest(DiscOrgContextHolder.class)
public class BuildHardLinksControllerTest extends PowerMockTestCase {
	@InjectMocks
	private BuildHardLinksController testingObject;

	@Mock
	private IErrorVisualizer errorVisualizerMock;

	@Mock
	private IBuildHardLinksListener buildHardLinksListenerMock;

	/**
	 * Injects mocks into testing object
	 */
	@BeforeClass(alwaysRun = true)
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Data provider for test
	 * {@link BuildHardLinksControllerTest#testExecute(boolean)}.
	 * 
	 * @return test cases
	 */
	@DataProvider
	public final Object[][] testExecute() {
		//@formatter:off
		return new Object [][]{
			new Object [] { true },
			new Object [] { false },
		};
		//@formatter:on
	}

	/**
	 * Tests method
	 * {@link BuildHardLinksController#execute(org.eclipse.core.commands.ExecutionEvent)}
	 * 
	 * @param throwError
	 *            if build hard links listener throws error for test case
	 * 
	 * @throws ExecutionException
	 *             taken from API of Eclipse handler
	 * @throws DiscOrgException
	 *             it is needed only because of Mockito verify syntax
	 */
	@Test(dataProvider = "testExecute")
	public void testExecute(boolean throwError) throws ExecutionException, DiscOrgException {
		PowerMockito.mockStatic(DiscOrgContextHolder.class);
		Mockito.when(DiscOrgContextHolder.getBean(IBuildHardLinksListener.class))
				.thenReturn(buildHardLinksListenerMock);
		Mockito.when(DiscOrgContextHolder.getBean(IErrorVisualizer.class))
				.thenReturn(errorVisualizerMock);

		//		DiscOrgException error = new DiscOrgException("", new Exception());
		//		if (throwError) {
		//			Mockito.doThrow(error).when(buildHardLinksListenerMock).onBuildHardLinks();
		//		}

		//call testing method 
		testingObject.execute(null);

		Mockito.verify(buildHardLinksListenerMock, Mockito.times(1)).onBuildHardLinks();
		//		if (throwError) {
		//			Mockito.verify(errorVisualizerMock, Mockito.times(1)).visualizeError(error);
		//		}
	}
}
