package sk.lkrnac.discorg.model.preferences;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Facade for accessing application preferences.
 * 
 * @author sitko
 */
@Controller
public class PreferencesListener implements IPropertyChangeListener {
	@Autowired
	private AudioFormatsPreferences audioFormatPreferences;

	@Autowired
	private FileNamesPreferences fileNamesPreferences;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void propertyChange(PropertyChangeEvent event) {
		// read and parse audio format preferences
		audioFormatPreferences.readAudioFormats();
		// read and parse file name preferences
		fileNamesPreferences.readFileNamesPreferences();
	}
}
