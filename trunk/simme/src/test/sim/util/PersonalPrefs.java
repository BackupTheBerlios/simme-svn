package test.sim.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Provides methods for accessing the <code>RecordStore<code> responsible for
 * saving data for personal information. 
 * @author kariem
 */
public class PersonalPrefs extends Prefs {

   private static final String RECORD_NAME = "personal";

   String[] savedData;
   byte[] recIds;

   static private PersonalPrefs _instance = null;

	/**
	 * Creates a new instance of <code>PersonalPrefs</code>. If this constructor
	 * has already been called a reference to the same instance will be returned.
	 * @return A new instance.
	 */
   static public PersonalPrefs getInstance() {
      if (_instance == null) {
         _instance = new PersonalPrefs();
      }
      return _instance;
   }

   /**
    * @param name
    * @param nbRecords
    */
   private PersonalPrefs() {
      super(RECORD_NAME, (byte) 7);
      savedData = new String[getNumberOfRecords()];
      //recIds = new byte[getNumberOfRecords()];
   }

   /**
    * Writes personal preferences from <code>savedData</code>
    * @throws PrefsException If not opened before calling this method.
    */
   public void update() throws PrefsException {
      resetEnum();
      System.out.println("updating");
      for (int i = 0; i < savedData.length; i++) {
         writeToBuffer(savedData[i]);
      }
      writeNext();
   }


	/**
	 * Saves personal preferences. This method is used, if the
	 * <code>RecordSet</code> does not have any data yet.
	 * @throws PrefsException If errors occur.
	 */
   public void save() throws PrefsException {
      resetEnum();
      System.out.println("saving");
      for (int i = 0; i < savedData.length; i++) {
         writeToBuffer(savedData[i]);
      }
      addNext();
   }

   /**
    * Loads personal preferences into <code>savedData</code>.
    * @throws PrefsException If not opened before calling this method.
    */
   public void load() throws PrefsException {
      try {
         System.out.println("loading");
         byte[] b = readNext();
         DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
         for (int i = 0; i < savedData.length; i++) {
            savedData[i] = dis.readUTF();
            System.out.println("loaded: " + savedData[i]);
         }
      } catch (IOException e) {
         throw new PrefsException(e.getMessage());
      }
   }

	/**
	 * Returns the data saved for the preferences.
	 * @return The data.
	 */
   public String[] getSavedData() {
      return savedData;
   }

	/**
	 * Returns the data saved for the preferences.
	 * @return The data.
	 */
   public void setSavedData(String[] savedData) {
      this.savedData = savedData;
   }

}
