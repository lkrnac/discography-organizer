package sk.lkrnac.discorg.model.listeners;

import java.util.Iterator;

import sk.lkrnac.discorg.model.interfaces.IMediaIssue;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;



/**
 * Interface for sending visualize storages event
 * @author sitko
 *
 */
public interface IVisualizeStoragesListener {
	/**
	 * Informs about visualize storages event
	 * @param referenceRootNode root node in the reference storage 
	 * @param inputRootNode root node in the input storage
	 * @param mediaIssuesIterator iterator for media issues to be visualized
	 */
	public void visualizeStorages(ITreeStorageNode referenceRootNode,
			ITreeStorageNode inputRootNode, Iterator<IMediaIssue> mediaIssuesIterator);
}
