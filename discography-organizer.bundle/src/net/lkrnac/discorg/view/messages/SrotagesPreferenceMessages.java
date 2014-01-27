package net.lkrnac.discorg.view.messages;

import org.eclipse.osgi.util.NLS;

/**
 * Translations of storages preference page.
 * 
 * @author sitko
 * 
 */
public final class SrotagesPreferenceMessages extends NLS {
	private static final String BUNDLE_NAME = "net.lkrnac.discorg.view.messages" //$NON-NLS-1$
			+ ".storages-preference-messages"; //$NON-NLS-1$

	// SUPPRESS CHECKSTYLE VisibilityModifier 20 NLS implementation needs public
	// message fields

	/** Full album sub-directory preference field translation. */
	public static String fullAlbumSubdirectoryName;
	/** Input storage preference field translation. */
	public static String inputStorage;
	/** Loss-less output storage preference field translation. */
	public static String outputStorageLossless;
	/** Lossy output storage preference field translation. */
	public static String outputStorageLossy;
	/** Reference storage preference field translation. */
	public static String referenceStorage;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, SrotagesPreferenceMessages.class);
	}

	/**
	 * Avoid instantiation.
	 */
	private SrotagesPreferenceMessages() {
		super();
	}
}
