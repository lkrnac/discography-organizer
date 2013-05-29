package sk.lkrnac.discorg.model;

/**
 * Exception to reflect some application specific error
 * 
 * @author sitko
 * 
 */
public class DiscographyOrganizerException extends Exception {
	/**
	 * Serializable version
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates instance of discography organizer's specific exception
	 * 
	 * @param message
	 *            error message
	 */
	public DiscographyOrganizerException(String message) {
		super(message);
	}

	/**
	 * Creates instance of discography organizer's specific exception
	 * 
	 * @param message
	 *            error message
	 * @param t
	 *            cause of the error
	 */
	public DiscographyOrganizerException(String message, Throwable t) {
		super(message, t);
	}
}
