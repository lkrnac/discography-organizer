package sk.lkrnac.discorg.view;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import sk.lkrnac.discorg.model.interfaces.IMediaIssue;

/**
 * Media issues comparator. Is used for sorting media issues table viewer  
 * @author sitko
 *
 */
public class MediaIssueComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;
	
	/**
	 * Creates media issue comparator object
	 */
	public MediaIssueComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	/**
	 * @return direction of the sorting
	 */
	public int getDirection() {
		return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	/**
	 * Sets the column of the table viewer comparator
	 * @param column sorting column index
	 */
	public void setColumn(int column) {
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
	public int compare(Viewer viewer, Object e1, Object e2) {
		IMediaIssue p1 = (IMediaIssue) e1;
		IMediaIssue p2 = (IMediaIssue) e2;
		int rc = 0;
		switch (propertyIndex) {
		case 0:
			if (p1.isError() == p2.isError()) {
				rc = 0;
			} else
				rc = (p1.isError() ? 1 : -1);
			break;
		case 1:
			rc = p1.getIssueCode().compareTo(p2.getIssueCode());
			break;
		case 2:
			rc = p1.getRelativePath().compareTo(p2.getRelativePath());
			break;
		default:
			rc = 0;
		}
		// If descending order, flip the direction
		if (direction == DESCENDING) {
			rc = -rc;
		}
		return rc;
	}
}
