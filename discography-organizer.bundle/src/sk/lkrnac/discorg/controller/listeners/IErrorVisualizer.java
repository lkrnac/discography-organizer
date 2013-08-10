package sk.lkrnac.discorg.controller.listeners;

/**
 * Interface for visualizing error catch by controller.
 * 
 * @author sitko
 */
public interface IErrorVisualizer {
	/**
	 * Visualize given error.
	 * 
	 * @param thtowable
	 *            error to visualize
	 */
	void visualizeError(Throwable thtowable);
}
