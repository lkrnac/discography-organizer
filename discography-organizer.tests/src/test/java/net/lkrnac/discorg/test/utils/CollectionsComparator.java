package net.lkrnac.discorg.test.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.comparators.NullComparator;

/**
 * Test utility class for comparing collections
 * 
 * @author lubos krnac
 * 
 */
public final class CollectionsComparator {
	/**
	 * Avoid instantiation
	 */
	private CollectionsComparator() {
	};

	/**
	 * Utility method for comparing collections based on elements.
	 * 
	 * @param collection1
	 *            collection1 to compare
	 * @param collection2
	 *            collection2 to compare
	 * @param comparator
	 *            elements comparator
	 * @param sort
	 *            flag if sorting of both collections is needed
	 * @return result of comparison (similar to
	 *         {@link Comparator#compare(Object, Object)} result)
	 */
	public static <T> int compare(Collection<T> collection1, Collection<T> collection2,
			Comparator<T> comparator, boolean sort) {
		int result = 1;
		if (collection1 == null || collection2 == null) {
			result = new NullComparator<Collection<T>>().compare(collection1, collection2);
		}
		if (collection1.size() != collection2.size()) {
			result = collection1.size() - collection2.size();
		}

		List<T> list1 = new ArrayList<>(collection1);
		List<T> list2 = new ArrayList<>(collection2);
		if (sort) {
			Collections.sort(list1, comparator);
			Collections.sort(list2, comparator);
		}

		for (int idx = 0; idx < collection1.size(); idx++) {
			result = comparator.compare(list1.get(idx), list1.get(idx));
			if (result != 0) {
				break;
			}
		}
		return result;
	}
}
