package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import sk.lkrnac.discorg.general.context.DiscOrgContextHolder;
import sk.lkrnac.discorg.model.interfaces.INodeStatus;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;


/**
 * Represents leaf (file) in composite structure
 * @author sitko
 *
 */
public class TreeStorageNode implements ITreeStorageNode {
	/**
	 * Parent node in composite structure
	 */
	private TreeStorageBranchNode parent;

	/**
	 * File object belonging to this tree node
	 */
	private File file;
	
	private INodeStatus nodeStatus;
	
	/**
	 * Creates instance of tree node
	 * @param parent parent node
	 * @param file belonging file object
	 */
	public TreeStorageNode(TreeStorageBranchNode parent, File file){
		this.file = file;
		if (file != null){
			this.setNodeStatus(NodeStatus.NONE);
			if (file.isFile()){
				if (getFileDesignator().isLossLessMediaFile(file)){
					this.setNodeStatus(NodeStatus.LOSSLESS);
				} else if (getFileDesignator().isLossyMediaFile(file)) {
					this.setNodeStatus(NodeStatus.LOSSY);
				}
			}
		}
		if (parent != null){
			parent.children.add(this);
			parent.iterator = parent.children.iterator();
			this.parent = parent;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFullAlbum() {
		return false;
	}

	/**
	 * @return parent node in composite structure
	 */
	public ITreeStorageNode getParent() {
		return parent;
	}
	
	/**
	 * @return file designator retrieved from Spring context
	 */
	protected FileDesignator getFileDesignator() {
		return DiscOrgContextHolder.getInstance()
				.getContext().getBean(FileDesignator.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return (getFile() != null) ? getFile().getName() : null;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation returns false, because {@link TreeStorageNode}
	 * suppose to be leaf node (doesn't have children) 
	 */
	@Override
	public boolean hasNextChild(){
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation throws {@link NoSuchElementException}, 
	 * because {@link TreeStorageNode}
	 * suppose to be leaf node (doesn't have children) 
	 */
	@Override
	public ITreeStorageNode getNextChild() throws NoSuchElementException{
		throw new NoSuchElementException();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation does nothing, because {@link TreeStorageNode}
	 * suppose to be leaf node (doesn't have children) 
	 */
	@Override
	public void resetChildrenIterator() {
		//do nothing
	}

	/**
	 * @return file object belonging to this node
	 */
	public File getFile(){
		return file;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAbsolutePath() {
		return (getFile() != null) ? getFile().getAbsolutePath() : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getMirrorsAbsolutePaths() {
		return new HashSet<String>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public INodeStatus getNodeStatus() {
		return nodeStatus;
	}

	/**
	 * Setter
	 * @param nodeStatus status of the node
	 * <br>It is not set if there was already 
	 * adjusted status with higher severity
	 *  
	 */
	public void setNodeStatus(INodeStatus nodeStatus) {
		if (nodeStatus != null && (this.nodeStatus == null ||
				this.nodeStatus.getSeverity() < nodeStatus.getSeverity()))
			this.nodeStatus = nodeStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(ITreeStorageNode arg0) {
		return this.getName().compareTo(arg0.getName());
	}
}
