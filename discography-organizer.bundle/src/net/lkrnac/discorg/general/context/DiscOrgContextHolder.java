package net.lkrnac.discorg.general.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Holder of the spring context instance.
 * 
 * @author sitko
 */
final class DiscOrgContextHolder {
	private final ApplicationContext context;

	/** Private constructor of singleton class. */
	private DiscOrgContextHolder() {
		context = new AnnotationConfigApplicationContext(DiscOrgContext.class);
	}

	/**
	 * Singleton static holder inner class.
	 * 
	 * @author sitko
	 */
	// NOPMD:
	// http://en.wikipedia.org/wiki/Singleton_pattern#The_solution_of_Bill_Pugh
	@SuppressWarnings("PMD.AccessorClassGeneration")
	private static class SingletonHolder {
		public static final DiscOrgContextHolder SINGLETON_HOLDER = new DiscOrgContextHolder();
	}

	/**
	 * @return instance of singleton
	 */
	public static DiscOrgContextHolder getInstance() {
		return SingletonHolder.SINGLETON_HOLDER;
	}

	/**
	 * @return application Spring context
	 */
	public ApplicationContext getContext() {
		return context;
	}
}