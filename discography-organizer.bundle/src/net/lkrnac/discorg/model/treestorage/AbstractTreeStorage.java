package net.lkrnac.discorg.model.treestorage;

import java.beans.Introspector;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.lkrnac.discorg.model.interfaces.ITreeStorageNode;
import net.lkrnac.discorg.model.treestorage.node.FileDesignator;
import net.lkrnac.discorg.model.treestorage.node.MediaBranchNode;
import net.lkrnac.discorg.model.treestorage.node.TreeStorageBranchNode;
import net.lkrnac.discorg.model.treestorage.node.TreeStorageNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Abstract storage of tree elements. It represents root of media
 * files/directories tree.
 * <p>
 * NOPMD: This abstract class contains template methods and few abstract methods
 * that are used by its children. It is less than 300 lines long. Decided not to
 * re-factor it because of methods count.
 * 
 * @author sitko
 */
@SuppressWarnings("PMD.TooManyMethods")
public abstract class AbstractTreeStorage implements ApplicationContextAware {
	private TreeStorageBranchNode rootNode;

	@Autowired
	private FileDesignator fileDesignator;

	private ApplicationContext applicationContext;

	/**
	 * @return Spring application context that will be injected via child bean.
	 */
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * @return root node of tree storage
	 */
	public TreeStorageBranchNode getRootNode() {
		return rootNode;
	}

	/**
	 * Clear old meta data and reload storage from disk.
	 */
	public void loadStorage() {
		// if this is a first load -> create root node
		if (getRootNode() == null) {
			rootNode = createTreeStorageBranchNode(null, null, null);
		}

		// clear previous load and load again
		getRootNode().clearAllChildren();
		if (!getStoragePath().isEmpty()) {
			loadTreeFromHdd(getStoragePath(), getRootNode());
		}

		// perform post load processing
		if (needPostLoadProcessing()) {
			processPostLoad(getRootNode());
		}
	}

	/**
	 * Perform recursive post load processing for tree storage.<br>
	 * For each node call
	 * {@link AbstractTreeStorage#processNodePostLoad(ITreeStorageNode)}
	 * 
	 * @param parent
	 *            parent node in composite structure
	 */
	private void processPostLoad(TreeStorageNode parent) {
		while (parent.hasNextChild()) {
			TreeStorageNode nextChild = (TreeStorageNode) parent.getNextChild();
			processNodePostLoad(nextChild);
			processPostLoad(nextChild);
			parent.setNodeStatus(nextChild.getNodeStatus());
		}
		parent.resetChildrenIterator();
	}

	/**
	 * Loads music files storage into tree.
	 * 
	 * @param path
	 *            - absolute path of directory to load
	 * @param rootNode
	 *            - tree where to store directory subtree
	 */
	private void loadTreeFromHdd(String path, TreeStorageBranchNode rootNode) {
		File rootDir = new File(path);
		loadSubNode(rootDir, rootNode, ""); //$NON-NLS-1$
	}

	/**
	 * Recursively loads whore directory subtree.
	 * 
	 * @param parentFile
	 *            - parent File object
	 * @param parentNode
	 *            - parent node
	 * @param relativePath
	 *            - relative part of node in music file storage
	 */
	private void loadSubNode(File parentFile, TreeStorageBranchNode parentNode, String relativePath) {
		// sort subfiles or sub-directories by name
		List<File> subFiles = sortSubNodes(parentFile);

		// for each subfile or sub-directory create TreeItem object and put it
		// into tree
		for (File childFile : subFiles) {
			String childRelativePath = relativePath + File.separator + childFile.getName();

			// if child is directory
			TreeStorageNode childNode = null;
			if (childFile.isDirectory()) {
				if (fileDesignator.isMediaDir(childFile)) {
					// create media directory
					childNode = createMediaNode(parentNode, childFile, relativePath);

					loadNode(childNode);
				} else {
					childNode = createTreeStorageBranchNode(parentNode, relativePath, childFile);
				}

				// recursively read sub items
				loadSubNode(childFile, (TreeStorageBranchNode) childNode, childRelativePath);

				// adjust parent status
				parentNode.setNodeStatus(childNode.getNodeStatus());
			} else {
				childNode = createTreeStorageNode(parentNode, childFile);
			}
		}
	}

	/**
	 * Creates tree storage branch node as prototype bean.
	 * 
	 * @param parentNode
	 *            parent node
	 * @param relativePath
	 *            relative path of the node
	 * @param file
	 *            file belonging to the node
	 * @return created {@link TreeStorageBranchNode} instance
	 */
	private TreeStorageBranchNode createTreeStorageBranchNode(TreeStorageBranchNode parentNode,
			String relativePath, File file) {
		String beanName = Introspector.decapitalize(TreeStorageBranchNode.class.getSimpleName());
		return (TreeStorageBranchNode) applicationContext.getBean(beanName, parentNode, file, relativePath);
	}

	/**
	 * Creates tree storage node as prototype bean.
	 * 
	 * @param parentNode
	 *            parent node
	 * @param file
	 *            file belonging to node
	 * @return created {@link TreeStorageNode} instance
	 */
	private TreeStorageNode createTreeStorageNode(TreeStorageBranchNode parentNode, File file) {
		String beanName = Introspector.decapitalize(TreeStorageNode.class.getSimpleName());
		return (TreeStorageNode) getApplicationContext().getBean(beanName, parentNode, file);
	}

	/**
	 * Performs sort on sub-nodes.
	 * 
	 * @param parent
	 *            parent file
	 * @return sorted sub-files
	 */
	private List<File> sortSubNodes(File parent) {
		// TODO: implement changeable sorting
		List<File> subFiles = Arrays.asList(parent.listFiles());
		Collections.sort(subFiles, new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				return file1.getName().compareTo(file2.getName());
			}
		});
		return subFiles;
	}

	/**
	 * Create child storage specific media node.
	 * 
	 * @param parent
	 *            parent node
	 * @param file
	 *            file belonging to the node
	 * @param relativePath
	 *            relative part within storage
	 * @return media branch node instance to be added into meta-data tree
	 */
	protected abstract MediaBranchNode createMediaNode(TreeStorageBranchNode parent, File file,
			String relativePath);

	/**
	 * Is used for loading directory object and performing various checks.
	 * 
	 * @param treeNode
	 *            - branch node in tree composite structure
	 */
	protected abstract void loadNode(ITreeStorageNode treeNode);

	/**
	 * @return absolute path of music files storage
	 */
	protected abstract String getStoragePath();

	/**
	 * @return specifies if post load processing needs to be running for the
	 *         storage
	 */
	protected abstract boolean needPostLoadProcessing();

	/**
	 * Runs post load processing for tree node.
	 * 
	 * @param treeNode
	 *            branch node in tree composite structure
	 */
	protected abstract void processNodePostLoad(ITreeStorageNode treeNode);
}
