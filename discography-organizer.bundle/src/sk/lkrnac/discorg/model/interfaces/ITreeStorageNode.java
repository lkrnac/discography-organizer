package sk.lkrnac.discorg.model.interfaces;

import java.util.Collection;

/**
 * Represent node abstraction of tree storage in composite structure.
 * 
 * @author sitko
 * 
 */
public interface ITreeStorageNode extends Comparable<ITreeStorageNode> {
	/**
	 * @return status of tree storage node
	 */
	INodeStatus getNodeStatus();

	/**
	 * @return <code>true</code> there is another child node to process
	 */
	boolean hasNextChild();

	/**
	 * @return another child in iteration
	 */
	ITreeStorageNode getNextChild();

	/**
	 * Resets iterator of the children nodes.
	 */
	void resetChildrenIterator();

	/**
	 * @return name of the node
	 */
	String getName();

	/**
	 * @return absolute path of the node
	 */
	String getAbsolutePath();

	/**
	 * @return IDs of mirror directories on comparing storage
	 */
	Collection<String> getMirrorsAbsolutePaths();

	/**
	 * @return flag if this is full album media node
	 */
	boolean isFullAlbum();
}
