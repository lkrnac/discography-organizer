package sk.lkrnac.discorg.preferences;

import java.util.List;

import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.Activator;

/**
 * Component that holds file names preferences.
 * 
 * @author sitko
 */
@Service
public class FileNamesPreferences extends AbstractPreferences {
	/** Delimiters used for preference value parsing. */
	public static final String FILE_NAMES_DELIMITER = "\\?"; //$NON-NLS-1$

	private String ignoreCharacters;
	private List<String> ignoreRegexes;

	/**
	 * Creates instance of file names preferences holder.
	 */
	public FileNamesPreferences() {
		super();
		readFileNamesPreferences();
	}

	/**
	 * Reads file name preferences from preference page.
	 */
	public final void readFileNamesPreferences() {
		ignoreRegexes = parsePreferenceString(FileNamesPreferencePage.REGEX_IGNORE_PATTERN);
		ignoreCharacters = Activator.getDefault().getPreferenceStore()
				.getString(FileNamesPreferencePage.CHARS_IGNORE_PATTERN);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final String getDelimiters() {
		return FILE_NAMES_DELIMITER;
	}

	/**
	 * @return characters that should be ignored in path
	 */
	private String getIgnoreChars() {
		return ignoreCharacters;
	}

	/**
	 * @return regular expressions that should be ignored in path
	 */
	private List<String> getIgnoreRegexes() {
		return ignoreRegexes;
	}

	/**
	 * Removes ignored characters and regular expressions from given path.
	 * 
	 * @param path
	 *            original path
	 * @return path without ignored characters and regular expressions
	 */
	public final String getPathWithoutIgnoredParts(String path) {
		String changedPath = path;

		// delete ignoring regular expressions from name
		for (String regex : this.getIgnoreRegexes()) {
			changedPath = changedPath.replaceAll(regex, ""); //$NON-NLS-1$
		}

		changedPath = changedPath.replaceAll("\\s", ""); //$NON-NLS-1$ //$NON-NLS-2$
		// delete ignoring regular characters from name
		String ignoreChars = this.getIgnoreChars();
		for (int i = 0; i < ignoreChars.length(); i++) {
			changedPath = changedPath.replaceAll(String.valueOf(ignoreChars.charAt(i)), ""); //$NON-NLS-1$
		}

		return changedPath;
	}

}
