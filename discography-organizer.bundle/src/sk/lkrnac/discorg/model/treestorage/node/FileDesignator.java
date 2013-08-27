package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.general.context.WorkbenchEnvironmentFacade;
import sk.lkrnac.discorg.model.preferences.AudioFormatsPreferences;

/**
 * Service is used for resolving if file or directory contains some media data.
 * 
 * @author sitko
 */
@Service
public class FileDesignator {
	@Autowired
	private AudioFormatsPreferences audioFormatPreferences;

	@Autowired
	private WorkbenchEnvironmentFacade workbenchEnvironment;

	/**
	 * Indicates if file is directory containing audio files.
	 * 
	 * @param file
	 *            directory that is being checked
	 * @return <code>true</code> if directory contains audio files <br>
	 *         <code>false</code> if directory does not contain audio files or
	 *         is not a directory at all
	 */
	public final boolean isMediaDir(File file) {
		boolean result = false;
		for (File subFile : file.listFiles()) {
			if (isMediaFile(subFile)) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * Verifies if given file is loss-less audio file.
	 * 
	 * @param file
	 *            file object that is being checked
	 * @return true given file is loss-less audio file
	 */
	public final boolean isLossLessMediaFile(File file) {
		String extension = getExtension(file);
		boolean result = false;
		if (audioFormatPreferences.isLosslessAudioFormat(extension)) {
			result = true;
		}
		return result;
	}

	/**
	 * Retrieves file extension.
	 * 
	 * @param file
	 *            file object
	 * @return extension of the given file<br>
	 *         empty string if the file does not have extension
	 */
	public String getExtension(File file) {
		String extension = ""; //$NON-NLS-1$
		if (file.getName().lastIndexOf('.') > NumberUtils.INTEGER_ZERO) {
			extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);

			extension = extension.toLowerCase(workbenchEnvironment.getCurrentLocale());
		}
		return extension;
	}

	/**
	 * Verifies if given file is lossy audio file.
	 * 
	 * @param file
	 *            file object that is being checked
	 * @return true if given file is lossy audio file
	 */
	public final boolean isLossyMediaFile(File file) {
		boolean result = false;
		String extension = getExtension(file);
		if (audioFormatPreferences.isLossyAudioFormat(extension)) {
			result = true;
		}
		return result;
	}

	/**
	 * Verifies if given file is audio file.
	 * 
	 * @param file
	 *            file object that is being checked
	 * @return <code>true</code> given file is audio file
	 */
	public final boolean isMediaFile(File file) {
		return isLossLessMediaFile(file) || isLossyMediaFile(file);
	}

}
