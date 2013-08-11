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

/**
 * Visualizes media issues.
 * 
 * @author sitko
 * 
 */
public class MediaIssuesView extends ViewPart {
	private static final String COLUMN_MEDIA_DIRECTORY_NAME = "Media directory name";
	private static final String COLUMN_TEXT = "Text";
	private static final String COLUMN_TYPE = "Type";
	/** View ID. */
	public static final String ID = "discographyorganizer.views.MediaIssuesView"; //$NON-NLS-1$

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
		createColumns(viewer);
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
	 * 
	 * @param viewer
	 *            table viewer object
	 */
	private void createColumns(final TableViewer viewer) {
		String[] titles = { COLUMN_TYPE, COLUMN_TEXT, COLUMN_MEDIA_DIRECTORY_NAME };
		int[] bounds = { 100, 400, 400 };

		// First column is for the first name
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMediaIssue i = (IMediaIssue) element;
				return (i.isError()) ? "ERROR" : "WARNING";
			}
		});

		// Second column is for the last name
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMediaIssue i = (IMediaIssue) element;
				String errorMessage = (i.getErrorMessage() == null) ? "" : ": " + i.getErrorMessage();
				return MediaIssueMessages.getMessageForMessageCode(i.getIssueCode()) + errorMessage;
			}
		});

		// Now the gender
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMediaIssue i = (IMediaIssue) element;
				return i.getRelativePath();
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
			public void widgetSelected(SelectionEvent e) {
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
		for (; issuesIterator.hasNext();) {
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
			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof TableItem) {
					IMediaIssue issue = (IMediaIssue) e.item.getData();

					// select issue source in Input storage
					InputStorageView inputStorageView = VisualiseStoragesHandler
							.getTreeView(InputStorageView.ID);
					inputStorageView.selectAllMirrors(issue.getSourceAbsolutePath(), ReferenceStorageView.ID);

					// select issue source in Reference storage
					ReferenceStorageView referenceStorageView = VisualiseStoragesHandler
							.getTreeView(ReferenceStorageView.ID);
					referenceStorageView.selectAllMirrors(issue.getSourceAbsolutePath(), InputStorageView.ID);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
}
