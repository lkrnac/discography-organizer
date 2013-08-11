package sk.lkrnac.discorg.model.treestorage;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;
import sk.lkrnac.discorg.model.treestorage.node.FileDesignator;
import sk.lkrnac.discorg.model.treestorage.node.MediaBranchNode;
import sk.lkrnac.discorg.model.treestorage.node.TreeStorageBranchNode;
import sk.lkrnac.discorg.model.treestorage.node.TreeStorageNode;

/**
 * Abstract storage of tree elements.
 * <p>
 * It reflects directory with media files
 * 
 * @author sitko
 */
public abstract class TreeStorage {
	private TreeStorageBranchNode rootNode;

	@Autowired
	private FileDesignator fileDesignator;

	/**
	 * @return file designator object<br>
	 *         it designates files and directories types based on file name
	 */
	public final FileDesignator getFileDesignator() {
		return fileDesignator;
	}

	/**
	 * @return root node of tree storage
	 */
	public final TreeStorageBranchNode getRootNode() {
		return rootNode;
	}

	/**
	 * Clear old meta data and reload storage from disk.
	 */
	public final void loadStorage() {
		// if this is a first load -> create root node
		if (getRootNode() == null) {
			rootNode = new TreeStorageBranchNode(null, null, null);
		}

		// clear previous load and load again
		getRootNode().clearAllChildren();
		if (!"".equals(getStoragePath())) {
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
	 * {@link TreeStorage#processNodePostLoad(ITreeStorageNode)}
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
		loadSubNode(rootDir, rootNode, "");
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
				if (getFileDesignator().isMediaDir(childFile)) {
					// create media directory
					childNode = createMediaNode(parentNode, childFile, relativePath);

					loadNode(childNode);
				} else {
					childNode = new TreeStorageBranchNode(parentNode, childFile, relativePath);
				}

				// recursively read sub items
				loadSubNode(childFile, (TreeStorageBranchNode) childNode, childRelativePath);

				// adjust parent status
				parentNode.setNodeStatus(childNode.getNodeStatus());
			} else {
				childNode = new TreeStorageNode(parentNode, childFile);
			}
		}
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
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
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
