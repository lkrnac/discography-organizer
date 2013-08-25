package sk.lkrnac.discorg.preferences;

import java.util.ArrayList;
import java.util.List;

import sk.lkrnac.discorg.Activator;

/**
 * Abstract super class for preferences holder.
 * <p>
 * Implements functionality that enables parsing preference values based on
 * delimiters
 * 
 * @author sitko
 */
abstract class AbstractPreferences {
	/**
	 * Parses preference value based on delimiters.
	 * 
	 * @param preferenceString
	 *            preference value string
	 * @return list of parsed preferences
	 */
	protected List<String> parsePreferenceString(String preferenceString) {
		String preferencesString = Activator.getDefault().getPreferenceStore().getString(preferenceString);
		preferencesString = preferencesString.replaceAll("\\s", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String[] preferences = preferencesString.split(getDelimiters());
		List<String> preferencesList = new ArrayList<String>(preferences.length);
		for (String preference : preferences) {
			preferencesList.add(preference);
		}
		return preferencesList;
	}

	/**
	 * Specifies delimiters used to parse preference string.
	 * 
	 * @return possible delimiters for preference string
	 */
	protected abstract String getDelimiters();
}
