package sk.lkrnac.discorg.view.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import sk.lkrnac.discorg.Activator;
import sk.lkrnac.discorg.general.constants.StoragesPreferenceIds;
import sk.lkrnac.discorg.view.messages.SrotagesPreferenceMessages;

/**
 * Preference page, where media storages can be configured.
 * 
 * @author sitko
 */
public class StoragesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
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
	protected final void createFieldEditors() {
		// Create the field editors
		addField(new DirectoryFieldEditor(StoragesPreferenceIds.REFERENCE_STORAGE, SrotagesPreferenceMessages.referenceStorage,
				getFieldEditorParent()));
		addField(new DirectoryFieldEditor(StoragesPreferenceIds.INPUT_STORAGE, SrotagesPreferenceMessages.inputStorage,
				getFieldEditorParent()));
		addField(new DirectoryFieldEditor(StoragesPreferenceIds.OUTPUT_STORAGE_LOSSY, SrotagesPreferenceMessages.outputStorageLossy,
				getFieldEditorParent()));
		addField(new DirectoryFieldEditor(StoragesPreferenceIds.OUTPUT_STORAGE_LOSSLESS,
				SrotagesPreferenceMessages.outputStorageLossless, getFieldEditorParent()));
		addField(new StringFieldEditor(StoragesPreferenceIds.FULL_SUB_STORAGE,
				SrotagesPreferenceMessages.fullAlbumSubdirectoryName, getFieldEditorParent()));
	}

	/**
	 * Initialize the preference page.
	 * 
	 * @param workbench
	 *            application workbench interface
	 */
	public final void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

}
