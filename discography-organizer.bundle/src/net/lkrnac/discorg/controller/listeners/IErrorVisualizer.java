package net.lkrnac.discorg.controller.listeners;

import net.lkrnac.discorg.general.DiscOrgException;

/**
 * Interface for visualizing error catch by controller.
 * 
 * @author sitko
 */
public interface IErrorVisualizer {
	/**
	 * Visualize given error.
	 * 
	 * @param error
	 *            error to visualize
	 */
	void visualizeError(DiscOrgException error);
}
