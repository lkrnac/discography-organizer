package sk.lkrnac.discorg.model.preferences;

import java.util.List;

import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.Activator;
import sk.lkrnac.discorg.general.constants.AudioFormatsPreferenceIds;

/**
 * This component holds audio formats preferences.
 * 
 * @author sitko
 */
@Service
public class AudioFormatsPreferences extends AbstractPreferences {
	private static final String AUDIO_FORMATS_DELIMITERS = ",|\\.|;"; //$NON-NLS-1$
	private List<String> lossy;
	private List<String> lossless;
	private List<String> warning;
	private boolean warningIfEmpty;

	/**
	 * Creates instance of the audio format preferences holder.
	 */
	public AudioFormatsPreferences() {
		super();
		readAudioFormats();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final String getDelimiters() {
		return AUDIO_FORMATS_DELIMITERS;
	}

	/**
	 * Reads audio formats from preference page.
	 */
	public final void readAudioFormats() {
		lossy = parsePreferenceString(AudioFormatsPreferenceIds.LOSSY_FORMATS);
		lossless = parsePreferenceString(AudioFormatsPreferenceIds.LOSSLESS_FORMATS);
		warning = parsePreferenceString(AudioFormatsPreferenceIds.WARNING_FORMATS);
		warningIfEmpty = Activator.getDefault().getPreferenceStore()
				.getBoolean(AudioFormatsPreferenceIds.WARNING_EMPTY_FLAG);
	}

	/**
	 * Checks if given extension is lossy audio format.
	 * 
	 * @param extension
	 *            string extension to check
	 * @return <code>true</code> if extension is lossy audio format
	 */
	public final boolean isLossyAudioFormat(String extension) {
		return lossy.contains(extension);
	}

	/**
	 * Checks if given extension is loss-less audio format.
	 * 
	 * @param extension
	 *            string extension to check
	 * @return <code>true</code> when extension is loss-less audio format
	 */
	public final boolean isLosslessAudioFormat(String extension) {
		return lossless.contains(extension);
	}

	/**
	 * Checks if given extension is audio format that should cause warning.
	 * 
	 * @param extension
	 *            - string extension to check
	 * @return <code>true</code> audio format that should cause warning
	 */
	public final boolean isWarningAudioFotmat(String extension) {
		return warning.contains(extension);
	}

	/**
	 * Get preference flag - empty directory should cause a warning.
	 * 
	 * @return preference flag
	 */
	public final boolean isWarningIfEmpty() {
		return warningIfEmpty;
	}
}
