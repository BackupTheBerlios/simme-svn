package test.sim.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author kariem
 */
public class PersonalPrefs extends Prefs {

   private static final String RECORD_NAME = "personal";

   String[] savedData;
   byte[] recIds;

   static private PersonalPrefs _instance = null;

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
   /*
   public void update() throws PrefsException {
   	System.out.println("updating");
   	resetEnum();
   	for (int i = 0; i < savedData.length; i++) {
   		writeNext(savedData[i]);
   	}
   }*/

   public void update() throws PrefsException {
      resetEnum();
         System.out.println("updating");
         for (int i = 0; i < savedData.length; i++) {
				writeToBuffer(savedData[i]);
         }
         writeNext();
   }

   /*
   public void save() throws PrefsException {
   	System.out.println("saving");
   	resetEnum();
   	for (int i = 0; i < savedData.length; i++) {
   		addNext(savedData[i]);
   	}
   }
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
   /*
   public void load() throws PrefsException {
   	resetEnum();
   	System.out.println("loading");
   	// muss in umgekehrter Reihenfolge ausgeführt werden. warum? keine Ahnung
      for (int i = savedData.length-1; i >= 0; i--) {
         savedData[i] = readNextString();
      }
   }
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

   /*
   public void setSavedData(String s, int pos) throws PrefsException {
   	savedData[pos] = s;		
   }
   
   
   public String getSavedData(int pos) {
   	return savedData[pos];
   }
   */

   public String[] getSavedData() {
      return savedData;
   }

   public void setSavedData(String[] savedData) {
      this.savedData = savedData;
   }

}
