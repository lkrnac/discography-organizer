package net.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import net.lkrnac.discorg.model.interfaces.INodeStatus;
import net.lkrnac.discorg.model.interfaces.ITreeStorageNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Represents leaf (file) in composite structure.
 * 
 * @author sitko
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TreeStorageNode implements ITreeStorageNode {
	@Autowired
	private FileDesignator fileDesignator;

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
		if (parent != null) {
			parent.children.add(this);
			parent.iterator = parent.children.iterator();
			this.parent = parent;
		}
	}

	/**
	 * Initializes status of media node base on file instance.
	 * <p>
	 * NOPMD: Method is invoked by Spring
	 */
	@PostConstruct
	@SuppressWarnings("PMD.UnusedPrivateMethod")
	private void initStatus() {
		this.setNodeStatus(NodeStatus.NONE);
		if (file != null && file.isFile()) {
			if (fileDesignator.isLossLessMediaFile(file)) {
				this.setNodeStatus(NodeStatus.LOSSLESS);
			} else if (fileDesignator.isLossyMediaFile(file)) {
				this.setNodeStatus(NodeStatus.LOSSY);
			}
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
	 * @return file designator
	 */
	protected FileDesignator getFileDesignator() {
		return fileDesignator;
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
	public File getFile() {
		return file;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAbsolutePath() {
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
