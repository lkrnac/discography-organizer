package sk.lkrnac.discorg.view.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import sk.lkrnac.discorg.Activator;
import sk.lkrnac.discorg.general.constants.FileNamesPreferenceIds;
import sk.lkrnac.discorg.model.preferences.FileNamesPreferences;
import sk.lkrnac.discorg.view.messages.FileNamesPreferenceMessages;

/**
 * Preference page, where file names preferences can be configured.
 * 
 * @author sitko
 */
public class FileNamesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	/**
	 * Creates preference page instance.
	 */
	public FileNamesPreferencePage() {
		super(FLAT);
	}

	@Override
	public final void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected final void createFieldEditors() {
		addField(new StringFieldEditor(FileNamesPreferenceIds.CHARS_IGNORE_PATTERN,
				FileNamesPreferenceMessages.charsIgnorePattern, getFieldEditorParent()));
		addField(new StringFieldEditor(FileNamesPreferenceIds.REGEX_IGNORE_PATTERN,
				FileNamesPreferenceMessages.regexIgnorePattern + FileNamesPreferences.FILE_NAMES_DELIMITER
						+ "')", getFieldEditorParent())); //$NON-NLS-1$
	}

}
