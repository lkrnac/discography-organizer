package sk.lkrnac.discorg.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import sk.lkrnac.discorg.view.InputStorageView;
import sk.lkrnac.discorg.view.MediaIssuesView;
import sk.lkrnac.discorg.view.ReferenceStorageView;

/**
 * Perspective for comparing reference and input media storages.
 * 
 * @author sitko
 * 
 */
public class TreePairPerspective implements IPerspectiveFactory {
	private static final float INPUT_STORAGE_VIEW_RATIO = 0.5f;
	private static final float REFERENCE_STORAGE_VIEW_RATIO = 0.71f;
	private static final float MEDIA_ISSUES_VIEW_RATIO = 0.5f;

	/**
	 * Creates the initial layout for a page.
	 * 
	 * @param layout
	 *            perspective layout
	 */
	public final void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView(MediaIssuesView.VIEW_ID, IPageLayout.BOTTOM, MEDIA_ISSUES_VIEW_RATIO,
				IPageLayout.ID_EDITOR_AREA);
		layout.addView(ReferenceStorageView.VIEW_ID, IPageLayout.TOP, REFERENCE_STORAGE_VIEW_RATIO,
				MediaIssuesView.VIEW_ID);
		layout.addView(InputStorageView.VIEW_ID, IPageLayout.RIGHT, INPUT_STORAGE_VIEW_RATIO,
				ReferenceStorageView.VIEW_ID);
		layout.getViewLayout(MediaIssuesView.VIEW_ID).setCloseable(false);
		layout.getViewLayout(ReferenceStorageView.VIEW_ID).setCloseable(false);
		layout.getViewLayout(InputStorageView.VIEW_ID).setCloseable(false);
	}
}
