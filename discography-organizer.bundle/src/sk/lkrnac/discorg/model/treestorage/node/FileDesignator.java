package sk.lkrnac.discorg.model.treestorage.node;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sk.lkrnac.discorg.preferences.AudioFormatsPreferences;

/**
 * Service is used for resolving if file or directory contains some media data
 * @author sitko
 */
@Service
public class FileDesignator {
	@Autowired
	private AudioFormatsPreferences audioFormatPreferences;
	
	/**
	 * Indicates if file is directory containing audio files
	 * @param file directory that is being checked 
	 * @return <code>true</code> if directory contains audio files
	 * <br>code>false</code> if directory does not contain audio files or is not a 
	 * directory at all 
	 */
	public boolean isMediaDir(File file) {
		for (File subFile : file.listFiles()){
			if (isMediaFile(subFile) == true)
				return true;
		}
		return false;
	}
	
	/**
	 * Verifies if given file is loss-less audio file
	 * @param file file object that is being checked 
	 * @return true given file is loss-less audio file
	 */
	public boolean isLossLessMediaFile(File file){
		String extension = getExtension(file);
		if (audioFormatPreferences.isLosslessAudioFormat(extension))
			return true;
		return false;
	}

	/**
	 * Retrieves file extension
	 * @param file file object
	 * @return extension of the given file<br>
	 * empty string if the file does not have extension
	 */
	public static String getExtension(File file) {
		String extension = "";
		if (file.getName().lastIndexOf('.') > 0){
			extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
			extension = extension.toLowerCase();
		}
		return extension;
	}

	/**
	 * Verifies if given file is lossy audio file
	 * @param file file object that is being checked 
	 * @return true if given file is lossy audio file
	 */
	public boolean isLossyMediaFile(File file){
		String extension = getExtension(file);
		if (audioFormatPreferences.isLossyAudioFormat(extension))
			return true;
		return false;
	}
	
	/**
	 * Verifies if given file is audio file
	 * @param file file object that is being checked
	 * @return <code>true</code> given file is audio file
	 */
	public boolean isMediaFile(File file) {
		return isLossLessMediaFile(file) || isLossyMediaFile(file);
	}
	
}
