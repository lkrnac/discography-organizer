package net.lkrnac.discorg.controller.handlers;

import net.lkrnac.discorg.controller.listeners.IBuildHardLinksListener;
import net.lkrnac.discorg.general.DiscOrgException;
import net.lkrnac.discorg.general.context.DiscOrgContextAdapter;

import org.eclipse.core.commands.ExecutionException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link BuildHardLinksController}.
 * 
 * @author sitko
 * 
 */
public class BuildHardLinksControllerTest {
	@InjectMocks
	private BuildHardLinksController testingObject;

	//	@Mock
	//	private IErrorVisualizer errorVisualizerMock;

	@Mock
	private IBuildHardLinksListener buildHardLinksListenerMock;

	@Mock
	private DiscOrgContextAdapter discOrgContextAdapterMock;

	/**
	 * Injects mocks into testing object.
	 */
	@BeforeMethod(alwaysRun = true)
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
	 * .
	 * 
	 * @param throwError
	 *            if build hard links listener throws error for test case
	 * 
	 * @throws ExecutionException
	 *             taken from API of Eclipse handler
	 * @throws DiscOrgException
	 *             it is needed only because of Mockito verify syntax
	 */
	//NOPMD: test contains mockito verification
	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	@Test(dataProvider = "testExecute")
	public void testExecute(boolean throwError) throws ExecutionException, DiscOrgException {
		Mockito.when(discOrgContextAdapterMock.getBean(IBuildHardLinksListener.class)).thenReturn(
				buildHardLinksListenerMock);
		//		Mockito.when(discOrgContextAdapterMock.getBean(IErrorVisualizer.class)).thenReturn(
		//				errorVisualizerMock);

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
