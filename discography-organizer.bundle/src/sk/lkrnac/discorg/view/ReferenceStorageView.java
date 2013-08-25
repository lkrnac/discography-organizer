package sk.lkrnac.discorg.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TreeItem;

import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * View for visualizing reference media storage.
 * 
 * @author sitko
 * 
 */
public class ReferenceStorageView extends AbstractTreeStorageView {
	/** View ID. */
	public static final String VIEW_ID = "discographyorganizer.views.ReferenceStorageView"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void processNode(ITreeStorageNode childNode) {
		//no action needed here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final void createSelectionListenerForTree() {
		this.getTree().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				// select mirror items on reference view
				if (event.item instanceof TreeItem) {
					ITreeStorageNode selectedNode = (ITreeStorageNode) event.item.getData();
					List<String> selectionIds = new ArrayList<String>(selectedNode.getMirrorsAbsolutePaths());
					selectionIds.add(selectedNode.getAbsolutePath());
					selectItems(selectionIds);

					InputStorageView inputStorage = VisualiseStoragesHandler
							.getTreeView(InputStorageView.VIEW_ID);
					inputStorage.selectItems(selectionIds);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				//no action needed
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createActions() {
		// no actions needs to be created for this view
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFocus() {
		// no action needed
	}

}
