package sk.lkrnac.discorg.preferences;


import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import sk.lkrnac.discorg.Activator;

/**
 * Preference page, where media storages can be configured 
 * @author sitko
 */
public class StoragesPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	/** Lossy output storage preference ID */
	public static final String OUTPUT_STORAGE_LOSSY = "outputStorageLossy";
	/** Loss-less output storage preference ID */
	public static final String OUTPUT_STORAGE_LOSSLESS = "outputStorageLossless";
	/** Input storage preference ID */
	public static final String INPUT_STORAGE = "inputStorage";
	/** Reference storage preference ID */
	public static final String REFERENCE_STORAGE = "referenceStorage";
	/** Full sub-directory name preference ID */
	public static final String FULL_SUB_STORAGE = "fullSubDirectory";

	/**
	 * Create the preference page.
	 */
	public StoragesPreferencePage() {
		super(FLAT);
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		// Create the field editors
		addField(new DirectoryFieldEditor(REFERENCE_STORAGE, "Reference Storage", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(INPUT_STORAGE, "Input Storage", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(OUTPUT_STORAGE_LOSSY, "Output Storage lossy", getFieldEditorParent()));
		addField(new DirectoryFieldEditor(OUTPUT_STORAGE_LOSSLESS, "Output Storage lossless", getFieldEditorParent()));
		addField(new StringFieldEditor(FULL_SUB_STORAGE, "Full albums sub-directory name", getFieldEditorParent()));
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

}