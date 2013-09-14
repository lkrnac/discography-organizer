package sk.lkrnac.discorg.test.utils;

import java.io.File;

import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import sk.lkrnac.discorg.general.context.DiscOrgContextAdapter;

/**
 * Various test related utility methods
 * 
 * @author sitko
 */
public final class TestUtils {
	/**
	 * Base path for test resources
	 */
	private static final String TEST_RESOURCES_PATH = "src" + File.separator + "test" + File.separator
			+ "resources" + File.separator;

	/**
	 * Private constructor to avoid instantiation
	 */
	private TestUtils() {
	}

	/**
	 * Returns relative path where test resources for calling test method
	 * should be stored
	 * <p>
	 * <b><u> This method expects that is called directly from testing method
	 * (not some sub-method) </u></b>
	 * <p>
	 * Desired path would be {@link TestUtils#TEST_RESOURCES_PATH}
	 * /[test_package]/[test_class]/[test_method]
	 * 
	 * @return relative path to belonging test resources
	 */
	public static String getResourcesPathMethod() {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		StackTraceElement stackTraceElement = ste[2];
		return getResourcesPathClass(stackTraceElement.getClassName()) + stackTraceElement.getMethodName()
				+ File.separator;
	}

	/**
	 * Returns relative path where test resources for given test class
	 * should be stored
	 * <p>
	 * Desired path would be {@link TestUtils#TEST_RESOURCES_PATH}
	 * /[test_package]/ [test_class]
	 * 
	 * @param fullClassName
	 *            full class name of unit test class
	 * @return relative path to belonging test resources
	 */
	public static String getResourcesPathClass(String fullClassName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(TEST_RESOURCES_PATH).append(fullClassName);
		int lastIndexOf = stringBuilder.lastIndexOf(".");
		stringBuilder.replace(lastIndexOf, lastIndexOf + 1, File.separator);
		stringBuilder.append(File.separator);
		return stringBuilder.toString();
	}

	/**
	 * Verifies if testing object has initialized {@link DiscOrgContextAdapter}
	 * mock.
	 * If not initializes one and stubs given bean into it.
	 * This way simulates Spring beans for classes that use
	 * {@link DiscOrgContextAdapter}.
	 * 
	 * @param testingObject
	 *            testing object
	 * @param beanClass
	 *            class of the bean
	 * @param bean
	 *            bean instance
	 */
	public static <T> void stubBeanIntoSpringContextAdapter(Object testingObject, Class<T> beanClass, T bean) {
		DiscOrgContextAdapter contextAdapterMock =
				Whitebox.getInternalState(testingObject, DiscOrgContextAdapter.class);
		if (contextAdapterMock == null) {
			contextAdapterMock = Mockito.mock(DiscOrgContextAdapter.class);
			Whitebox.setInternalState(testingObject, DiscOrgContextAdapter.class, contextAdapterMock);
		}
		Mockito.when(contextAdapterMock.getBean(beanClass)).thenReturn(bean);
	}

}
