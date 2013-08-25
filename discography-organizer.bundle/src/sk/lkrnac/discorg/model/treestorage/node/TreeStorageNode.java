package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import sk.lkrnac.discorg.general.context.DiscOrgContextHolder;
import sk.lkrnac.discorg.model.interfaces.INodeStatus;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * Represents leaf (file) in composite structure.
 * 
 * @author sitko
 * 
 */
public class TreeStorageNode implements ITreeStorageNode {
	/**
	 * Parent node in composite structure.
	 */
	private TreeStorageBranchNode parent;

	/**
	 * File object belonging to this tree node.
	 */
	private final File file;

	private INodeStatus nodeStatus;

	/**
	 * Creates instance of tree node.
	 * 
	 * @param parent
	 *            parent node
	 * @param file
	 *            belonging file object
	 */
	public TreeStorageNode(TreeStorageBranchNode parent, File file) {
		this.file = file;
		if (file != null) {
			initStatus(file);
		}
		if (parent != null) {
			parent.children.add(this);
			parent.iterator = parent.children.iterator();
			this.parent = parent;
		}
	}

	/**
	 * Initializes status of media node base on file instance.
	 * 
	 * @param file
	 *            file object belonging to media node
	 */
	private void initStatus(File file) {
		this.setNodeStatus(NodeStatus.NONE);
		if (file.isFile()) {
			if (getFileDesignator().isLossLessMediaFile(file)) {
				this.setNodeStatus(NodeStatus.LOSSLESS);
			} else if (getFileDesignator().isLossyMediaFile(file)) {
				this.setNodeStatus(NodeStatus.LOSSY);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	// SUPPRESS CHECKSTYLE DesignForExtension 4 this meant to be overriden
	@Override
	public boolean isFullAlbum() {
		return false;
	}

	/**
	 * @return parent node in composite structure
	 */
	public final ITreeStorageNode getParent() {
		return parent;
	}

	/**
	 * @return file designator retrieved from Spring context
	 */
	protected final FileDesignator getFileDesignator() {
		return DiscOrgContextHolder.getInstance().getContext().getBean(FileDesignator.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getName() {
		return getFile() == null ? null : getFile().getName();
	}

	/**
	 * {@inheritDoc}.
	 * <p>
	 * This implementation returns false, because {@link TreeStorageNode}
	 * suppose to be leaf node (doesn't have children)
	 */
	// SUPPRESS CHECKSTYLE DesignForExtension 3 this means to be overriden
	@Override
	public boolean hasNextChild() {
		return false;
	}

	/**
	 * {@inheritDoc}.
	 * <p>
	 * This implementation throws {@link NoSuchElementException}, because
	 * {@link TreeStorageNode} suppose to be leaf node (doesn't have children)
	 */
	// SUPPRESS CHECKSTYLE DesignForExtension 4 this means to be overriden
	@Override
	public ITreeStorageNode getNextChild() {
		throw new NoSuchElementException();
	}

	/**
	 * {@inheritDoc}.
	 * <p>
	 * This implementation does nothing, because {@link TreeStorageNode} suppose
	 * to be leaf node (doesn't have children)
	 */
	@Override
	public void resetChildrenIterator() {
		// do nothing
	}

	/**
	 * @return file object belonging to this node
	 */
	public final File getFile() {
		return file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getAbsolutePath() {
		return getFile() == null ? null : getFile().getAbsolutePath();
	}

	/**
	 * {@inheritDoc}
	 */
	// SUPPRESS CHECKSTYLE DesignForExtension 4 this means to be overridden
	@Override
	public Collection<String> getMirrorsAbsolutePaths() {
		return new HashSet<String>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final INodeStatus getNodeStatus() {
		return nodeStatus;
	}

	/**
	 * @param nodeStatus
	 *            status of the node <br>
	 *            It is not set if there was already adjusted status with higher
	 *            severity
	 */
	public final void setNodeStatus(INodeStatus nodeStatus) {
		if (nodeStatus != null
				&& (this.nodeStatus == null || this.nodeStatus.getSeverity() < nodeStatus.getSeverity())) {
			this.nodeStatus = nodeStatus;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int compareTo(ITreeStorageNode arg0) {
		return this.getName().compareTo(arg0.getName());
	}
}
