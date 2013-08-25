package sk.lkrnac.discorg.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import sk.lkrnac.discorg.model.interfaces.IMediaIssue;
import sk.lkrnac.discorg.view.messages.MediaIssueMessages;
import sk.lkrnac.discorg.view.messages.MediaIssuesViewMessages;

/**
 * Visualizes media issues.
 * 
 * @author sitko
 * 
 */
public class MediaIssuesView extends ViewPart {
	private static final int COLUMN_MEDIA_DIRECTORY_NAME_WIDTH = 400;
	private static final int COLUMN_TEXT_WIDTH = 400;
	private static final int COLUMN_TYPE_WIDTH = 100;
	private static final String COLUMN_MEDIA_DIRECTORY_NAME = MediaIssuesViewMessages.mediaDirectoryNameColumn;
	private static final String COLUMN_TEXT = MediaIssuesViewMessages.textColumn;
	private static final String COLUMN_TYPE = MediaIssuesViewMessages.typeColumn;
	/** View ID. */
	public static final String VIEW_ID = "discographyorganizer.views.MediaIssuesView"; //$NON-NLS-1$

	private TableViewer viewer;
	private MediaIssueComparator comparator;

	/**
	 * Create table where media issues will be visualized.
	 * 
	 * @param parent
	 *            parent GUI container
	 */
	public final void createPartControl(Composite parent) {
		// createSwtTable(parent);
		createTableViewer(parent);
	}

	/**
	 * Creates table for viewing media issues.
	 * 
	 * @param parent
	 *            parent UI container
	 */
	private void createTableViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.BORDER);
		createColumns();
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createSelectionListenerForTable();

		viewer.setContentProvider(new ArrayContentProvider());
		// Make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// Set the sorter for the table
		comparator = new MediaIssueComparator();
		viewer.setComparator(comparator);

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	/**
	 * This will create columns for the table.
	 */
	private void createColumns() {
		String[] titles = { COLUMN_TYPE, COLUMN_TEXT, COLUMN_MEDIA_DIRECTORY_NAME };
		int[] bounds = { COLUMN_TYPE_WIDTH, COLUMN_TEXT_WIDTH, COLUMN_MEDIA_DIRECTORY_NAME_WIDTH };

		// First column is for the first name
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMediaIssue issue = (IMediaIssue) element;
				return issue.isError() ? MediaIssuesViewMessages.errorTranslation : MediaIssuesViewMessages.warningTranslation;
			}
		});

		// Second column is for the last name
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMediaIssue issue = (IMediaIssue) element;
				String errorMessage = (issue.getErrorMessage() == null) ? "" : ": " + //$NON-NLS-1$ //$NON-NLS-2$ 
						issue.getErrorMessage();
				return MediaIssueMessages.getMessageForMessageCode(issue.getIssueCode()) + errorMessage;
			}
		});

		// Now the gender
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMediaIssue issue = (IMediaIssue) element;
				return issue.getRelativePath();
			}
		});
	}

	/**
	 * Creates viewer table column.
	 * 
	 * @param title
	 *            title of the column
	 * @param bound
	 *            bound of the column
	 * @param colNumber
	 *            column number
	 * @return created table viewer column
	 */
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		column.addSelectionListener(getSelectionAdapter(column, colNumber));
		return viewerColumn;
	}

	/**
	 * Returns adapter that drives sorting of media issues based on user
	 * actions.
	 * 
	 * @param column
	 *            name of the column
	 * @param index
	 *            index of the column
	 * @return selection adapter
	 */
	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setFocus() {
		viewer.getControl().setFocus();
	}

	/**
	 * Visualize issues in view table.
	 * 
	 * @param issuesIterator
	 *            media issues iterator
	 */
	public final void visualiseIssues(Iterator<IMediaIssue> issuesIterator) {
		List<IMediaIssue> mediaIssues = new ArrayList<IMediaIssue>();
		while (issuesIterator.hasNext()) {
			mediaIssues.add(issuesIterator.next());
		}
		viewer.setInput(mediaIssues);
	}

	/**
	 * Creates table selection listener.<br>
	 * This listener selects source of issue on other views
	 */
	private void createSelectionListenerForTable() {
		this.viewer.getTable().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				if (event.item instanceof TableItem) {
					IMediaIssue issue = (IMediaIssue) event.item.getData();

					// select issue source in Input storage
					InputStorageView inputStorageView = VisualiseStoragesHandler
							.getTreeView(InputStorageView.VIEW_ID);
					inputStorageView.selectAllMirrors(issue.getSourceAbsolutePath(), ReferenceStorageView.VIEW_ID);

					// select issue source in Reference storage
					ReferenceStorageView referenceStorageView = VisualiseStoragesHandler
							.getTreeView(ReferenceStorageView.VIEW_ID);
					referenceStorageView.selectAllMirrors(issue.getSourceAbsolutePath(),
							InputStorageView.VIEW_ID);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
				//no action needed
			}
		});
	}
}
