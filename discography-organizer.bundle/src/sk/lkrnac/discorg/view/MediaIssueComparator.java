package sk.lkrnac.discorg.view;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import sk.lkrnac.discorg.model.interfaces.IMediaIssue;

/**
 * Media issues comparator. Is used for sorting media issues table viewer
 * 
 * @author sitko
 * 
 */
public class MediaIssueComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	/**
	 * Creates media issue comparator object.
	 */
	public MediaIssueComparator() {
		super();
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	/**
	 * @return direction of the sorting
	 */
	public final int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	/**
	 * Sets the column of the table viewer comparator.
	 * 
	 * @param column
	 *            sorting column index
	 */
	public final void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}

	@Override
	public final int compare(Viewer viewer, Object issueObject1, Object issueObject2) {
		IMediaIssue issue1 = (IMediaIssue) issueObject1;
		IMediaIssue issue2 = (IMediaIssue) issueObject2;
		int result = 0;
		switch (propertyIndex) {
		case 0:
			if (issue1.isError() == issue2.isError()) {
				result = 0;
			} else {
				result = (issue1.isError() ? 1 : -1);
			}
			break;
		case 1:
			result = issue1.getIssueCode().compareTo(issue2.getIssueCode());
			break;
		case 2:
			result = issue1.getRelativePath().compareTo(issue2.getRelativePath());
			break;
		default:
			result = 0;
		}
		// If descending order, flip the direction
		if (direction == DESCENDING) {
			result = -result;
		}
		return result;
	}
}
