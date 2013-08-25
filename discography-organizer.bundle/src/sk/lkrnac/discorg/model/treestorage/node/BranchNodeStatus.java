package sk.lkrnac.discorg.model.treestorage.node;

/**
 * Represents status node status of the media directory.
 * 
 * @author sitko
 * 
 */
public final class BranchNodeStatus extends NodeStatus {
	/** Status representing folder. */
	public static final BranchNodeStatus FOLDER = new BranchNodeStatus(10, "status-folder.png"); //$NON-NLS-1$
	// /** Status representing that lossy and loss-less data are presented in
	// comparing storage */
	// public static final BranchNodeStatus PLUS = new BranchNodeStatus (20
	// ,"status-plus.png" );
	/** Status representing that data in comparing storage are up to date. */
	public static final BranchNodeStatus OK = new BranchNodeStatus(30, "status-ok.png"); //NOPMD //$NON-NLS-1$
	/** Status representing that data in data are missing in comparing storage. */
	public static final BranchNodeStatus UPLOAD = new BranchNodeStatus(40, "status-upload.png"); //$NON-NLS-1$
	/** Status representing that data in comparing storage needs to be updated. */
	public static final BranchNodeStatus UPDATE = new BranchNodeStatus(45, "status-update.png"); //$NON-NLS-1$
	/** Status representing that folder is ignored. */
	public static final BranchNodeStatus IGNORED = new BranchNodeStatus(50, "status-ignored.png"); //$NON-NLS-1$
	/** Status representing that folder caused warning. */
	public static final BranchNodeStatus WARNING = new BranchNodeStatus(60, "status-warning.png"); //$NON-NLS-1$
	/** Status representing that folder caused error. */
	public static final BranchNodeStatus ERROR = new BranchNodeStatus(70, "status-error.png"); //$NON-NLS-1$

	/**
	 * Private constructor of branch node status class. This class acts nearly
	 * as enumeration.
	 * 
	 * @param priority
	 *            status priority
	 * @param iconName
	 *            name of the icon to visualize with status
	 */
	private BranchNodeStatus(int priority, String iconName) {
		super(priority, iconName);
	}
}
