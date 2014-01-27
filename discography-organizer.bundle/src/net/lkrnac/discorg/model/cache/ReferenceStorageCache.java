package net.lkrnac.discorg.model.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.lkrnac.discorg.model.preferences.FileNamesPreferences;
import net.lkrnac.discorg.model.treestorage.node.MediaBranchNode;
import net.lkrnac.discorg.model.treestorage.node.NodeStatus;
import net.lkrnac.discorg.model.treestorage.node.ReferenceMediaNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Singleton that holds reference storage maps.
 * 
 * @author sitko
 */
@Service
public class ReferenceStorageCache {
	@Autowired
	private FileNamesPreferences fileNamesPreferences;

	private Map<String, Collection<ReferenceMediaNode>> normalizedReferenceLosslessItems;
	private Map<String, Collection<ReferenceMediaNode>> normalizedReferenceLossyItems;

	private Map<String, ReferenceMediaNode> absolutePathReferenceItems;

	/** This map holds media items that should be updated on reference storage. */
	private final Map<String, MediaBranchNode> itemsForUpdate;

	/**
	 * Creates instance of storage meta-data holder.
	 */
	public ReferenceStorageCache() {
		normalizedReferenceLossyItems = new HashMap<String, Collection<ReferenceMediaNode>>();
		normalizedReferenceLosslessItems = new HashMap<String, Collection<ReferenceMediaNode>>();
		itemsForUpdate = new HashMap<String, MediaBranchNode>();
	}

	/**
	 * Stores reference media directory into meta data for processing.<br>
	 * 
	 * @param referenceStorageNode
	 *            - item to be stored for processing
	 */
	public void putReferenceItem(ReferenceMediaNode referenceStorageNode) {
		String path = fileNamesPreferences.getPathWithoutIgnoredParts(referenceStorageNode.getRelativePath());
		// put node into normalized maps
		if (NodeStatus.LOSSY.equals(referenceStorageNode.getAudioFormatType())) {
			addNodeIntoReferenceStorageCache(normalizedReferenceLossyItems, path, referenceStorageNode);
		} else if (NodeStatus.LOSSLESS.equals(referenceStorageNode.getAudioFormatType())) {
			addNodeIntoReferenceStorageCache(normalizedReferenceLosslessItems, path, referenceStorageNode);
		}

		// put item into absolute path map
		absolutePathReferenceItems.put(referenceStorageNode.getAbsolutePath(), referenceStorageNode);
	}

	/**
	 * Add reference storage node into meta-data maps based on path key.
	 * 
	 * @param referenceLossyItems
	 *            meta-data map where reference storage will be stored
	 * @param path
	 *            key in meta-data map
	 * @param referenceStorageNode
	 *            node to be added
	 */
	private void addNodeIntoReferenceStorageCache(
			Map<String, Collection<ReferenceMediaNode>> referenceLossyItems, String path,
			ReferenceMediaNode referenceStorageNode) {
		Collection<ReferenceMediaNode> nodes = referenceLossyItems.get(path);
		if (nodes == null) {
			nodes = new ArrayList<ReferenceMediaNode>();
			referenceLossyItems.put(path, nodes);
		}
		nodes.add(referenceStorageNode);
	}

	/**
	 * Returns reference lossy media nodes based on relative path.
	 * 
	 * @param relativePath
	 *            relative path in the reference storage
	 * @return collection of reference lossy media nodes belonging to the
	 *         relative path
	 */
	public Collection<ReferenceMediaNode> getReferenceLossyItems(String relativePath) {
		String changedPath = fileNamesPreferences.getPathWithoutIgnoredParts(relativePath);
		return new ArrayList<ReferenceMediaNode>(normalizedReferenceLossyItems.get(changedPath));
	}

	/**
	 * Returns reference loss-less media nodes based on relative path.
	 * 
	 * @param relativePath
	 *            relative path in the reference storage
	 * @return collection of reference loss-less media nodes belonging to the
	 *         relative path
	 */
	public Collection<ReferenceMediaNode> getReferenceLosslessItems(String relativePath) {
		String changedPath = fileNamesPreferences.getPathWithoutIgnoredParts(relativePath);
		return new ArrayList<ReferenceMediaNode>(normalizedReferenceLosslessItems.get(changedPath));
	}

	/**
	 * Returns reference media nodes based on relative path.
	 * 
	 * @param relativePath
	 *            relative path in the reference storage
	 * @return collection of reference media nodes belonging to the relative
	 *         path
	 */
	public Collection<ReferenceMediaNode> getReferenceItems(String relativePath) {
		String changedPath = fileNamesPreferences.getPathWithoutIgnoredParts(relativePath);
		ArrayList<ReferenceMediaNode> retVal = new ArrayList<ReferenceMediaNode>();
		if (normalizedReferenceLosslessItems.get(changedPath) != null) {
			retVal.addAll(normalizedReferenceLosslessItems.get(changedPath));
		}
		if (normalizedReferenceLossyItems.get(changedPath) != null) {
			retVal.addAll(normalizedReferenceLossyItems.get(changedPath));
		}
		return retVal;
	}

	/**
	 * Stores item for update into meta-data.
	 * 
	 * @param mediaDir
	 *            tree item for update
	 */
	public void putItemForUpdate(MediaBranchNode mediaDir) {
		String path = fileNamesPreferences.getPathWithoutIgnoredParts(mediaDir.getRelativePath());
		itemsForUpdate.put(path, mediaDir);
	}

	/**
	 * Returns and removes next item for update from meta-data.
	 * 
	 * @return next item for update
	 */
	public MediaBranchNode getAndRemoveNextItemForUpdate() {
		MediaBranchNode item = null;
		if (!itemsForUpdate.isEmpty()) {
			String itemKey = itemsForUpdate.keySet().iterator().next();
			item = itemsForUpdate.get(itemKey);
			itemsForUpdate.remove(itemKey);
		}
		return item;
	}

	/**
	 * Returns referenceMediaNode for absolute path.
	 * 
	 * @param absolutePath
	 *            absolute path of of retrieving node
	 * @return reference media node belonging to the path
	 */
	public ReferenceMediaNode getReferenceMediaNode(String absolutePath) {
		return absolutePathReferenceItems.get(absolutePath);
	}

	/**
	 * Remove all reference storage meta-data.
	 */
	public void removeAll() {
		normalizedReferenceLossyItems = new HashMap<String, Collection<ReferenceMediaNode>>();
		normalizedReferenceLosslessItems = new HashMap<String, Collection<ReferenceMediaNode>>();
		absolutePathReferenceItems = new HashMap<String, ReferenceMediaNode>();
	}
}
