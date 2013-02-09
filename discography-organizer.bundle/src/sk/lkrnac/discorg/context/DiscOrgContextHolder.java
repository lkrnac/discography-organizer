package sk.lkrnac.discorg.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Holder of the spring context instance
 * @author sitko
 */
public class DiscOrgContextHolder {
	private ApplicationContext context;
	
	/** Private constructor of singleton class */
	private DiscOrgContextHolder(){
		context = new AnnotationConfigApplicationContext(
				DiscOrgContext.class);
	}
	 
	private static class SingletonHolder{
		public static final DiscOrgContextHolder 
			SINGLETON_HOLDER = new DiscOrgContextHolder(); 
	} 
	
	/**
	 * @return instance of singleton
	 */
	public static DiscOrgContextHolder getInstance(){
		return SingletonHolder.SINGLETON_HOLDER;
	}

	/**
	 * @return application Spring context
	 */
	public ApplicationContext getContext() {
		return context;
	}
	
	
}