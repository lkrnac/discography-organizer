package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * Represents branch node of storage tree in composite structure
 * @author sitko
 *
 */
public class TreeStorageBranchNode extends TreeStorageNode{
	private String relativePath;
	/**
	 * Sub-nodes of storage tree in composite structure
	 */
	protected List<ITreeStorageNode> children;
	
	/**
	 * Children iterator
	 */
	protected Iterator<ITreeStorageNode> iterator;
	
	/**
	 * Creates instance of branch node
	 * @param parent parent node
	 * @param file belonging file object
	 * @param relativePath relative part of the branch node within storage
	 */
	public TreeStorageBranchNode(TreeStorageBranchNode parent, File file, 
			String relativePath){
		super(parent, file);
		children = new ArrayList<ITreeStorageNode>();
		iterator = children.iterator();
		this.setNodeStatus(BranchNodeStatus.FOLDER);
		if (file == null){
			this.relativePath = File.separator;
		} else {
			this.relativePath = relativePath + File.separator + file.getName();
		}
	}

	/**
	 * @return relative path in the storage
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNextChild(){
		return iterator.hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITreeStorageNode getNextChild() throws NoSuchElementException{
		return iterator.next();
	}
	
	/**
	 * Clears all children nodes
	 */
	public void clearAllChildren(){
		children = new ArrayList<ITreeStorageNode>();
		resetChildrenIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void resetChildrenIterator() {
		iterator = children.iterator(); 
	}
}
