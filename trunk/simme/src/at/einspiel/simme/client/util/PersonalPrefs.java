//----------------------------------------------------------------------------
//[Simme]
//  Java Source File: PersonalPrefs.java
//             $Date: 2004/09/22 18:25:42 $
//         $Revision: 1.16 $
//----------------------------------------------------------------------------
package at.einspiel.simme.client.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import at.einspiel.logging.Logger;

/**
 * Provides methods for accessing the <code>RecordStore<code> responsible for
 * saving data for personal information.
 *
 * @author kariem
 */
public class PersonalPrefs extends Prefs {
	private static final String RECORD_NAME = "personal";
	/** number of records */
	public static final byte NB_RECORDS = 4;
	private static PersonalPrefs instance = null;
	String[] savedData;
	byte[] recIds;

	/**
	 * Creates new Preferences used to save personal information.
	 */
	private PersonalPrefs() {
		super(RECORD_NAME, NB_RECORDS);
		savedData = new String[getNbRecs()];
	}

	/**
	 * Creates a new instance of <code>PersonalPrefs</code>. If this
	 * constructor has already been called a reference to the same instance will
	 * be returned.
	 * 
	 * @return A new instance.
	 */
	public static PersonalPrefs getInstance() {
		if (instance == null) {
			instance = new PersonalPrefs();
		}

		return instance;
	}

	/**
	 * Writes personal preferences from <code>savedData</code>
	 * 
	 * @throws PrefsException
	 *             If not opened before calling this method.
	 */
	public void update() throws PrefsException {
		resetEnum();
		Logger.debug("updating prefs");

		for (int i = 0; i < savedData.length; i++) {
			writeToBuffer(savedData[i]);
		}

		writeNext();
	}

	/**
	 * Saves personal preferences. This method is used, if the
	 * <code>RecordSet</code> does not have any data yet.
	 * 
	 * @throws PrefsException
	 *             If errors occur.
	 */
	public void save() throws PrefsException {
		resetEnum();
		Logger.debug("saving");

		for (int i = 0; i < savedData.length; i++) {
			writeToBuffer(savedData[i]);
		}

		addNext();
	}

	/**
	 * Loads personal preferences into <code>savedData</code>.
	 * 
	 * @throws PrefsException
	 *             If not opened before calling this method.
	 */
	public void load() throws PrefsException {
		try {
			//System.out.println("loading");
			byte[] b = readNext();
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(b));

			for (int i = 0; i < savedData.length; i++) {
				savedData[i] = inputStream.readUTF();

				//System.out.println("loaded: " + savedData[i]);
			}
		} catch (IOException e) {
			throw new PrefsException(e.getMessage());
		}
	}

	/**
	 * Returns the data saved for the preferences.
	 * 
	 * @return The data.
	 */
	public String[] getSavedData() {
		return savedData;
	}

	/**
	 * Returns the data saved for the preferences.
	 * 
	 * @param savedData
	 *            the data to be saved.
	 */
	public void setSavedData(String[] savedData) {
		this.savedData = savedData;
	}

	/**
	 * Returns the player information. After loading the current user
	 * preferences the player information is retrieved. If no information has
	 * been set yet, the
	 * 
	 * @return the player information as an array, like in
	 *         {@linkplain #getSavedData()}
	 * @throws PrefsException
	 *             if no preferences have been set.
	 * 
	 * @see #getSavedData()
	 */
	public static String[] getPlayerInfo() throws PrefsException {
		// get preferences
		PersonalPrefs prefs = PersonalPrefs.getInstance();

		// load nick name, password and additional info
		prefs.open();

		if (prefs.currentSize() == 0) {
			// empty prefs => error message
			StringBuffer buf = new StringBuffer();
			buf.append("Nickname und Passwort m�ssen eingegeben werden. Bitte ");
			buf.append("stellen Sie diese unter \"Einstellungen > Internet\" ");
			buf.append("im Hauptmen� ein.");
			throw new PrefsException(buf.toString());
		}

		prefs.load();
		return prefs.getSavedData();
	}
}