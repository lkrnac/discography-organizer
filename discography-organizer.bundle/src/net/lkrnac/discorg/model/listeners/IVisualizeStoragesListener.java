package net.lkrnac.discorg.model.listeners;

import java.util.Iterator;

import net.lkrnac.discorg.model.interfaces.IMediaIssue;
import net.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * Interface for sending visualize storages event.
 * 
 * @author sitko
 * 
 */
public interface IVisualizeStoragesListener {
	/**
	 * Informs about visualize storages event.
	 * 
	 * @param referenceRootNode
	 *            root node in the reference storage
	 * @param inputRootNode
	 *            root node in the input storage
	 * @param mediaIssuesIterator
	 *            iterator for media issues to be visualized
	 */
	void visualizeStorages(ITreeStorageNode referenceRootNode, ITreeStorageNode inputRootNode,
			Iterator<IMediaIssue> mediaIssuesIterator);
}
