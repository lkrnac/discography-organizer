package sk.lkrnac.discorg.model.preferences;

import org.springframework.stereotype.Controller;

import sk.lkrnac.discorg.Activator;
import sk.lkrnac.discorg.general.constants.StoragesPreferenceIds;

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
	public final String getOutputStorageLossy() {
		return Activator.getDefault().getPreferenceStore()
				.getString(StoragesPreferenceIds.OUTPUT_STORAGE_LOSSY);
	}

	/**
	 * @return Application preference - path for output loss-less storage
	 */
	public final String getOutputStorageLossless() {
		return Activator.getDefault().getPreferenceStore()
				.getString(StoragesPreferenceIds.OUTPUT_STORAGE_LOSSLESS);
	}

	/**
	 * @return Application preference - path for input storage
	 */
	public final String getInputStorage() {
		return Activator.getDefault().getPreferenceStore().getString(StoragesPreferenceIds.INPUT_STORAGE);
	}

	/**
	 * @return Application preference - path for reference storage
	 */
	public final String getReferenceStorage() {
		return Activator.getDefault().getPreferenceStore().getString(StoragesPreferenceIds.REFERENCE_STORAGE);
	}

	/**
	 * @return Application preference - name of full album (media sub-directory)
	 */
	public final String getFullSubDirectory() {
		return Activator.getDefault().getPreferenceStore().getString(StoragesPreferenceIds.FULL_SUB_STORAGE);
	}
}
