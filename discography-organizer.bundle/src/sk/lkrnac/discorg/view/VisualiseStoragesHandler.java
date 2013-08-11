package sk.lkrnac.discorg.view;

import java.util.Iterator;

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
		TreeStorageView referenceStorageView = getTreeView(ReferenceStorageView.ID);
		referenceStorageView.visualizeStorageNode(referenceRootNode);

		// visualize input storage view
		TreeStorageView inputStorageView = getTreeView(InputStorageView.ID);
		inputStorageView.visualizeStorageNode(inputRootNode);

		// visualize media issues view
		MediaIssuesView issuesView = getTreeView(MediaIssuesView.ID);
		issuesView.visualiseIssues(issuesIterator);
	}

	/**
	 * Return view instance based on view Id.
	 * 
	 * @param viewId
	 *            ID of the view
	 * @return view instance
	 */
	static <T> T getTreeView(String viewId) {
		IViewReference referenceStorageViewRef = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findViewReference(viewId);
		@SuppressWarnings("unchecked")
		T referenceStorageView = (T) referenceStorageViewRef.getView(true);
		return referenceStorageView;
	}
}
