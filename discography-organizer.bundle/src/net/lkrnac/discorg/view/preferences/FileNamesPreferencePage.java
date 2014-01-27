package net.lkrnac.discorg.view.preferences;

import net.lkrnac.discorg.Activator;
import net.lkrnac.discorg.general.constants.FileNamesPreferenceIds;
import net.lkrnac.discorg.model.preferences.FileNamesPreferences;
import net.lkrnac.discorg.view.messages.FileNamesPreferenceMessages;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

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
