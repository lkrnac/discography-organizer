package sk.lkrnac.discorg.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.ViewPart;

import sk.lkrnac.discorg.Activator;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * Abstract Class representing music files storage view. Contains recursive
 * algorithm for loading directory tree into {@link Tree} instance
 * 
 * @author sitko
 * 
 */
public abstract class TreeStorageView extends ViewPart {
	private static final String ICONS_DIR = "icons" + File.separator + //$NON-NLS-1$ 
			"status-small" + File.separator; //$NON-NLS-1$
	private static Map<String, Image> icons = new HashMap<String, Image>();

	/** map of tree items based on itemId. */
	private Map<String, TreeItem> treeItemsMap;

	/** tree object representing directory tree. */
	private Tree tree;

	/**
	 * @return tree object representing directory tree
	 */
	public final Tree getTree() {
		return tree;
	}

	/**
	 * Create contents of the view part.
	 * 
	 * @param parent
	 *            parent composite object
	 */
	@Override
	public final void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		tree = new Tree(container, SWT.BORDER | SWT.MULTI);
		treeItemsMap = new HashMap<String, TreeItem>();
		createSelectionListenerForTree();

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		@SuppressWarnings("unused")
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
		// Set the focus
	}

	/**
	 * Is used for creation of FocusListener.
	 */
	protected abstract void createSelectionListenerForTree();

	/**
	 * @return used or default display device
	 */
	private static Display getDisplay() {
		Display display = Display.getCurrent();
		// may be null if outside the UI thread
		if (display == null) {
			display = Display.getDefault();
		}
		return display;
	}

	/**
	 * Visualizes media storage on this view.
	 * 
	 * @param rootNode
	 *            - root storage node
	 */
	final void visualizeStorageNode(ITreeStorageNode rootNode) {
		treeItemsMap.clear();
		getTree().removeAll();
		visualizeStorageNode(getTree(), rootNode);
	}

	/**
	 * Returns icon image object. Create instance if such object wasn't created
	 * before or reuse old instance
	 * 
	 * @param iconName
	 *            - name of the icon
	 * @return image object for the icon
	 */
	private Image getIcon(String iconName) {
		Image oldIcon = icons.get(iconName);
		if (oldIcon != null) {
			return oldIcon;
		}

		ImageDescriptor d = Activator.getImageDescriptor(File.separator + ICONS_DIR + File.separator
				+ iconName);
		Image icon = null;
		if (d != null) {
			icon = d.createImage();
		} else {
			// TODO: handle error
			icon = new Image(getDisplay(), 1, 1);
		}
		icons.put(iconName, icon);
		return icon;
	}

	/**
	 * Creates tree view representation of a storage recursively.
	 * 
	 * @param parentWidget
	 *            - parent widget in the visualisation tree
	 * @param parentNode
	 *            - parent storage node
	 */
	private void visualizeStorageNode(Widget parentWidget, ITreeStorageNode parentNode) {
		while (parentNode.hasNextChild()) {
			ITreeStorageNode childNode = parentNode.getNextChild();
			TreeItem childTreeItem = (parentWidget instanceof Tree) ? new TreeItem((Tree) parentWidget,
					SWT.NULL) : new TreeItem((TreeItem) parentWidget, SWT.NULL);
			childTreeItem.setText(childNode.getName());
			childTreeItem.setImage(getIcon(childNode.getNodeStatus().getIconName()));
			childTreeItem.setData(childNode);
			processNode(childNode);
			if (childNode.isFullAlbum()) {
				FontData[] fontData = childTreeItem.getFont().getFontData();
				fontData[0].setStyle(SWT.ITALIC);
				childTreeItem.setFont(new Font(childTreeItem.getFont().getDevice(), fontData));
			}
			visualizeStorageNode(childTreeItem, childNode);

			// update map of tree items
			treeItemsMap.put(childNode.getAbsolutePath(), childTreeItem);
		}
	}

	/**
	 * Handle processing for node in subclasses.
	 * 
	 * @param childNode
	 *            child node in the tree view
	 */
	protected abstract void processNode(ITreeStorageNode childNode);

	/**
	 * Select tree items on the view based on item IDs.
	 * 
	 * @param mirrorIds
	 *            - list of item IDs to select
	 * @return number of selected items
	 */
	public final int selectItems(Collection<String> mirrorIds) {
		Collection<TreeItem> itemsForSelection = new ArrayList<TreeItem>();
		for (String mirrorId : mirrorIds) {
			TreeItem treeItem = treeItemsMap.get(mirrorId);
			if (treeItem != null) {
				itemsForSelection.add(treeItem);

				// select also full mirror of mirrors
				// to ensure that also full albums are selected
				ITreeStorageNode node = (ITreeStorageNode) treeItem.getData();
				for (String fullMirrorId : node.getMirrorsAbsolutePaths()) {
					treeItem = treeItemsMap.get(fullMirrorId);
					if (treeItem != null) {
						itemsForSelection.add(treeItem);
					}
				}
			}
		}
		tree.setSelection(itemsForSelection.toArray(new TreeItem[0]));
		return itemsForSelection.size();
	}

	/**
	 * Retrieve TreeItem object.
	 * 
	 * @param itemId
	 *            item ID
	 * @return tree item object for ID
	 */
	protected final TreeItem getItem(String itemId) {
		return treeItemsMap.get(itemId);
	}

	/**
	 * Is used for selecting source media node and its mirror items on this and
	 * mirror storage view.
	 * 
	 * @param sourceId
	 *            ID of the selection source tree item
	 * @param mirrorViewId
	 *            ID of the mirror tree item
	 */
	public final void selectAllMirrors(String sourceId, String mirrorViewId) {
		TreeItem source = this.getItem(sourceId);
		if (source != null && source.getData() != null && source.getData() instanceof ITreeStorageNode) {
			ITreeStorageNode mediaDir = (ITreeStorageNode) source.getData();

			// create list of items to select (include source item)
			Collection<String> mirrorIds = mediaDir.getMirrorsAbsolutePaths();
			mirrorIds.add(sourceId);

			// select mirrors on this view
			selectItems(mirrorIds);

			// find reference view instance
			TreeStorageView mirrorStorageView = VisualiseStoragesHandler.getTreeView(mirrorViewId);

			// select mirror items on reference view
			mirrorStorageView.selectItems(mirrorIds);
		}
	}

}
