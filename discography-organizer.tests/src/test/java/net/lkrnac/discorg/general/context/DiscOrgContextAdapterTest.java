package net.lkrnac.discorg.general.context;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit test for class {@link DiscOrgContextAdapter}
 * 
 * @author sitko
 * 
 */
@PrepareForTest(DiscOrgContextHolder.class)
public class DiscOrgContextAdapterTest extends PowerMockTestCase {
	private class DummyClass {
	}

	/**
	 * Test for {@link DiscOrgContextAdapter#getBean(Class)}
	 */
	@Test
	public void testGetBean() {
		DummyClass expectedBean = new DummyClass();
		ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
		Mockito.when(applicationContext.getBean(DummyClass.class)).thenReturn(expectedBean);

		PowerMockito.mockStatic(DiscOrgContextHolder.class);
		DiscOrgContextHolder contextHolderMock = PowerMockito.mock(DiscOrgContextHolder.class);
		Mockito.when(DiscOrgContextHolder.getInstance()).thenReturn(contextHolderMock);
		Mockito.when(contextHolderMock.getContext()).thenReturn(applicationContext);

		//call testing method
		DummyClass actualBean = new DiscOrgContextAdapter().getBean(DummyClass.class);

		Assert.assertEquals(actualBean, expectedBean);
	}
}
