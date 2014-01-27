package net.lkrnac.discorg.general.context;

//SUPPRESS CHECKSTYLE UnusedImports 1 Import is used in java-doc
import net.lkrnac.discorg.controller.handlers.BuildHardLinksController;

/**
 * Reason for this adaptor is testability.
 * Spring context instance is sometimes needed by classes not driven by Spring
 * container. Therefore there is singleton class {@link DiscOrgContextHolder}.
 * <p>
 * But singleton can't be test with plain Mockito (only with PowerMockito).
 * Therefore I decided not to use singleton everywhere Spring context instance
 * is needed and rather use this adapter.
 * <p>
 * This adapter can be easily tested by composite encapsulation + @InjectMocks
 * Mockito feature. See test for {@link BuildHardLinksController}.
 * 
 * @author sitko
 * 
 */
public class DiscOrgContextAdapter {
	/**
	 * Finds bean in Spring context with given type.
	 * 
	 * @param beanClass
	 *            bean type class
	 * @param <T>
	 *            bean type
	 * @return bean instance
	 */
	public <T> T getBean(Class<T> beanClass) {
		return DiscOrgContextHolder.getInstance().getContext().getBean(beanClass);
	}
}
