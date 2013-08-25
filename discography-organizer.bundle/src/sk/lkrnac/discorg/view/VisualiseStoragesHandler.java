package sk.lkrnac.discorg.view;

import java.util.Iterator;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.springframework.stereotype.Controller;

import sk.lkrnac.discorg.model.interfaces.IMediaIssue;
import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;
import sk.lkrnac.discorg.model.listeners.IVisualizeStoragesListener;

/**
 * Used for visualization of tree media storages.
 * 
 * @author sitko
 * 
 */
@Controller
public class VisualiseStoragesHandler implements IVisualizeStoragesListener {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void visualizeStorages(ITreeStorageNode referenceRootNode, ITreeStorageNode inputRootNode,
			Iterator<IMediaIssue> issuesIterator) {
		// visualize reference storage view
		AbstractTreeStorageView referenceStorageView = getTreeView(ReferenceStorageView.VIEW_ID);
		referenceStorageView.visualizeStorageNode(referenceRootNode);

		// visualize input storage view
		AbstractTreeStorageView inputStorageView = getTreeView(InputStorageView.VIEW_ID);
		inputStorageView.visualizeStorageNode(inputRootNode);

		// visualize media issues view
		MediaIssuesView issuesView = getTreeView(MediaIssuesView.VIEW_ID);
		issuesView.visualiseIssues(issuesIterator);
	}

	/**
	 * Return view instance based on view Id.
	 * 
	 * @param viewId
	 *            ID of the view
	 * @return view instance
	 * @param <T>
	 *            type of the view the method is looking for
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getTreeView(String viewId) {
		T referenceStorageView = null;
		IViewReference referenceStorageViewRef = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findViewReference(viewId);
		if (referenceStorageViewRef != null) {
			IViewPart view = referenceStorageViewRef.getView(true);
			referenceStorageView = (T) view;
		}
		return referenceStorageView;
	}
}
