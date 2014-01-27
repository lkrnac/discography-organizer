package net.lkrnac.discorg.model.preferences;

import net.lkrnac.discorg.Activator;
import net.lkrnac.discorg.general.constants.StoragesPreferenceIds;

import org.springframework.stereotype.Controller;

/**
 * Provides facade for retrieving application preferences concerning to
 * storages.
 * 
 * @author sitko
 */
@Controller
public class StoragesPreferences {
	/**
	 * @return Application preference - path for output lossy storage
	 */
	public String getOutputStorageLossy() {
		return Activator.getDefault().getPreferenceStore()
				.getString(StoragesPreferenceIds.OUTPUT_STORAGE_LOSSY);
	}

	/**
	 * @return Application preference - path for output loss-less storage
	 */
	public String getOutputStorageLossless() {
		return Activator.getDefault().getPreferenceStore()
				.getString(StoragesPreferenceIds.OUTPUT_STORAGE_LOSSLESS);
	}

	/**
	 * @return Application preference - path for input storage
	 */
	public String getInputStorage() {
		return Activator.getDefault().getPreferenceStore().getString(StoragesPreferenceIds.INPUT_STORAGE);
	}

	/**
	 * @return Application preference - path for reference storage
	 */
	public String getReferenceStorage() {
		return Activator.getDefault().getPreferenceStore().getString(StoragesPreferenceIds.REFERENCE_STORAGE);
	}

	/**
	 * @return Application preference - name of full album (media sub-directory)
	 */
	public String getFullSubDirectory() {
		return Activator.getDefault().getPreferenceStore().getString(StoragesPreferenceIds.FULL_SUB_STORAGE);
	}
}
