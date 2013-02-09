package sk.lkrnac.discorg.perspectives;


import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import sk.lkrnac.discorg.view.InputStorageView;
import sk.lkrnac.discorg.view.MediaIssuesView;
import sk.lkrnac.discorg.view.ReferenceStorageView;


/**
 * Perspective for comparing reference and input media storages
 * @author sitko
 *
 */
public class TreePairPerspective implements IPerspectiveFactory {

	/**
	 * Creates the initial layout for a page.
	 */
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView(MediaIssuesView.ID, IPageLayout.BOTTOM, 0.5f, IPageLayout.ID_EDITOR_AREA);
		layout.addView(ReferenceStorageView.ID, IPageLayout.TOP, 0.71f, MediaIssuesView.ID);
		layout.addView(InputStorageView.ID, IPageLayout.RIGHT, 0.5f, ReferenceStorageView.ID);
		layout.getViewLayout(MediaIssuesView.ID).setCloseable(false);
		layout.getViewLayout(ReferenceStorageView.ID).setCloseable(false);
		layout.getViewLayout(InputStorageView.ID).setCloseable(false);
	}
}
