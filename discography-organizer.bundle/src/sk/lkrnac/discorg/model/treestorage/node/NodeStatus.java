package sk.lkrnac.discorg.model.treestorage.node;

import sk.lkrnac.discorg.model.interfaces.INodeStatus;

/**
 * Represents generic status of the node.<br>
 * Indicates if the folder contains some media files
 * 
 * @author sitko
 */
public class NodeStatus implements INodeStatus {
	/** Folder does not contain media files. */
	public static final NodeStatus NONE = new NodeStatus(0, "status-file.png");
	/** Folder contains lossy media files. */
	public static final NodeStatus LOSSY = new NodeStatus(0, "status-lossy.png");
	/** Folder contains loss-less. */
	public static final NodeStatus LOSSLESS = new NodeStatus(0, "status-lossless.png");

	private final String iconName;
	private final int severity;

	/**
	 * Creates instance of node status.
	 * 
	 * @param severity
	 *            severity of the status (status with higher severity will be
	 *            populated to parent nodes)
	 * @param iconName
	 *            name of the icon that
	 */
	protected NodeStatus(int severity, String iconName) {
		this.severity = severity;
		this.iconName = iconName;
	}

	/**
	 * {@inheritDoc}
	 */
	public final int getSeverity() {
		return severity;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getIconName() {
		return iconName;
	}

	@Override
	public final String toString() {
		return "NodeStatus [iconName=" + iconName + ", severity=" + severity + "]";
	}
}
