package sk.lkrnac.discorg.view;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TreeItem;

import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * Visualizes input storage tree.
 * 
 * @author sitko
 * 
 */
public class InputStorageView extends TreeStorageView {
	/** View ID. */
	public static final String ID = "discographyorganizer.views.InputStorageView"; //$NON-NLS-1$

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
			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof TreeItem) {
					selectAllMirrors(((ITreeStorageNode) e.item.getData()).getAbsolutePath(),
							ReferenceStorageView.ID);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
}
