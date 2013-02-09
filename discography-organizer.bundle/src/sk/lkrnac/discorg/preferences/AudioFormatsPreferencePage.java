package sk.lkrnac.discorg.preferences;


import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import sk.lkrnac.discorg.Activator;

/**
 * Preference page, where audio formats can be configured
 * @author sitko
 */
public class AudioFormatsPreferencePage extends FieldEditorPreferencePage 
	implements IWorkbenchPreferencePage {
	
	/** Lossy audio formats preference ID */
	public static final String LOSSY_FORMATS = "lossyFormats";
	/** Loss-less audio formats preference ID */
	public static final String LOSSLESS_FORMATS = "losslessFormats";
	/** Warning audio formats preference ID */
	public static final String WARNING_FORMATS = "warningFormats";
	/** Empty directory flag preference ID */
	public static final String WARNING_EMPTY_FLAG = "warningEmptyFlag";
	
	/**
	 * Create the preference page.
	 */
	public AudioFormatsPreferencePage() {
		super(FLAT);
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		// Create the field editors
		addField(new StringFieldEditor(LOSSY_FORMATS, "Lossy audio formats", getFieldEditorParent()));
		addField(new StringFieldEditor(LOSSLESS_FORMATS, "Lossless audio formats", getFieldEditorParent()));
		addField(new StringFieldEditor(WARNING_FORMATS, "Warning audio formats", getFieldEditorParent()));
		addField(new BooleanFieldEditor(WARNING_EMPTY_FLAG, "Warning if empty directory", getFieldEditorParent()));
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	
}
