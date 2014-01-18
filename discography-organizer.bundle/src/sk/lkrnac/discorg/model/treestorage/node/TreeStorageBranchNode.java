package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sk.lkrnac.discorg.general.context.DiscOrgBeanQualifiers;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * Represents branch node of storage tree in composite structure.
 * 
 * @author sitko
 * 
 */
@Component(DiscOrgBeanQualifiers.TREE_STORAGE_BRANCH_NODE)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TreeStorageBranchNode extends TreeStorageNode {
	private String relativePath;
	/**
	 * Sub-nodes of storage tree in composite structure.
	 */
	// SUPPRESS CHECKSTYLE VisibilityModifier 10 inherited fields
	protected List<ITreeStorageNode> children;

	/**
	 * Children iterator.
	 */
	protected Iterator<ITreeStorageNode> iterator;

	/**
	 * Creates instance of branch node.
	 * 
	 * @param parent
	 *            parent node
	 * @param file
	 *            belonging file object
	 * @param relativePath
	 *            relative part of the branch node within storage
	 */
	public TreeStorageBranchNode(TreeStorageBranchNode parent, File file, String relativePath) {
		super(parent, file);
		children = new ArrayList<ITreeStorageNode>();
		iterator = children.iterator();
		this.setNodeStatus(BranchNodeStatus.FOLDER);
		if (file == null) {
			this.relativePath = File.separator;
		} else {
			this.relativePath = relativePath + File.separator + file.getName();
		}
	}

	/**
	 * @return relative path in the storage
	 */
	public final String getRelativePath() {
		return relativePath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean hasNextChild() {
		return iterator.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ITreeStorageNode getNextChild() {
		return iterator.next();
	}

	/**
	 * Clears all children nodes.
	 */
	public final void clearAllChildren() {
		children = new ArrayList<ITreeStorageNode>();
		resetChildrenIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void resetChildrenIterator() {
		iterator = children.iterator();
	}
}
