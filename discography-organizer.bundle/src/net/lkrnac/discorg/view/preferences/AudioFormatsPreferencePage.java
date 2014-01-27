package net.lkrnac.discorg.view.preferences;

import net.lkrnac.discorg.Activator;
import net.lkrnac.discorg.general.constants.AudioFormatsPreferenceIds;
import net.lkrnac.discorg.view.messages.AudiFormatsPreferenceMessages;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Preference page, where audio formats can be configured.
 * 
 * @author sitko
 */
public class AudioFormatsPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
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
	protected final void createFieldEditors() {
		// Create the field editors
		addField(new StringFieldEditor(AudioFormatsPreferenceIds.LOSSY_FORMATS,
				AudiFormatsPreferenceMessages.lossyFormats, getFieldEditorParent()));
		addField(new StringFieldEditor(AudioFormatsPreferenceIds.LOSSLESS_FORMATS,
				AudiFormatsPreferenceMessages.losslessFormats, getFieldEditorParent()));
		addField(new StringFieldEditor(AudioFormatsPreferenceIds.WARNING_FORMATS,
				AudiFormatsPreferenceMessages.warningFormats, getFieldEditorParent()));
		addField(new BooleanFieldEditor(AudioFormatsPreferenceIds.WARNING_EMPTY_FLAG,
				AudiFormatsPreferenceMessages.warningEmptyFlag, getFieldEditorParent()));
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
