package at.einspiel.simme.client.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * Class that provides easy and performant access to saved data.
 *
 * @author kariem
 */
public class Prefs {
   static final byte INITIAL_SIZE = 15;
   RecordStore rs;
   RecordEnumeration enum;
   String name;
   byte nbRecs;
   byte[] data;
   DataInputStream dis;
   DataOutputStream dos;
   ByteArrayOutputStream baos;

   /**
    * Creates a new Prefs object.
    *
    * @param theName name of preferences
    * @param nbRecords number of records
    */
   public Prefs(String theName, byte nbRecords) {
      setName(theName);
      setNbRecs(nbRecords);
      data = new byte[INITIAL_SIZE];
      dis = new DataInputStream(new ByteArrayInputStream(data));
      baos = new ByteArrayOutputStream();
      dos = new DataOutputStream(baos);
   }

   /**
    * Opens the recordset. This method has to be called before executing any
    * other operation on this instance.
    *
    * @throws PrefsException The "open" command cannot be successfully
    *         executed, if one of the following conditions occur:
    *         <ul>
    *           <li>RecordStore is full.
    *           <li>RecordStore could not be found.
    *           <li>RecordStore throws general error.
    *         </ul>
    *
    */
   public void open() throws PrefsException {
      try {
         rs = RecordStore.openRecordStore("user", true);
         enum = rs.enumerateRecords(null, null, false);
      } catch (RecordStoreFullException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreNotFoundException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * Closes the recordset. After closing the recordset may not be accessed any
    * more until another call to open has occured.
    *
    * @throws PrefsException The "close" command cannot be successfully
    *         executed, if one of the following conditions occur:
    *         <ul>
    *           <li>RecordStore is not open.
    *           <li>RecordStore throws general error.
    *         </ul>
    */
   public void close() throws PrefsException {
      if (rs != null) {
         try {
            rs.closeRecordStore();
         } catch (RecordStoreNotOpenException e1) {
            throw new PrefsException(e1.getMessage());
         } catch (RecordStoreException e1) {
            throw new PrefsException(e1.getMessage());
         }
      }
   }

   /**
    * Shows if there are remaining data elements in this object after the
    * current position.
    *
    * @return <code>true</code>, if there are objects; <code>false</code>
    *         otherwise.
    */
   public boolean hasNext() {
      return enum.hasNextElement();
   }

   /**
    * Shows if there are remaining data elements in this object before the
    * current position.
    *
    * @return <code>true</code>, if there are objects; <code>false</code>
    *         otherwise.
    */
   public boolean hasPrevious() {
      return enum.hasPreviousElement();
   }

   /**
    * Returns the current size of the record that is in use. It shows the number
    * of records.
    *
    * @return The number of records.
    *
    * @throws PrefsException if the RecordSet is not open.
    */
   public int currentSize() throws PrefsException {
      try {
         return rs.getNumRecords();
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * Reads the next byte and returns it.
    *
    * @return the next byte.
    *
    * @throws PrefsException see {@link RecordEnumeration#nextRecord()}
    */
   public byte[] readNext() throws PrefsException {
      try {
         return enum.nextRecord();
      } catch (InvalidRecordIDException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * Writes the next byte.
    *
    * @param b next byte
    *
    * @throws PrefsException see {@link RecordStore#setRecord(int, byte[], int, int)}.
    */
   public void writeNext(byte[] b) throws PrefsException {
      try {
         // go through recordset and change information
         int id = enum.nextRecordId();
         rs.setRecord(id, b, 0, b.length);
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      } catch (InvalidRecordIDException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreFullException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * Writes the next part of the byte output stream.
    *
    * @see #writeNext(byte[]).
    */
   public void writeNext() throws PrefsException {
      writeNext(baos.toByteArray());
      baos.reset();
   }

   /**
    * Adds a record.
    *
    * @param b the record to add as byte[].
    *
    * @throws PrefsException {@link RecordStore#addRecord(byte[], int, int)}
    */
   public void addNext(byte[] b) throws PrefsException {
      try {
         rs.addRecord(b, 0, b.length);
      } catch (RecordStoreNotOpenException e) {
         throw new PrefsException(e.getMessage());
      } catch (InvalidRecordIDException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreFullException e) {
         throw new PrefsException(e.getMessage());
      } catch (RecordStoreException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   /**
    * Adds the next record by using the output stream.
    *
    * @see #addNext(byte[]).
    */
   public void addNext() throws PrefsException {
      addNext(baos.toByteArray());
      baos.reset();
   }

   void writeToBuffer(String s) throws PrefsException {
      try {
         dos.writeUTF(s);
      } catch (IOException e) {
         throw new PrefsException(e.getMessage());
      }
   }

   void resetEnum() {
      enum.rebuild();
      enum.reset();
   }

   /**
    * Returns the name.
    *
    * @return this object's name.
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the name.
    *
    * @param theName The name to set
    */
   public void setName(String theName) {
      this.name = theName;
   }

   /**
    * Returns the number of records.
    *
    * @return byte
    */
   public byte getNbRecs() {
      return nbRecs;
   }

   /**
    * Sets the number of records.
    *
    * @param numberOfRecords The number of records to set
    */
   public void setNbRecs(byte numberOfRecords) {
      this.nbRecs = numberOfRecords;
   }
}
