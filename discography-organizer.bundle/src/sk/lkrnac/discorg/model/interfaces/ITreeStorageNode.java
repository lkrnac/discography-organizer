package sk.lkrnac.discorg.model.interfaces;

import java.util.Collection;
import java.util.NoSuchElementException;


/**
 * Represent node abstraction of tree storage in composite structure.
 * @author sitko
 *
 */
public interface ITreeStorageNode extends Comparable<ITreeStorageNode>{
	/**
	 * @return status of tree storage node
	 */
	public INodeStatus getNodeStatus();
	
	/**
	 * @return <code>true</code> there is another child node to process
	 */
	public boolean hasNextChild();
	
	/**
	 * @return another child in iteration
	 * @throws NoSuchElementException there isn't another child node to process
	 */
	public ITreeStorageNode getNextChild() throws NoSuchElementException;
	
	/**
	 * Resets iterator of the children nodes 
	 */
	public void resetChildrenIterator();
	
	/**
	 * @return name of the node
	 */
	public String getName();
	
	/**
	 * @return absolute path of the node
	 */
	public String getAbsolutePath(); 
	
	/**
	 * @return IDs of mirror directories on comparing storage
	 */
	public Collection<String> getMirrorsAbsolutePaths();
	
	/**
	 * @return flag if this is full album media node
	 */
	public boolean isFullAlbum();
}
