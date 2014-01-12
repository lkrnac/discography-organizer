package sk.lkrnac.discorg.model.preferences;

import java.util.ArrayList;
import java.util.Collection;
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
	protected String getDelimiters() {
		return AUDIO_FORMATS_DELIMITERS;
	}

	/**
	 * Reads audio formats from preference page.
	 */
	public void readAudioFormats() {
		lossy = parsePreferenceString(AudioFormatsPreferenceIds.LOSSY_FORMATS);
		lossless = parsePreferenceString(AudioFormatsPreferenceIds.LOSSLESS_FORMATS);
		warning = parsePreferenceString(AudioFormatsPreferenceIds.WARNING_FORMATS);
		warningIfEmpty =
				Activator.getDefault().getPreferenceStore()
						.getBoolean(AudioFormatsPreferenceIds.WARNING_EMPTY_FLAG);

		audioFormatIntersectionCheck(lossy, lossless);
	}

	/**
	 * Performs intersection check for lossy and loss-less formats. Application
	 * expects that lossy format can't be loss-less at the same time and vice
	 * versa.
	 * 
	 * @param lossy
	 *            collection of lossy formats
	 * @param lossless
	 *            collection of loss-less formats
	 */
	private void audioFormatIntersectionCheck(Collection<String> lossy, Collection<String> lossless) {
		Collection<String> lossyDifference = new ArrayList<>(lossy);
		int lossyCount = lossy.size();
		lossyDifference.removeAll(lossless);
		if (lossyCount != lossyDifference.size()) {
			Collection<String> intersection = new ArrayList<>(lossy);
			intersection.removeAll(lossyDifference);

			//TODO: need to display some translated message to user
			throw new AudioFormatsPreferencesException(intersection.toString());
		}
	}

	/**
	 * Checks if given extension is lossy audio format.
	 * 
	 * @param extension
	 *            string extension to check
	 * @return <code>true</code> if extension is lossy audio format
	 */
	public boolean isLossyAudioFormat(String extension) {
		return lossy.contains(extension);
	}

	/**
	 * Checks if given extension is loss-less audio format.
	 * 
	 * @param extension
	 *            string extension to check
	 * @return <code>true</code> when extension is loss-less audio format
	 */
	public boolean isLosslessAudioFormat(String extension) {
		return lossless.contains(extension);
	}

	/**
	 * Checks if given extension is audio format that should cause warning.
	 * 
	 * @param extension
	 *            - string extension to check
	 * @return <code>true</code> audio format that should cause warning
	 */
	public boolean isWarningAudioFotmat(String extension) {
		return warning.contains(extension);
	}

	/**
	 * Get preference flag - empty directory should cause a warning.
	 * 
	 * @return preference flag
	 */
	public boolean isWarningIfEmpty() {
		return warningIfEmpty;
	}
}
