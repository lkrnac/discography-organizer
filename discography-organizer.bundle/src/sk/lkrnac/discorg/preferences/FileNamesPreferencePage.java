package sk.lkrnac.discorg.preferences;


import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import sk.lkrnac.discorg.Activator;


/**
 * Preference page, where file names preferences can be configured
 * @author sitko
 */
public class FileNamesPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {
	/** Regular expressions to ignore preference ID */	
	static final String REGEX_IGNORE_PATTERN = "REGEX_IGNORE_PATTERN";
	/** Characters to ignore preference ID */
	static final String CHARS_IGNORE_PATTERN = "CHARS_IGNORE_PATTERN";
	
	/**
	 * Creates preference page instance
	 */
	public FileNamesPreferencePage(){
		super(FLAT);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());	
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(CHARS_IGNORE_PATTERN, "Ignore characters", getFieldEditorParent()));
		addField(new StringFieldEditor(REGEX_IGNORE_PATTERN, "Ignore regular expressions (separeted by '" + 
				FileNamesPreferences.FILE_NAMES_DELIMITER + "')", getFieldEditorParent()));
	}

}
