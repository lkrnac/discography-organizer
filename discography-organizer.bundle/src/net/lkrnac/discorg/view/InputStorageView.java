package net.lkrnac.discorg.view;

import net.lkrnac.discorg.model.interfaces.ITreeStorageNode;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Visualizes input storage tree.
 * 
 * @author sitko
 * 
 */
public class InputStorageView extends AbstractTreeStorageView {
	/** View ID. */
	public static final String VIEW_ID = "discographyorganizer.views.InputStorageView"; //$NON-NLS-1$

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void processNode(ITreeStorageNode childNode) {
		// no processing needed on this view
	}

	/**
	 * Creates focus listener for tree on this view.
	 */
	@Override
	protected final void createSelectionListenerForTree() {
		// add focus listener
		this.getTree().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				if (event.item instanceof TreeItem) {
					selectAllMirrors(((ITreeStorageNode) event.item.getData()).getAbsolutePath(),
							ReferenceStorageView.VIEW_ID);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				// no action needed
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
