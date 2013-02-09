package sk.lkrnac.discorg.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TreeItem;

import sk.lkrnac.discorg.model.interfaces.ITreeStorageNode;

/**
 * View for visualizing reference media storage  
 * @author sitko
 *
 */
public class ReferenceStorageView extends TreeStorageView{
	/** View ID */
	public static final String ID = "discographyorganizer.views.ReferenceStorageView";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void processNode(ITreeStorageNode childNode) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createSelectionListenerForTree() {
		this.getTree().addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				//select mirror items on reference view
				if (e.item instanceof TreeItem) {
					ITreeStorageNode selectedNode = (ITreeStorageNode) e.item.getData();
					List<String> selectionIds = new ArrayList<String> (selectedNode.getMirrorsAbsolutePaths());
					selectionIds.add(selectedNode.getAbsolutePath());
					selectItems(selectionIds);
					
					InputStorageView inputStorage = VisualiseStoragesHandler
							.getTreeView(InputStorageView.ID);
					inputStorage.selectItems(selectionIds);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	
}

